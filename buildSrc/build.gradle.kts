
plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {

    // TODO: this is already fixed in IntelliJ IDEA 2021.1, when it is merged into Android Studio,
    //  remove the line below (https://github.com/gradle/gradle/issues/15383)
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.dagger.hilt.androidGradlePlugin)
    implementation(libs.dokka.gradlePlugin)
    implementation(libs.nexusGradlePlugin)
}
