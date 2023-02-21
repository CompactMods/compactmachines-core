val targets: List<String> = (rootProject.property("enabled_platforms") as String).split(",")
architectury {
    common(targets)
}

loom {
    // accessWidenerPath = file("src/main/resources/examplemod.accesswidener")
}

java {
    withSourcesJar()
    withJavadocJar()
}

dependencies {
    implementation(project(":common-api"))
}

publishing {
    publications.register<MavenPublication>("tunnels") {
        artifactId = "tunnels-api"
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