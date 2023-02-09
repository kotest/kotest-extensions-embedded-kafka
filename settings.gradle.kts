rootProject.name = "kotest-extensions-embedded-kafka"

dependencyResolutionManagement {
   repositories {
      mavenLocal()
      mavenCentral()
      maven {
         url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots")
      }
   }
}

pluginManagement {
   repositories {
      gradlePluginPortal()
      mavenCentral()
      maven {
         url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots")
      }
   }
}

plugins {
   `gradle-enterprise`
}

gradleEnterprise {
   buildScan {
      termsOfServiceUrl = "https://gradle.com/terms-of-service"
      termsOfServiceAgree = "yes"
      publishAlwaysIf(System.getenv("GITHUB_ACTIONS") == "true")
      publishOnFailure()
   }
}
