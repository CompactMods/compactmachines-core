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

afterEvaluate {
    println(rootProject.name)
    rootProject.childProjects.forEach {
        println(it.key)
    }
}

var deps: List<Project> = listOf(
        project(":core-api"),
        project(":room-api"),
        project(":room-upgrade-api")
)

dependencies {
    deps.forEach {
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