
// give the user a nice default project!
ThisBuild / organization := "com.knoldus"
ThisBuild / scalaVersion := "2.12.8"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.12.4"

lazy val `greet` = (project in file("."))
  .aggregate(`greet-api`, `greet-impl`)

lazy val `greet-api` = (project in file("greet-api"))
  .settings(common: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomJavadslApi,
      lombok
    )
  )

lazy val `greet-impl` = (project in file("greet-impl"))
  .enablePlugins(LagomJava)
  .settings(common: _*)
  .settings(
    libraryDependencies ++= Seq(
      akkaPersistenceCouchbase,
      lagomJavadslPersistenceCouchbase,
      lagomJavadslKafkaBroker,
      lagomScaladslKafkaBroker,
      lagomLogback,
      lagomJavadslTestKit,
      lombok
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`greet-api`)

val lombok = "org.projectlombok" % "lombok" % "1.16.18"
val akkaPersistenceCouchbase = "com.lightbend.akka" %% "akka-persistence-couchbase" % "1.0-RC2"
val lagomJavadslPersistenceCouchbase = "com.lightbend.akka" %% "lagom-javadsl-persistence-couchbase" % "1.0-RC2"

def common: Seq[Def.Setting[Task[Seq[String]]]] = Seq(
  javacOptions in compile += "-parameters"
)

lagomCassandraEnabled in ThisBuild := false
lagomKafkaEnabled in ThisBuild := false

