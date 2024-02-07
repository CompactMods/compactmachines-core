val versionMain: String = System.getenv("VERSION") ?: "0.0.0"

plugins {
    id("java-platform")
    id("maven-publish")
}

javaPlatform.allowDependencies()

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
        runtime(it)
    }
}

publishing {
    publications {
        create<MavenPublication>("platform") {
            artifactId = "platform"
            from(components["javaPlatform"])
        }
    }
}