import java.text.SimpleDateFormat
import java.util.Date
import net.fabricmc.loom.task.RemapJarTask
import net.fabricmc.loom.task.RemapSourcesJarTask

val versionMain: String = System.getenv("CM_VERSION") ?: "0.0.0"
val mcVersion = property("minecraft_version") as String
val parchmentVersion = property("parchment_version") as String

val targets: List<String> = (property("enabled_platforms") as String).split(",")

plugins {
    java
    id("maven-publish")
    id("architectury-plugin") version "3.4-SNAPSHOT"
    id("dev.architectury.loom") version "1.0-SNAPSHOT"
}

architectury {
    this.minecraft = mcVersion
    common(targets)
}

loom {
    setGenerateSrgTiny(true)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    withJavadocJar()
    withSourcesJar()
}

repositories {
    maven("https://maven.parchmentmc.org") {
        name = "ParchmentMC"
    }
}

val loom = project.extensions.getByName<net.fabricmc.loom.api.LoomGradleExtensionAPI>("loom")
dependencies {
    minecraft("com.mojang:minecraft:${mcVersion}")

    // The following line declares the mojmap mappings, you may use other mappings as well
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${mcVersion}:${parchmentVersion}@zip")
    })

    compileOnly(project(":common-api")) {
        isTransitive = false
    }
}

tasks.withType<Jar> {
    manifest {
        val now = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(Date())
        attributes(mapOf(
                "Specification-Title" to "Compact Machines - Tunnels API",
                "Specification-Version" to "1", // We are version 1 of ourselves
                "Implementation-Title" to "Compact Machines - Tunnels API",
                "Implementation-Timestamp" to now,
                "FMLModType" to "GAMELIBRARY"
        ))
    }
}

tasks.withType<RemapSourcesJarTask> {
    targetNamespace.set("named")
}

tasks.named<RemapJarTask>("remapJar") {
    targetNamespace.set("named")
}

tasks.create<RemapJarTask>("remapSrgJar") {
    val out = tasks.getByName<Jar>("jar").archiveFile
    this.inputFile.set(out)

    targetNamespace.set("srg")
    archiveClassifier.set("srg")
}

val PACKAGES_URL = System.getenv("GH_PKG_URL") ?: "https://maven.pkg.github.com/compactmods/compactmachines-core"
publishing {
    publications.register<MavenPublication>("tunnels") {
        artifactId = "tunnels-api"
        from(components.getByName("java"))
        artifact(tasks.named("remapSrgJar"))
    }

    repositories {
        // GitHub Packages
        maven(PACKAGES_URL) {
            name = "GitHubPackages"
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}