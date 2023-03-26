@file:Suppress("UnstableApiUsage")

val versionMain: String = System.getenv("CM_VERSION") ?: "0.0.0"
val mcVersion = property("minecraft_version") as String
val parchmentVersion = property("parchment_version") as String


//plugins {
//    java
//    id("maven-publish")
//    id("architectury-plugin") version "3.4-SNAPSHOT"
//    id("dev.architectury.loom") version "1.0-SNAPSHOT" apply false
//}
//
//
//architectury {
//    this.minecraft = mcVersion
//}
//
//repositories {
//    mavenCentral()
//    mavenLocal()
//
//    maven("https://maven.fabricmc.net") {
//        name = "Fabric"
//    }
//
//    maven("https://maven.parchmentmc.org") {
//        name = "ParchmentMC"
//        content {
//            includeGroup("org.parchmentmc.data")
//        }
//    }
//}
//
//allprojects {
//    apply(plugin = "java")
//    apply(plugin = "architectury-plugin")
//    apply(plugin = "maven-publish")
//
//    repositories {
//        maven("https://maven.parchmentmc.org") {
//            name = "ParchmentMC"
//        }
//    }
//
//    tasks.withType<JavaCompile> {
//        options.encoding = "UTF-8"
//        options.compilerArgs.add("-proc:none")
//        options.compilerArgs.addAll(arrayOf("-Xmaxerrs", "9000"))
//    }
//}