name := "the-note"

version := "1.0"
scalaVersion := "2.13.2"
//scalaOrganization in ThisBuild := "org.typelevel"

useJCenter := true

lazy val `the-note` = (project in file("."))
  .dependsOn(`code`, `macro`)
  .aggregate(`code`, `macro`)

lazy val `code` = (project in file("code"))
  .settings(commonSettings)
//  .enablePlugins(TutPlugin)
  .dependsOn(`macro`)

lazy val `macro` = (project in file("macro"))
  .settings(commonSettings)

lazy val commonSettings = Seq(
  scalaVersion := "2.13.2",
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
    "-feature",
    "-language:existentials",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-language:experimental.macros",
    "-language:reflectiveCalls",
    "-language:postfixOps",
    "-Ymacro-annotations"
  ),
  resolvers ++= Seq(
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots"),
    Resolver.jcenterRepo
  ),
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-core" % "2.1.1",
    "org.typelevel" %% "cats-effect" % "2.1.3",
    "org.typelevel" %% "cats-mtl-core" % "0.7.1",
    "io.monix" %% "monix" % "3.2.1",
    //    "org.typelevel" %% "cats" % "0.9.0",
    "com.chuusai" %% "shapeless" % "2.3.3",
    "org.tpolecat" %% "doobie-core" % "0.9.0",
    "org.tpolecat" %% "doobie-h2" % "0.9.0",
    "org.scalactic" %% "scalactic" % "3.1.1",

    "com.github.mpilquist" %% "simulacrum" % "0.19.0",
//    "com.thoughtworks.each" %% "each" % "3.3.1",
    "org.atnos" %% "eff" % "5.8.0",

    // fs2
    "co.fs2" %% "fs2-core" % "2.3.0",
    "co.fs2" %% "fs2-io" % "2.3.0",

    // freestyle
//    "io.frees" %% "frees-core" % "0.6.2",
//    // optional - effects and patterns
//    "io.frees" %% "frees-effects" % "0.6.2",
    //    "io.frees" %% "frees-tagless"      % "0.6.2",

    "com.twitter" %% "util-core" % "20.4.1",

    "eu.timepit" %% "refined" % "0.9.14",

    // reative streams
    "org.reactivestreams" % "reactive-streams" % "1.0.3",
    "org.reactivestreams" % "reactive-streams-tck" % "1.0.3",
    // rxjava
    "io.reactivex.rxjava3" % "rxjava" % "3.0.3",
    "org.twitter4j" % "twitter4j-core" % "4.0.7",
    "org.twitter4j" % "twitter4j-stream" % "4.0.7",
    "org.asynchttpclient" % "async-http-client" % "2.12.1",
    // reactor
    "io.projectreactor" % "reactor-core" % "3.3.5.RELEASE",


    // A reactive (or non-blocking, or asynchronous) JSON parser
    "de.undercouch" % "actson" % "1.2.0",
    // reactive mongo driver
    "org.mongodb" % "mongodb-driver-reactivestreams" % "4.0.2",

    // akka streams
    "com.typesafe.akka" %% "akka-stream" % "2.6.5",

    "org.scalaz" %% "scalaz-core" % "7.3.0",
    "org.scalaz" %% "scalaz-concurrent" % "7.2.30",

    "io.circe" %% "circe-core" % "0.13.0",
    "io.circe" %% "circe-generic" % "0.13.0",
    "io.circe" %% "circe-parser" % "0.13.0",
    "io.circe" %% "circe-shapes" % "0.13.0",
    "io.circe" %% "circe-generic-extras" % "0.13.0",


    "org.scalatest" %% "scalatest" % "3.1.1" % "test",
    // junit 4
    "junit" % "junit" % "4.13" % "test",
    // junit 5
    "org.awaitility" % "awaitility" % "4.0.2" % "test",

    "com.github.julien-truffaut" %% "monocle-core" % "2.0.3",
    "com.github.julien-truffaut" %% "monocle-macro" % "2.0.3",
    "com.github.julien-truffaut" %% "monocle-law" % "2.0.3" % "test",
    // scala meta
    "org.scalameta" %% "scalameta" % "4.3.10",
//    "org.scalameta" %% "contrib" % "4.3.10",

    "com.twitter" %% "finagle-http" % "20.4.1",
    // netty
    "io.netty" % "netty-all" % "4.1.49.Final",

    // armeria
    "com.linecorp.armeria" % "armeria" % "0.99.4",
    "com.linecorp.armeria" % "armeria-grpc" % "0.99.4",
    "com.linecorp.armeria" % "armeria-rxjava" % "0.99.4",
    "com.linecorp.armeria" % "armeria-thrift" % "0.99.4",
    "com.linecorp.armeria" % "armeria-brave" % "0.99.4",

    // assertj
    "org.assertj" % "assertj-core" % "3.15.0" % "test",

    "org.scala-lang" % "scala-compiler" % scalaVersion.value
  ),
  addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
  //  addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full),
//  addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)


)

resolvers += Resolver.mavenLocal


