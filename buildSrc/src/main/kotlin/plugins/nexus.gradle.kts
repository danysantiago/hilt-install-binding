
plugins {
    id("io.github.gradle-nexus.publish-plugin")
}

nexusPublishing {
    repositories {
        sonatype {
            username.set("${properties["oss.user"] ?: System.getenv("OSS_USER")}")
            password.set("${properties["oss.token"] ?: System.getenv("OSS_TOKEN")}")
            stagingProfileId.set(
                "${properties["oss.stagingProfileId"] ?: System.getenv("OSS_STAGING_PROFILE_ID")}",
            )
        }
    }
}
