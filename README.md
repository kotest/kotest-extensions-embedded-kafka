# kotest-extensions-embedded-kafka

A kotest extension that spins up embedded kafka instances using the [embedded-kakfa library](https://github.com/embeddedkafka/embedded-kafka).

[![Build Status](https://github.com/kotest/kotest-extensions-embedded-kafka/workflows/master/badge.svg)](https://github.com/kotest/kotest/actions)
[<img src="https://img.shields.io/maven-central/v/io.kotest/kotest-extensions-embedded-kafka.svg?label=latest%20release"/>](http://search.maven.org/#search|ga|1|kotest)
![GitHub](https://img.shields.io/github/license/kotest/kotest-extensions-embedded-kafka)
[![kotest @ kotlinlang.slack.com](https://img.shields.io/static/v1?label=kotlinlang&message=kotest&color=blue&logo=slack)](https://kotlinlang.slack.com/archives/CT0G9SD7Z)




### Getting started:

Add the `io.kotest:kotest-extensions-embedded-kafka` module to your classpath.

Then register the `embeddedKafkaListener` listener in your test class:

```kotlin
class EmbeddedKafkaListenerTest : FunSpec({
  listener(embeddedKafkaListener)
})
```

or

```kotlin
class EmbeddedKafkaListenerTest : FunSpec() {
  init {
    listener(embeddedKafkaListener)
  }
}
```

And the broker will be started once the spec is created and stopped once the spec completes.

Note: The underlying embedded kafka library uses a global object for state. Do not start multiple kafka instances at the same time.

### Consumer / Producer

To create a consumer and producer we can use methods on the listener

```kotlin
class EmbeddedKafkaListenerTest : FunSpec({

   listener(embeddedKafkaListener)

   test("send / receive") {

     val producer = embeddedKafkaListener.stringStringProducer()
     producer.send(ProducerRecord("foo", "a"))
     producer.close()

     val consumer = embeddedKafkaListener.stringStringConsumer("foo")
     eventually(10.seconds) {
       consumer.poll(1000).first().value() shouldBe "a"
     }
     consumer.close()
   }

})
```


### Custom Ports

You can create a new instance of the listener specifying a port and then using that instance rather than
the default instance.

```kotlin
class EmbeddedKafkaCustomPortTest : FunSpec({

   val listener = EmbeddedKafkaListener(5678)
   listener(listener)

   test("send / receive") {

      val producer = listener.stringStringProducer()
      producer.send(ProducerRecord("foo", "a"))
      producer.close()

      val consumer = listener.stringStringConsumer("foo")
      eventually(10.seconds) {
         consumer.poll(1000).first().value() shouldBe "a"
      }
      consumer.close()
   }
})
```
