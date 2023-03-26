import java.text.SimpleDateFormat
import java.util.*
import net.fabricmc.loom.task.RemapJarTask
import net.fabricmc.loom.task.RemapSourcesJarTask

val versionMain: String = System.getenv("CM_VERSION") ?: "0.0.0"
val mcVersion = property("minecraft_version") as String
val parchmentVersion = property("parchment_version") as String

val targets: List<String> = (rootProject.property("enabled_platforms") as String).split(",")

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

repositories {
    mavenCentral() {
        content {
            includeGroup("com.aventrix.jnanoid")
        }
    }
}

sourceSets {
    named("main") {
        resources {
            //The API has no resources
            setSrcDirs(emptyList<String>())
        }
    }

    named("test") {
        resources {
            //The test module has no resources
            setSrcDirs(emptyList<String>())
        }
    }
}

base {
    group = "dev.compactmods.compactmachines"
    version = versionMain
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
    this.add("minecraft", "com.mojang:minecraft:${mcVersion}")

    // The following line declares the mojmap mappings, you may use other mappings as well
    this.add("mappings", loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${mcVersion}:${parchmentVersion}@zip")
    })

    implementation(project(":common-api"))
    implementation(project(":tunnels-api"))

    compileOnly("com.aventrix.jnanoid", "jnanoid", "2.0.0")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-proc:none")
    options.compilerArgs.addAll(arrayOf("-Xmaxerrs", "9000"))
}

tasks.withType<Jar> {
    manifest {
        val now = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(Date())
        attributes(mapOf(
                "Specification-Title" to "Compact Machines - Core",
                "Specification-Version" to "1", // We are version 1 of ourselves
                "Implementation-Title" to "Compact Machines - Core",
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
    publications.register<MavenPublication>("core") {
        artifactId = "core"
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