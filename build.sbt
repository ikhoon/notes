name := "scala-exersiece-notes"

version := "1.0"

scalaVersion := "2.11.8"

scalacOptions ++= Seq(
  // See other posts in the series for other helpful options
  "-target:jvm-1.8",
  "-encoding", "UTF-8",
  "-unchecked",
  "-deprecation",
  "-Xfuture",
//  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Ywarn-unused",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions"
)

resolvers += Resolver.sonatypeRepo("releases")

libraryDependencies += "org.typelevel" %% "cats" % "0.8.1"
libraryDependencies += "com.chuusai" %% "shapeless" % "2.3.2"
libraryDependencies += "org.tpolecat" %% "doobie-core" % "0.3.0"
libraryDependencies += "org.tpolecat" %% "doobie-contrib-h2" % "0.3.0"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0" % "test"
libraryDependencies += compilerPlugin("org.spire-math" %% "kind-projector" % "0.7.1")

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

libraryDependencies += "com.github.mpilquist" %% "simulacrum" % "0.10.0"
