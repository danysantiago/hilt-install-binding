
plugins {
    docs
    nexus
}

group = checkNotNull(properties["libGroup"]?.toString())
version = checkNotNull(properties["libVersion"]?.toString())

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
