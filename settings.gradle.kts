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

        maven("https://maven.neoforged.net/releases/") {
            name = "NeoForged"
        }

        maven("https://maven.fabricmc.net") {
            name = "Fabric"
        }
    }
}

rootProject.name = "Compact Machines Core"

include("core-api")
include("core")
// include("common-compat")
include("room-api")
include("room-upgrade-api")
