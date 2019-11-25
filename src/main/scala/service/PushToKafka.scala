package service

import example.googlehistory.{GoogleHistoryKey, GoogleHistoryValue}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord, RecordMetadata}
import parameters.Config

trait PushToKafka {

  def push(kafkaProducer: KafkaProducer[GoogleHistoryKey, GoogleHistoryValue], googleHistory: List[GoogleHistoryValue], config: Config): List[RecordMetadata] = {

    googleHistory.map { gh =>
      kafkaProducer.send(new ProducerRecord[GoogleHistoryKey, GoogleHistoryValue](config.topic, GoogleHistoryKey(gh.user_id), gh)).get()
    }
  }
}

object PushToKafka extends PushToKafka

