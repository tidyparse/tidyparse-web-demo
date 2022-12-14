plugins {
  kotlin("js") version "1.8.0-RC"
  id("com.github.ben-manes.versions") version "0.44.0"
}

group = "me.breandan"
version = "0.1-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

dependencies {
  testImplementation(kotlin("test"))
  implementation("org.jetbrains.kotlinx:kotlinx-html:0.8.1")
  implementation("ai.hypergraph:kaliningraph")
//  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.6.4")
}


kotlin {
    js {
      binaries.executable()
      browser()
    }
}