import java.text.SimpleDateFormat
import java.util.*

val versionMain: String = System.getenv("VERSION") ?: "0.0.0"

var coreApi: Project;
var roomApi: Project;
var roomUpgradeApi: Project;
if (rootProject.name == "Compact Machines Core") {
    coreApi = project(":core-api")
    roomApi = project(":room-api")
    roomUpgradeApi = project(":room-upgrade-api")
} else {
    coreApi = project(":core:core-api")
    roomApi = project(":core:room-api")
    roomUpgradeApi = project(":core:room-upgrade-api")
}

var cmModules = listOf(coreApi, roomApi, roomUpgradeApi)

plugins {
    java
    id("maven-publish")
    id("org.spongepowered.gradle.vanilla") version "0.2.1-SNAPSHOT"
}

cmModules.forEach {
    project.evaluationDependsOn(it.path)
}

minecraft {
    version(libraries.versions.minecraft.get())
}

base {
    group = "dev.compactmods.compactmachines"
    version = versionMain
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    withJavadocJar()
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

dependencies {
    cmModules.forEach {
        implementation(it)
        testImplementation(it)
    }

    compileOnly(libraries.feather)
    implementation(libraries.jnanoid)
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

val PACKAGES_URL = System.getenv("GH_PKG_URL") ?: "https://maven.pkg.github.com/compactmods/compactmachines-core"
publishing {
    publications.register<MavenPublication>("core") {
        artifactId = "core"
        from(components.getByName("java"))
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