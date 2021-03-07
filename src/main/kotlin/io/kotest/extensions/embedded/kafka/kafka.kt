@file:Suppress("MemberVisibilityCanBePrivate")

package io.kotest.extensions.embedded.kafka

import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.Spec
import net.manub.embeddedkafka.EmbeddedKafka
import net.manub.embeddedkafka.EmbeddedKafkaConfig
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.common.serialization.BytesDeserializer
import org.apache.kafka.common.serialization.BytesSerializer
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.kafka.common.utils.Bytes
import java.util.Properties

val embeddedKafkaListener: EmbeddedKafkaListener = EmbeddedKafkaListener(EmbeddedKafkaConfig.defaultConfig())

class EmbeddedKafkaListener(
   private val config: EmbeddedKafkaConfig,
) :
   TestListener {

   val port: Int = config.kafkaPort()

   val host: String = "127.0.0.1"

   override suspend fun beforeSpec(spec: Spec) {
      EmbeddedKafka.start(config)
      while (!EmbeddedKafka.isRunning()) {
         Thread.sleep(100)
      }
   }

   override suspend fun afterSpec(spec: Spec) {
      EmbeddedKafka.stop()
   }

   /**
    * Returns a kafka consumer configured with the details of the embedded broker.
    */
   fun stringStringConsumer(configure: Properties.() -> Unit = {}): KafkaConsumer<String, String> {
      val props = Properties()
      props[CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG] = "$host:$port"
      props[ConsumerConfig.GROUP_ID_CONFIG] = "test_consumer_group_" + System.currentTimeMillis()
      props[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
      props.configure()
      return KafkaConsumer(props, StringDeserializer(), StringDeserializer())
   }

   /**
    * Returns a kafka consumer subscribed to the given topic on the embedded broker.
    */
   fun stringStringConsumer(topic: String, configure: Properties.() -> Unit = {}): KafkaConsumer<String, String> {
      val consumer = stringStringConsumer()
      consumer.subscribe(listOf(topic))
      return consumer
   }

   /**
    * Returns a kafka consumer configured with the details of the embedded broker.
    */
   fun bytesBytesConsumer(configure: Properties.() -> Unit = {}): KafkaConsumer<Bytes, Bytes> {
      val props = Properties()
      props[CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG] = "$host:$port"
      props[ConsumerConfig.GROUP_ID_CONFIG] = "test_consumer_group_" + System.currentTimeMillis()
      props[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
      props.configure()
      return KafkaConsumer(props, BytesDeserializer(), BytesDeserializer())
   }

   /**
    * Returns a kafka consumer subscribed to the given topic on the embedded broker.
    */
   fun bytesBytesConsumer(topic: String, configure: Properties.() -> Unit = {}): KafkaConsumer<Bytes, Bytes> {
      val consumer = bytesBytesConsumer()
      consumer.subscribe(listOf(topic))
      return consumer
   }

   fun bytesBytesProducer(configure: Properties.() -> Unit = {}): KafkaProducer<Bytes, Bytes> {
      val props = Properties()
      props[CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG] = "$host:$port"
      props.configure()
      return KafkaProducer(props, BytesSerializer(), BytesSerializer())
   }

   fun stringStringProducer(configure: Properties.() -> Unit = {}): KafkaProducer<String, String> {
      val props = Properties()
      props[CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG] = "$host:$port"
      props.configure()
      return KafkaProducer(props, StringSerializer(), StringSerializer())
   }
}


