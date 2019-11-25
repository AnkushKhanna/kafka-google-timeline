name := "kafka_google_timeline"

version := "0.1"


scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-xml" % "1.0.5",
  "org.apache.kafka" %% "kafka" % "2.3.1",
  "io.circe" %% "circe-core" % "0.12.3",

  "org.apache.avro" % "avro" % "1.8.2",
  "io.confluent" % "kafka-connect-avro-converter" % "5.2.0",
  "io.confluent" % "kafka-streams-avro-serde" % "5.2.0",
  "io.circe" %% "circe-generic" % "0.12.3",
  "com.github.scopt" %% "scopt" % "3.7.1"
)
resolvers += "confluent" at "https://packages.confluent.io/maven/"

sourceGenerators in Compile += (avroScalaGenerateSpecific in Compile).taskValue