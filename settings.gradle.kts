dependencyResolutionManagement {
    versionCatalogs.create("libraries") {
        library("feather", "dev.compactmods:feather:0.1.6")
        library("jnanoid", "com.aventrix.jnanoid:jnanoid:2.0.0")

        version("minecraft", "1.20.4")
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
    id("org.gradle.toolchains.foojay-resolver-convention") version("0.5.0")
}

include("core")
include("core-api")
include("room-api")
include("room-upgrade-api")

include("platform")

