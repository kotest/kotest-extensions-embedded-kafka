import org.gradle.api.tasks.testing.logging.TestExceptionFormat

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
   java
   `java-library`
   id("java-library")
   id("maven-publish")
   signing
   `maven-publish`
   alias(libs.plugins.kotlin.jvm)
}

group = "io.kotest.extensions"
version = Ci.version

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
      languageVersion.set(JavaLanguageVersion.of(11))
   }
}

tasks.named<Test>("test") {
   useJUnitPlatform()
   testLogging {
      showExceptions = true
      showStandardStreams = true
      exceptionFormat = TestExceptionFormat.FULL
   }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
   kotlinOptions {
      jvmTarget = "11"
      freeCompilerArgs += "-opt-in=kotlin.time.ExperimentalTime"
   }
}

// --- Publishing ------------------------------------------ //

val signingKey: String? by project
val signingPassword: String? by project

val publications: PublicationContainer = (extensions.getByName("publishing") as PublishingExtension).publications

signing {
   useGpgCmd()
   if (signingKey != null && signingPassword != null) {
      useInMemoryPgpKeys(signingKey, signingPassword)
   }
   if (Ci.isRelease) {
      sign(publications)
   }
}

java {
   withJavadocJar()
   withSourcesJar()
}

publishing {
   repositories {
      maven {
         val releasesRepoUrl = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
         val snapshotsRepoUrl = uri("https://oss.sonatype.org/content/repositories/snapshots/")
         name = "deploy"
         url = if (Ci.isRelease) releasesRepoUrl else snapshotsRepoUrl
         credentials {
            username = System.getenv("OSSRH_USERNAME") ?: ""
            password = System.getenv("OSSRH_PASSWORD") ?: ""
         }
      }
   }

   publications {
      register("mavenJava", MavenPublication::class) {
         from(components["java"])
         pom {
            name.set("kotest-extensions-embedded-kafka")
            description.set("Kotest extension for embedded kafka")
            url.set("http://www.github.com/kotest/kotest-extensions-embedded-kafka")

            scm {
               connection.set("scm:git:http://www.github.com/kotest/kotest-extensions-embedded-kafka")
               developerConnection.set("scm:git:http://github.com/sksamuel")
               url.set("http://www.github.com/kotest/kotest-extensions-embedded-kafka")
            }

            licenses {
               license {
                  name.set("The Apache 2.0 License")
                  url.set("https://opensource.org/licenses/Apache-2.0")
               }
            }

            developers {
               developer {
                  id.set("sksamuel")
                  name.set("Stephen Samuel")
                  email.set("sam@sksamuel.com")
               }
            }
         }
      }
   }
}
