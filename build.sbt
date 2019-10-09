name := "the-note"

version := "1.0"
scalaVersion := "2.12.10"
//scalaOrganization in ThisBuild := "org.typelevel"

useJCenter := true

lazy val `the-note` = (project in file("."))
  .dependsOn(`code`, `macro`)
  .aggregate(`code`, `macro`)

lazy val `code` = (project in file("code"))
  .settings(commonSettings)
  .enablePlugins(TutPlugin)
  .dependsOn(`macro`)

lazy val `macro` = (project in file("macro"))
  .settings(commonSettings)

lazy val commonSettings = Seq(
  scalaVersion := "2.12.10",
  //  scalaOrganization in ThisBuild := "org.typelevel",
  //  scalacOptions += "-Xfatal-warnings",
  scalacOptions ++= Seq(
    // See other posts in the series for other helpful options
    "-target:jvm-1.8",
    "-encoding", "UTF-8",
    "-unchecked",
//    "-deprecation",
    "-Xfuture",
    //    "-Xfatal-warnings",
    //  "-Yno-adapted-args",
//    "-Ywarn-dead-code",
//    "-Ywarn-numeric-widen",
//    "-Ywarn-value-discard",
//    "-Ywarn-unused",
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
    "org.typelevel" %% "cats-core" % "1.4.0",
    "org.typelevel" %% "cats-effect" % "1.1.0",
    "org.typelevel" %% "cats-mtl-core" % "0.4.0",
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
    "co.fs2" %% "fs2-core" % "1.0.0-M3",
    "co.fs2" %% "fs2-io" % "1.0.0-M3",

    // freestyle
    "io.frees" %% "frees-core" % "0.6.2",
    // optional - effects and patterns
    "io.frees" %% "frees-effects" % "0.6.2",
    //    "io.frees" %% "frees-tagless"      % "0.6.2",

    "com.twitter" %% "util-core" % "6.45.0",

    "eu.timepit" %% "refined" % "0.8.2",

    // reative streams
    "org.reactivestreams" % "reactive-streams" % "1.0.3",
    "org.reactivestreams" % "reactive-streams-tck" % "1.0.3",
    // rxjava
    "io.reactivex.rxjava2" % "rxjava" % "2.1.6",
    "org.twitter4j" % "twitter4j-core" % "4.0.0",
    "org.twitter4j" % "twitter4j-stream" % "4.0.0",
    "org.asynchttpclient" % "async-http-client" % "2.0.0",
    // reactor
    "io.projectreactor" % "reactor-core" % "3.2.12.RELEASE",


    // A reactive (or non-blocking, or asynchronous) JSON parser
    "de.undercouch" % "actson" % "1.2.0",
    // reactive mongo driver
    "org.mongodb" % "mongodb-driver-reactivestreams" % "1.12.0",

    // reactor core
    "io.projectreactor" % "reactor-core" % "3.3.0.RELEASE",

    // akka streams
    "com.typesafe.akka" %% "akka-stream" % "2.5.25",

    "org.scalaz" %% "scalaz-core" % "7.2.16",
    "org.scalaz" %% "scalaz-concurrent" % "7.2.16",

    "io.circe" %% "circe-core" % "0.9.3",
    "io.circe" %% "circe-generic" % "0.9.3",
    "io.circe" %% "circe-parser" % "0.9.3",
    "io.circe" %% "circe-shapes" % "0.9.3",
    "io.circe" %% "circe-generic-extras" % "0.9.3",


    "org.scalatest" %% "scalatest" % "3.0.1" % "test",
    "junit" % "junit" % "4.12" % "test",
    "org.awaitility" % "awaitility" % "3.1.6" % "test",

    "com.github.julien-truffaut" %% "monocle-core" % "1.5.0-cats",
    "com.github.julien-truffaut" %% "monocle-macro" % "1.5.0-cats",
    "com.github.julien-truffaut" %% "monocle-law" % "1.5.0-cats" % "test",
    // scala meta
    "org.scalameta" %% "scalameta" % "3.7.2",
    "org.scalameta" %% "contrib" % "3.7.2",

    "com.twitter" %% "finagle-http" % "19.6.0",
    // netty
    "io.netty" % "netty-all" % "4.0.36.Final",

    // armeria
    "com.linecorp.armeria" % "armeria" % "0.94.0",
    "com.linecorp.armeria" % "armeria-grpc" % "0.94.0",
    "com.linecorp.armeria" % "armeria-rxjava" % "0.94.0",
    "com.linecorp.armeria" % "armeria-thrift" % "0.94.0",

    // assertj
    "org.assertj" % "assertj-core" % "3.11.1" % "test",

    "org.scala-lang" % "scala-compiler" % scalaVersion.value
  ),
  addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
  //  addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full),
  addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)


)

resolvers += Resolver.mavenLocal


