plugins {
  kotlin("jvm") version "1.9.0"
  application
}

group = "io.stefankreidel"
version = "1.0.0"


dependencies {
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

  implementation("io.github.microutils:kotlin-logging:3.0.5")
  implementation("ch.qos.logback:logback-classic:1.4.11")

  testImplementation(kotlin("test"))
}

tasks.test {
  useJUnitPlatform()
}

kotlin {
  jvmToolchain(17)
}

application {
  mainClass.set("MainKt")
}
