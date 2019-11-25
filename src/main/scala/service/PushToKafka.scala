package service

import example.googlehistory.{GoogleHistoryKey, GoogleHistoryValue}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

trait PushToKafka {

  def push(kafkaProducer: KafkaProducer[GoogleHistoryKey, GoogleHistoryValue], googleHistory: List[GoogleHistoryValue]) = {

    googleHistory.foreach { gh =>
      kafkaProducer.send(new ProducerRecord[GoogleHistoryKey, GoogleHistoryValue]("google_history_trip", GoogleHistoryKey(gh.user_id), gh))
    }
  }
}

object PushToKafka extends PushToKafka

