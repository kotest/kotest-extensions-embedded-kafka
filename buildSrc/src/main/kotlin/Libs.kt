object Libs {

   const val kotlinVersion = "1.4.31"
   const val org = "io.kotest.extensions"

   object Kotlin {
      const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion"
   }

   object Kotest {
      private const val version = "4.4.1"
      const val assertions = "io.kotest:kotest-assertions-core-jvm:$version"
      const val api = "io.kotest:kotest-framework-api:$version"
      const val junit5 = "io.kotest:kotest-runner-junit5-jvm:$version"
   }

   object embeddedkafka {
      private const val version = "2.7.0"
      const val kafka = "io.github.embeddedkafka:embedded-kafka_2.13:$version"
   }
}
