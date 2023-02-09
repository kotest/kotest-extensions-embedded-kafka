import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
   alias(libs.plugins.kotlin.jvm)
}

dependencies {
   implementation(libs.kotlin.stdlib)
   implementation(libs.kotest.framework.api)
   implementation(libs.embedded.kafka)

   testImplementation(libs.kotest.runner.junit5)
   testImplementation(libs.kotest.assertions)
   testImplementation(libs.slf4j.simple)
}

java {
   toolchain {
      languageVersion.set(JavaLanguageVersion.of(8))
   }
}

tasks.withType<Test>().configureEach {
   useJUnitPlatform()
   testLogging {
      showExceptions = true
      showStandardStreams = true
      events = setOf(
         TestLogEvent.FAILED,
         TestLogEvent.SKIPPED,
         TestLogEvent.STANDARD_ERROR,
         TestLogEvent.STANDARD_OUT
      )
      exceptionFormat = TestExceptionFormat.FULL
   }
}

tasks.withType<KotlinCompile> {
   kotlinOptions {
      freeCompilerArgs += "-opt-in=kotlin.time.ExperimentalTime"
      jvmTarget = "1.8"
      apiVersion = "1.6"
      languageVersion = "1.6"
   }
}
