package com.knoldus.greet.impl.dao;

import akka.stream.alpakka.couchbase.javadsl.CouchbaseSession;
import com.couchbase.client.java.document.json.JsonObject;
import com.knoldus.greet.api.models.UserGreeting;
import com.knoldus.greet.impl.util.Constants;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

public class CouchbaseRepository {
    
    private CouchbaseSession couchbaseSession;
    
    @Inject
    public CouchbaseRepository(CouchbaseSession couchbaseSession) {
        this.couchbaseSession = couchbaseSession;
    }
    
    public CompletionStage<List<UserGreeting>> listUserGreetings() {
        return couchbaseSession.get(Constants.DOC_ID)
                .thenApply(docOpt -> {
                    if (docOpt.isPresent()) {
                        JsonObject content = docOpt.get().content();
                        return content.getNames().stream().map(
                                name -> new UserGreeting(name, content.getString(name))
                        ).collect(Collectors.toList());
                    } else {
                        return Collections.emptyList();
                    }
                });
    }
}

