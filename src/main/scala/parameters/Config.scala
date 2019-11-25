package parameters

case class Config(filePath: String = "", kafkaBroker: String = "", schemaRegistryUrl: String = "", topic: String = "")

object Config {
  val parser = new scopt.OptionParser[Config]("scopt") {
    head("scopt", "3.x")

    opt[String]('f', "file-google-timeline")
      .action((x, c) => c.copy(filePath = x))
      .required()
      .text("file path to your google timeline kml file.")

    opt[String]("kafka-broker")
      .required()
      .action((x, c) => c.copy(kafkaBroker = x))
      .text("kafka broker url")

    opt[String]("schema-registry-url")
      .required()
      .action((x, c) => c.copy(schemaRegistryUrl = x))
      .text("schema registry url")

    opt[String]("topic")
      .required()
      .action((x, c) => c.copy(topic = x))
      .text("Kafka topic to push data to")
  }
}
