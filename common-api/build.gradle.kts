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

publishing {
    publications.register<MavenPublication>("api") {
        artifactId = "core-api"
        groupId = "dev.compactmods.compactmachines"

        from(components.getByName("java"))
    }

    repositories {
        // GitHub Packages
        maven("https://maven.pkg.github.com/CompactMods/CompactMachines") {
            name = "GitHubPackages"
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}