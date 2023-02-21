val targets: List<String> = (rootProject.property("enabled_platforms") as String).split(",")
architectury {
    common(targets)
}

plugins {
    id("maven-publish")
    id("fabric-loom")
}

java {
    withSourcesJar()
    withJavadocJar()
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

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-proc:none")
}

val PACKAGES_URL = System.getenv("GH_PKG_URL") ?: "https://maven.pkg.github.com/compactmods/compactmachines-core"
publishing {
    publications.register<MavenPublication>("api") {
        artifactId = "core-api"
        groupId = "dev.compactmods.compactmachines"

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