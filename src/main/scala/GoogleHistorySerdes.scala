import example.googlehistory.{GoogleHistoryKey, GoogleHistoryValue}
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde
import org.apache.avro.specific.SpecificRecordBase
import org.apache.kafka.common.serialization.Serde

class GoogleHistorySerdes(schemaRegistryUrl: String) {

  import SerdeHelper._

  val key = createSerde[GoogleHistoryKey](true, schemaRegistryUrl)
  val value = createSerde[GoogleHistoryValue](false, schemaRegistryUrl)

}

object SerdeHelper {
  def createSerde[T <: SpecificRecordBase](isKey: Boolean, schemaRegistryUrl: String): Serde[T] = {
    val serde = new SpecificAvroSerde[T]()

    val properties = new java.util.HashMap[String, String]()
    properties.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl)

    serde.configure(properties, isKey)
    serde
  }
}

