import java.util.Properties

import example.googlehistory.{GoogleHistoryKey, GoogleHistoryValue}
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerializer
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig}
import parameters.Config
import service.{ParseGoogleKml, PushToKafka}

object Application extends App {
  val config =
    Config.parser.parse(args, Config()) match {
      case Some(config) => config
      case None =>
        Config.parser.showHeader()
        throw new ExceptionInInitializerError()
    }

  val googleTimelineValues = ParseGoogleKml.parse(config, "1")

  val serdes = new GoogleHistorySerdes(config.schemaRegistryUrl)

  val kafkaProducer = new KafkaProducer[GoogleHistoryKey, GoogleHistoryValue](prop())

  val recordsMetadata = PushToKafka.push(kafkaProducer, googleTimelineValues, config)

  sys.addShutdownHook(() => {
    println(recordsMetadata.last.offset())
    println(recordsMetadata.last.timestamp())
  })


  def prop() = {
    val properties = new Properties()
    properties.put(ProducerConfig.CLIENT_ID_CONFIG, "google-history-client-id")
    properties.put(ProducerConfig.ACKS_CONFIG, "1")
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.kafkaBroker)
    properties.put("schema.registry.url", config.schemaRegistryUrl)
    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[SpecificAvroSerializer[GoogleHistoryKey]])
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[SpecificAvroSerializer[GoogleHistoryValue]])
    properties
  }

}
