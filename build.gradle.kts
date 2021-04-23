// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.32")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.29.1-alpha")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()
    }
}

tasks.withType<Delete>() {
    delete(rootProject.buildDir)
}
