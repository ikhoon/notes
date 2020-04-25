import com.google.protobuf.gradle.*

buildscript {
    dependencies {
        classpath("com.google.protobuf:protobuf-gradle-plugin:0.8.10")
    }
}
plugins {
    java
    idea
    `java-library`
    id("com.google.protobuf") version "0.8.10"
}

protobuf {
    // Configure the protoc executable
    protoc {
        // Download from repositories
        artifact = "com.google.protobuf:protoc:3.9.1"
    }

    // Locate the codegen plugins.
    plugins {
        // Locate a plugin with name 'grpc'.
        id("grpc") {
            // Download from the repository.
            artifact = "io.grpc:protoc-gen-grpc-java:1.24.0"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                // Apply the "grpc" plugin whose spec is defined above, without options.
                id("grpc")
            }
        }
    }
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
}

dependencies {
    // Adjust the list as you need.
    listOf("armeria",
            "armeria-grpc",
            "armeria-jetty",
            "armeria-kafka",
            "armeria-logback",
            "armeria-retrofit2",
            "armeria-rxjava",
            "armeria-saml",
            "armeria-thrift",
            "armeria-tomcat",
            "armeria-brave",
            "armeria-zookeeper").map {
        api("com.linecorp.armeria:$it:0.97.0")
    }

    // Project reactor
    api("io.projectreactor:reactor-core:3.3.0.RELEASE")

    // Logging
    runtimeOnly("ch.qos.logback:logback-classic:1.2.3")
    runtimeOnly("org.slf4j:log4j-over-slf4j:1.7.28")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.5.2")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_12
}

tasks {
    // Use the built-in JUnit support of Gradle.
    test {
        useJUnitPlatform()
    }
}
