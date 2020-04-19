plugins {
    kotlin("jvm") version "1.3.50"
}

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))


    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit5"))
//    testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.2")
//    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.5.1")
}

tasks.test {
    useJUnitPlatform()
}