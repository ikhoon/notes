resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)
//addSbtPlugin("org.scala-exercises" % "sbt-exercise" % "0.2.1-SNAPSHOT", "0.13", "2.10")

addSbtPlugin("com.typesafe.sbt" % "sbt-site" % "1.3.1")

addSbtPlugin("com.twitter" %% "scrooge-sbt-plugin" % "20.4.1")

addSbtPlugin("com.thesamet" % "sbt-protoc" % "1.0.0-RC4")

libraryDependencies += "com.thesamet.scalapb" %% "compilerplugin" % "0.10.9"
