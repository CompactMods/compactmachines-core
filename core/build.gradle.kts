import java.text.SimpleDateFormat
import java.util.*
import net.fabricmc.loom.task.RemapJarTask
import net.fabricmc.loom.task.RemapSourcesJarTask

val versionMain: String = System.getenv("CM_VERSION") ?: "0.0.0"
val mcVersion = property("minecraft_version") as String
val parchmentVersion = property("parchment_version") as String
val feather_version = property("feather_version") as String

val targets: List<String> = (rootProject.property("enabled_platforms") as String).split(",")

plugins {
    java
    id("maven-publish")
    id("architectury-plugin") version "3.4-SNAPSHOT"
    id("dev.architectury.loom") version "1.1-SNAPSHOT"
}

architectury {
    this.minecraft = mcVersion
    common(targets)
}

loom {
    setGenerateSrgTiny(true)
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
    mavenCentral() {
        content {
            includeGroup("com.aventrix.jnanoid")
        }
    }

    maven("https://maven.parchmentmc.org") {
        name = "ParchmentMC"
    }

    maven("https://maven.pkg.github.com/compactmods/feather") {
        name = "Feather"
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
            password = project.findProperty("gpr.token") as String? ?: System.getenv("GITHUB_TOKEN")
        }
    }
}

val loom = project.extensions.getByName<net.fabricmc.loom.api.LoomGradleExtensionAPI>("loom")

val cmModules = listOf(
        project(":core-api"),
        project(":room-api"),
        project(":room-upgrade-api"),
        project(":tunnel-api")
)

dependencies {
    this.add("minecraft", "com.mojang:minecraft:${mcVersion}")

    // The following line declares the mojmap mappings, you may use other mappings as well
    this.add("mappings", loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${mcVersion}:${parchmentVersion}@zip")
    })

    cmModules.forEach {
        implementation(it)
        testImplementation(it)
    }

    compileOnly("com.aventrix.jnanoid", "jnanoid", "2.0.0")
    implementation("dev.compactmods", "feather", feather_version)
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