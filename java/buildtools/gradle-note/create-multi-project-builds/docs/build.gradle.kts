plugins {
    id("org.asciidoctor.convert")
}

tasks.asciidoctor {
    println("Generate asciidoctor")
    sources(delegateClosureOf<PatternSet> {
        include("greeter.adoc")
    })
}

tasks.build {
    dependsOn(tasks.asciidoctor)
}
