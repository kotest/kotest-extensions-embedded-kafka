rootProject.name = "buildSrc"

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
