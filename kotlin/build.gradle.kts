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

apply(from = "$rootDir/gradle/scripts/build-flags.gradle")

dependencies {
    // kotlin
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8")

    runtimeOnly("ch.qos.logback:logback-classic")

    // armeria
    implementation("com.linecorp.armeria:armeria")

    // test
    testImplementation("com.linecorp.armeria:armeria-testing-junit")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testRuntimeOnly("org.junit.platform:junit-platform-commons")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine")
    testImplementation("org.assertj:assertj-core")
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
