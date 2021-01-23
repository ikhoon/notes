plugins {
    id("base")
}

// Copy files
tasks.create<Copy>("copy") {
    description = "Copies source directories to destination"
    group = "custom"
    println("copy from src to desc")
    from("src")
    into("desc")
}

tasks.create<Zip>("zip") {
    description = "Archives source into a zip file"
    group = "custom"
    println("Archives source into a zip file")

    from("src")
    archiveFileName.set("gragle-note-0.2.zip")
}
