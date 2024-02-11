val versionMain: String = System.getenv("VERSION") ?: "0.0.0"

plugins {
    id("java-library")
    id("maven-publish")
}

base {
    group = "dev.compactmods.compactmachines"
    version = versionMain
}

var cmModules: List<Project>;
if (rootProject.name == "Compact Machines Core") {
    cmModules = listOf(
            project(":core-api"),
            project(":room-api"),
            project(":room-upgrade-api")
    )
} else {
    cmModules = listOf(
            project(":core:core-api"),
            project(":core:room-api"),
            project(":core:room-upgrade-api")
    )
}

dependencies {
    cmModules.forEach {
        api(it)
    }
}