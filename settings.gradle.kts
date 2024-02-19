dependencyResolutionManagement {
    versionCatalogs.create("libraries") {
        library("feather", "dev.compactmods", "feather")
                .versionRef("feather")

        library("jnanoid", "com.aventrix.jnanoid", "jnanoid")
                .versionRef("jnanoid")

        version("minecraft", "1.20.4")
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

        maven("https://repo.spongepowered.org/repository/maven-public/") {
            name = "Sponge Snapshots"
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

include("platform")

