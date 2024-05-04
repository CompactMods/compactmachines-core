rootProject.name = "Compact Machines Core"

dependencyResolutionManagement {
    versionCatalogs.create("neoforged") {
        version("neoform", "7.0.119")
        plugin("vanilla", "net.neoforged.gradle.neoform")
            .versionRef("neoform")
    }

    versionCatalogs.create("mojang") {
        library("minecraft", "net.minecraft", "neoform_joined")
            .versionRef("neoform");

        version("neoform", "1.20.6-20240429.153634")
        version("minecraft", "1.20.6")
    }

    versionCatalogs.create("libraries") {
        library("feather", "dev.compactmods", "feather")
                .versionRef("feather")

        library("jnanoid", "com.aventrix.jnanoid", "jnanoid")
                .versionRef("jnanoid")

        version("feather", "[0.1.8, 2.0)")
        version("jnanoid", "[2.0.0, 3)")
    }
}

pluginManagement {
    repositories {
        mavenCentral()
        mavenLocal()
        gradlePluginPortal()

        maven("https://libraries.minecraft.net") {
            name = "Minecraft"
        }

        maven("https://maven.parchmentmc.org") {
            name = "ParchmentMC"
            content {
                includeGroup("org.parchmentmc.data")
            }
        }

        maven("https://maven.neoforged.net/releases") {
            name = "NeoForged"
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version("0.8.0")
}

include("core")
include("core-api")
include("room-api")
include("room-upgrade-api")

//include("platform")

