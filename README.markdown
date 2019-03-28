A [Giter8][g8] template for Lagom persistence with Couchbase

Template license
----------------
Written in 2019 by Pallavi Singh 

To the extent possible under law, the author(s) have dedicated all copyright and related
and neighboring rights to this template to the public domain worldwide.
This template is distributed without any warranty. See <http://creativecommons.org/publicdomain/zero/1.0/>.

[g8]: http://www.foundweekends.org/giter8/

## Lagom persistence with Couchbase
Template uses Couchbase Lagom Plugin to implement ES and CQRS.

The template uses PersistentEntity with Couchbase as a backend. 
Also, the read-side processors consumes events and update a couchbase document that can be queried.

## Clone the Project

- Execute the following commands in the terminal

    ```
     sbt new knoldus/lagom-es-couchbase.g8
     
     cd lagom-es-couchbase
     
     sbt clean compile
         
    ```

>## How to run the Application

- Run Couchbase service.

  - Docker image can be used to run Couchbase locally without installation.
  - Or use the link to install Couchbase: <https://docs.couchbase.com/server/6.0/install/ubuntu-debian-install.html>

- In the application.conf in the application, set the following configuration values or default will be used:

    ```
        couchbase.bucket = <Bucket_Name>
        couchbase.connection {
          nodes = ["localhost"]
          username = <Username>
          password = <Password>
        }
    ```

- Pre-create the bucket and the following indexes on it to support cqrs and es

    ```
        // For Journal
        CREATE INDEX `persistence-ids` on `akka` (`persistence_id`)
          WHERE `type` = "journal_message"

        CREATE INDEX `sequence-nrs` on `akka`
          (DISTINCT ARRAY m.sequence_nr FOR m in messages END)
          WHERE `type` = "journal_message"

        // For query side with event-for-tags
        CREATE INDEX `tags` ON
        `akka`((ALL (ARRAY (ALL (ARRAY [`t`, (`m`.`ordering`)] FOR `t` IN (`m`.`tags`) END)) FOR `m` IN `messages` END)))
        WHERE (`type` = "journal_message")

        CREATE INDEX `tag-seq-nrs` ON
        `akka`((ALL (ARRAY (ALL (ARRAY [`persistence_id`, `t`.`tag`, `t`.`seq_nr`] FOR `t` IN (`m`.`tag_seq_nrs`) END)) FOR `m` IN `messages` END)))
        WHERE (`type` = "journal_message")

        // For Snapshot plugin
        CREATE INDEX `snapshots` ON `akka` (persistence_id, sequence_nr)
          WHERE akka.type = "snapshot"
    ```

    Note : Please update the bucket name in the indexes if using other than the default one. And ensure that the indexes are online.

- Invoke `sbt runAll` to start up the service

- To see the records ingested in Couchbase make the following curl command

     ```
     curl -X GET http://localhost:9000/api/user-greetings
     ```

    Alternatively you can also use a curl command to make a write request to the Couchbase using the Lagom application

    ```
    curl -X POST http://localhost:9000/api/greet/Akka '{"message": "Hey One??"}'
    ```
    
 Note: Postman collection can be found in g8 folder under /postman-collection

