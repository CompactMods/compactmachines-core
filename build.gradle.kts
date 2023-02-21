@file:Suppress("UnstableApiUsage")

import net.fabricmc.loom.task.RemapJarTask

val modId = rootProject.property("mod_id") as String

val versionMain: String = System.getenv("CM_SEMVER_VERSION") ?: "9.9.9"
val versionBuild: String = System.getenv("CM_BUILD_NUM") ?: "0"
val isRelease: Boolean = (System.getenv("CM_RELEASE") ?: "false").equals("true", true)
val modVersion = if (isRelease) versionMain else "${versionMain}.${versionBuild}-nightly"

plugins {
    java
    id("maven-publish")
    id("architectury-plugin") version "3.4-SNAPSHOT"
    id("dev.architectury.loom") version "1.0-SNAPSHOT" apply false
}

val mcVersion = rootProject.extra.get("minecraft_version") as String
val parchmentVersion = rootProject.property("parchment_version") as String

architectury {
    this.minecraft = mcVersion
}

repositories {
    mavenCentral()
    mavenLocal()

    maven("https://maven.fabricmc.net") {
        name = "Fabric"
    }

    maven("https://maven.parchmentmc.org") {
        name = "ParchmentMC"
        content {
            includeGroup("org.parchmentmc.data")
        }
    }
}

subprojects {
    plugins.apply("dev.architectury.loom")
    plugins.apply("java")
    plugins.apply("maven-publish")

    base {
        group = "dev.compactmods.compactmachines"
        version = modVersion
    }

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    }

    val loom = project.extensions.getByName<net.fabricmc.loom.api.LoomGradleExtensionAPI>("loom")
    dependencies {
        this.add("minecraft", "com.mojang:minecraft:${mcVersion}")

        // The following line declares the mojmap mappings, you may use other mappings as well
        this.add("mappings", loom.layered {
            officialMojangMappings()
            parchment("org.parchmentmc.data:parchment-${mcVersion}:${parchmentVersion}@zip")
        })
    }

    tasks.withType<RemapJarTask> {
        targetNamespace.set("srg")
    }
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "architectury-plugin")
    apply(plugin = "maven-publish")

    repositories {
        // Add repositories to retrieve artifacts from in here.
        // You should only use this when depending on other mods because
        // Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
        // See https://docs.gradle.org/current/userguide/declaring_repositories.html
        // for more information about repositories.
        maven("https://maven.parchmentmc.org") {
            name = "ParchmentMC"
        }
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.compilerArgs.add("-proc:none")
        options.compilerArgs.addAll(arrayOf("-Xmaxerrs", "9000"))
    }
}

subprojects {

}