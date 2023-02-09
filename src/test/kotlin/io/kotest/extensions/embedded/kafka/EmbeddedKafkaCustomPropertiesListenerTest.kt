package io.kotest.extensions.embedded.kafka

import io.github.embeddedkafka.EmbeddedKafka
import io.kotest.assertions.timing.eventually
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.apache.kafka.clients.producer.ProducerRecord
import java.time.Duration
import java.time.temporal.ChronoUnit
import kotlin.time.Duration.Companion.seconds

class EmbeddedKafkaCustomPropertiesListenerTest : FunSpec({

   val listener = EmbeddedKafkaListener(6002)
   listener(listener)

   test("kafka should startup") {
      eventually(10.seconds) {
         EmbeddedKafka.isRunning() shouldBe true
      }
   }

   test("send / receive") {

      val producer = listener.stringStringProducer()
      producer.send(ProducerRecord("foo", "a"))
      producer.close()

      val consumer = listener.stringStringConsumer("foo")
      eventually(10.seconds) {
         consumer.poll(Duration.of(1, ChronoUnit.SECONDS)).first().value() shouldBe "a"
      }
      consumer.close()
   }

   afterProject {
      EmbeddedKafka.isRunning() shouldBe false
   }
})
