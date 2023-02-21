val targets: List<String> = (rootProject.property("enabled_platforms") as String).split(",")
architectury {
    common(targets)
}

java {
    withSourcesJar()
    withJavadocJar()
}

dependencies {
    implementation(project(":common-api"))
}

val PACKAGES_URL = System.getenv("GH_PKG_URL") ?: "https://maven.pkg.github.com/compactmods/compactmachines-core"
publishing {
    publications.register<MavenPublication>("tunnels") {
        artifactId = "tunnels-api"
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