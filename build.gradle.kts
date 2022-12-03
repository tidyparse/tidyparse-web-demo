plugins {
    kotlin("js") version "1.7.10"
}

group = "me.breandan"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

dependencies {
  testImplementation(kotlin("test"))
  implementation("org.jetbrains.kotlinx:kotlinx-html:0.8.0")
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

