pluginManagement {
    repositories {
        mavenCentral()
        mavenLocal()
        gradlePluginPortal()

        maven("https://maven.architectury.dev/")

        maven("https://libraries.minecraft.net") {
            name = "Minecraft"
        }

        maven("https://maven.parchmentmc.org") {
            name = "ParchmentMC"
            content {
                includeGroup("org.parchmentmc.data")
            }
        }

        maven("https://maven.minecraftforge.net") {
            name = "Forge"
        }

        maven("https://maven.fabricmc.net") {
            name = "Fabric"
        }
    }

    plugins {
        id("fabric-loom").version(settings.extra["loom_version"] as String)
    }
}

rootProject.name = "Compact Machines Core"

include("common-api")
include("common-main")
include("tunnels-api")

// fabric.loom.multiProjectOptimisation=true
