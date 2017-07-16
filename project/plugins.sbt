resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)
//addSbtPlugin("org.scala-exercises" % "sbt-exercise" % "0.2.1-SNAPSHOT", "0.13", "2.10")
addSbtPlugin("org.ensime" % "ensime-sbt" % "0.4.0")
