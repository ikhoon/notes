plugins {
    java
    application
}

dependencies {
    implementation(project(":greeting-library"))
}

application {
    mainClassName = "greeter.Greeter"
}

tasks.distZip {
   from(project(":docs").tasks["asciidoctor"]) {
       into("${project.name}-$archiveVersion")
   }
}

tasks.distTar {
    from(project(":docs").tasks["asciidoctor"]) {
        into("${project.name}-$archiveVersion")
    }
}