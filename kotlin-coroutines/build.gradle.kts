plugins {
  kotlin("jvm") version "1.9.0"
  application
}

group = "io.stefankreidel"
version = "1.0.0"


dependencies {
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
