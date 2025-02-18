plugins {
    java
    application
    alias(refinery.plugins.shadow)
}

group = "tools.refinery.geometric-refinery"
version = "1.0-SNAPSHOT"

application {
    mainClass = "tools.refinery.geometric_refinery.MainClass"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(refinery.store)
    implementation(refinery.store.query)
    implementation(refinery.store.query.interpreter)
    implementation(refinery.store.dse)
    implementation(refinery.store.reasoning)
    implementation(refinery.generator)
}
