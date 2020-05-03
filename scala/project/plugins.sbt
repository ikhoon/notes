resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)
//addSbtPlugin("org.scala-exercises" % "sbt-exercise" % "0.2.1-SNAPSHOT", "0.13", "2.10")

// addSbtPlugin("org.tpolecat" % "tut-plugin" % "0.6.2")

addSbtPlugin("com.typesafe.sbt" % "sbt-site" % "1.3.1")

addSbtPlugin("com.twitter" %% "scrooge-sbt-plugin" % "20.4.1")
