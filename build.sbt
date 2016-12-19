name := "scala-exersiece-notes"

version := "1.0"

scalaVersion := "2.11.8"

resolvers += Resolver.sonatypeRepo("releases")

libraryDependencies += "org.typelevel" %% "cats" % "0.8.1"
libraryDependencies += "com.chuusai" %% "shapeless" % "2.3.2"
libraryDependencies += "org.tpolecat" %% "doobie-core" % "0.3.0"
libraryDependencies += "org.tpolecat" %% "doobie-contrib-h2" % "0.3.0"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0" % "test"
libraryDependencies += compilerPlugin("org.spire-math" %% "kind-projector" % "0.7.1")
