import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.google.osdetector") version "1.6.2" apply false
    kotlin("jvm") version "1.3.72"
}

allprojects {
    repositories {
        mavenCentral()
    }
}

apply(from = "${rootDir}/gradle/scripts/build-flags.gradle")

dependencies {
    // kotlin
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")

    runtimeOnly("org.slf4j:slf4j-simple")

    // armeria
    implementation("com.linecorp.armeria:armeria")

    // test
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
    javaParameters = true
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}