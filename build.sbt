name := "the-notes"

version := "1.0"
scalaVersion := "2.12.4"
//scalaOrganization in ThisBuild := "org.typelevel"

useJCenter := true

lazy val `scala-notes` = (project in file("."))
  .dependsOn(`scala-exercise`,  `macro-exercise`)

lazy val `scala-exercise` = (project in file("scala-exercise"))
  .settings(commonSettings)
  .enablePlugins(TutPlugin)
  .dependsOn(`macro-exercise`)

lazy val `macro-exercise` = (project in file("macro-exercise"))
  .settings(commonSettings)

lazy val commonSettings = Seq(
  scalaVersion := "2.12.4",
//  scalaOrganization in ThisBuild := "org.typelevel",
//  scalacOptions += "-Xfatal-warnings",
  scalacOptions ++= Seq(
    // See other posts in the series for other helpful options
    "-target:jvm-1.8",
    "-encoding", "UTF-8",
    "-unchecked",
    "-deprecation",
    "-Xfuture",
//    "-Xfatal-warnings",
    //  "-Yno-adapted-args",
    "-Ywarn-dead-code",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
    "-Ywarn-unused",
    "-Ypartial-unification",
    "-feature",
    "-language:existentials",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-language:experimental.macros",
    "-language:reflectiveCalls",
    "-language:postfixOps",
    "-Xplugin-require:macroparadise"

  ),
  resolvers ++= Seq(
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots"),
    Resolver.jcenterRepo
  ),
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-core" % "1.1.0",
    "org.typelevel" %% "cats-effect" % "0.10",
    "io.monix" %% "monix" % "3.0.0-M3",
//    "org.typelevel" %% "cats" % "0.9.0",
    "com.chuusai" %% "shapeless" % "2.3.2",
    "org.tpolecat" %% "doobie-core" % "0.5.2",
    "org.tpolecat" %% "doobie-h2" % "0.5.2",
    "org.scalactic" %% "scalactic" % "3.0.1",

    "com.github.mpilquist" %% "simulacrum" % "0.10.0",
    "com.thoughtworks.each" %% "each" % "3.3.1",
    "org.atnos" %% "eff" % "4.2.0",

    // fs2
    "co.fs2" %% "fs2-core" % "0.10.3",
    "co.fs2" %% "fs2-io" % "0.10.3",

    // freestyle
    "io.frees" %% "frees-core" % "0.6.2",
    // optional - effects and patterns
    "io.frees" %% "frees-effects"      % "0.6.2",
//    "io.frees" %% "frees-tagless"      % "0.6.2",

    "com.twitter" %% "util-core" % "6.45.0",

    "eu.timepit" %% "refined"            % "0.8.2",

    // rxjava
    "io.reactivex.rxjava2" % "rxjava" % "2.1.6",
    "org.twitter4j" % "twitter4j-core" % "4.0.0",
    "org.twitter4j" % "twitter4j-stream" % "4.0.0",
    "org.asynchttpclient" % "async-http-client" % "2.0.0",

    "org.scalaz" %% "scalaz-core" % "7.2.16",
    "org.scalaz" %% "scalaz-concurrent" % "7.2.16",


    "org.scalatest" %% "scalatest" % "3.0.1" % "test",
    "junit" % "junit" % "4.12" % "test",

    "com.github.julien-truffaut" %%  "monocle-core"  % "1.5.0-cats",
    "com.github.julien-truffaut" %%  "monocle-macro" % "1.5.0-cats",
    "com.github.julien-truffaut" %%  "monocle-law"   % "1.5.0-cats" % "test",
    // scala meta
    "org.scalameta" %% "scalameta" % "3.7.2",
    "org.scalameta" %% "contrib" % "3.7.2",

    "org.scala-lang" % "scala-compiler" % scalaVersion.value
  ),
  addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.3"),
  addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
  addCompilerPlugin("org.scalameta" % "paradise" % "3.0.0-M10" cross CrossVersion.full)


)



