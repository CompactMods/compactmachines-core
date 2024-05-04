
plugins {
    alias(neoforged.plugins.vanilla).apply(false)
}

subprojects {
    afterEvaluate {
        extensions.configure<JavaPluginExtension> {
            // toolchain.vendor.set(JvmVendorSpec)
            toolchain.languageVersion.set(JavaLanguageVersion.of(21))
        }
    }
}
