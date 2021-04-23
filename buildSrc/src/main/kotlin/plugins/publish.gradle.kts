import com.android.build.gradle.LibraryExtension
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    `maven-publish`
    signing
    id("org.jetbrains.dokka")
}

val docsJar =
    tasks.create<Jar>("dokkaJar") {
        group = "build"
        description = "Assembles Javadoc jar from Dokka API docs"
        archiveClassifier.set("javadoc")
        dependsOn(tasks.named<DokkaTask>("dokkaHtml"))
    }

val sourcesJar =
    tasks.register<Jar>("sourcesJar") {
        group = "build"
        description = "Assembles Source jar for publishing"
        archiveClassifier.set("sources")
        if (plugins.hasPlugin("com.android.library")) {
            from(
                (project.extensions.getByName("android") as LibraryExtension)
                    .sourceSets
                    .named("main")
                    .get()
                    .java
                    .srcDirs
            )
        } else {
            from(
                (project.extensions.getByName("sourceSets") as SourceSetContainer)
                    .named("main")
                    .get()
                    .allSource
            )
        }
    }

afterEvaluate {
    configure<PublishingExtension> {
        publications {
            withType<MavenPublication> {
                pom {
                    name.set(property("pomName").toString())
                    description.set(property("pomDescription").toString())
                    url.set(property("pomUrl").toString())

                    licenses {
                        license {
                            name.set(property("pomLicenseName").toString())
                            url.set(property("pomLicenseUrl").toString())
                        }
                    }

                    developers {
                        developer {
                            id.set(property("pomDeveloperId").toString())
                            name.set(property("pomDeveloperName").toString())
                            email.set(property("pomDeveloperEmail").toString())
                        }
                    }

                    scm {
                        url.set(property("pomSmcUrl").toString())
                        connection.set(property("pomSmcConnection").toString())
                        developerConnection.set(property("pomSmcDeveloperConnection").toString())
                    }
                }

                artifact(docsJar)
                artifact(sourcesJar)
            }

            create<MavenPublication>("release") {
                if (plugins.hasPlugin("com.android.library")) from(components["release"])
                else from(components["java"])
            }
        }
    }
}

signing {
    if (!project.version.toString().endsWith("-SNAPSHOT")) {
        useGpgCmd()
        sign(publishing.publications)
    }
}
