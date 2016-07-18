name := "scala-exersiece-notes"

version := "1.0"

scalaVersion := "2.11.8"

resolvers += Resolver.sonatypeRepo("releases")

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.8.0")

libraryDependencies += "org.typelevel" %% "cats" % "0.6.1"
libraryDependencies += "org.scalactic" %% "scalactic" % "2.2.6"
libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % "test"


