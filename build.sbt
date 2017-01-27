name := "the-notes"

version := "1.0"
scalaVersion := "2.11.8"

lazy val `the-notes` = (project in file("."))
  .dependsOn(`scala-exercise`,  `macro-exercise`)

lazy val `scala-exercise` = (project in file("scala-exercise"))
  .settings(commonSettings)
  .dependsOn(`macro-exercise`)

lazy val `macro-exercise` = (project in file("macro-exercise"))
  .settings(commonSettings)

lazy val commonSettings = Seq(
  scalaVersion := "2.11.8",
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
    "-language:implicitConversions"),
  resolvers += Resolver.sonatypeRepo("releases"),
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats" % "0.8.1",
    "com.chuusai" %% "shapeless" % "2.3.2",
    "org.tpolecat" %% "doobie-core" % "0.3.0",
    "org.tpolecat" %% "doobie-contrib-h2" % "0.3.0",
    "org.scalactic" %% "scalactic" % "3.0.0",
    "org.scalatest" %% "scalatest" % "3.0.0" % "test",
    "com.github.mpilquist" %% "simulacrum" % "0.10.0",
    "com.thoughtworks.each" %% "each" % "3.1.1",
    compilerPlugin("org.spire-math" %% "kind-projector" % "0.7.1"),
    "org.scala-lang" % "scala-compiler" % scalaVersion.value


  ),
  addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
)


