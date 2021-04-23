import org.jetbrains.dokka.gradle.DokkaMultiModuleTask

plugins {
    id("org.jetbrains.dokka")
}

tasks {
    withType<DokkaMultiModuleTask>().configureEach {
        val dokkaDir = buildDir.resolve("dokka")
        outputDirectory.set(dokkaDir)
    }
}
