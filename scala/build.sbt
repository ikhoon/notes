import sbtprotoc.ProtocPlugin.autoImport.PB

name := "the-note"

version := "1.0"
scalaVersion := "2.13.2"
//scalaOrganization in ThisBuild := "org.typelevel"

lazy val `the-note` = (project in file("."))
  .dependsOn(`code`, `macro`)
  .aggregate(`code`, `macro`)

lazy val `code` = (project in file("code"))
  .settings(commonSettings)
  .dependsOn(`macro`)

lazy val `macro` = (project in file("macro"))
  .settings(commonSettings)

lazy val commonSettings = Seq(
  scalaVersion := "2.13.4",
  //  scalaOrganization in ThisBuild := "org.typelevel",
  //  scalacOptions += "-Xfatal-warnings",
  scalacOptions ++= Seq(
    // See other posts in the series for other helpful options
    "-target:jvm-1.8",
    "-encoding",
    "UTF-8",
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
    "org.typelevel" %% "cats-core" % "2.7.0",
    "org.typelevel" %% "cats-effect" % "3.3.2",
    "org.typelevel" %% "cats-mtl-core" % "0.7.1",
//    "io.monix" %% "monix" % "3.4.0",
    //    "org.typelevel" %% "cats" % "0.9.0",
    "com.chuusai" %% "shapeless" % "2.3.7",
    "org.tpolecat" %% "doobie-core" % "1.0.0-RC1",
    "org.tpolecat" %% "doobie-h2" % "1.0.0-RC1",
    "org.scalactic" %% "scalactic" % "3.2.9",
    "com.github.mpilquist" %% "simulacrum" % "0.19.0",
    "org.atnos" %% "eff" % "5.22.0",
    // fs2
    "co.fs2" %% "fs2-core" % "3.2.4",
    "co.fs2" %% "fs2-io" % "3.2.4",
    "com.twitter" %% "util-core" % "21.12.0",
    "eu.timepit" %% "refined" % "0.9.28",
    // reative streams
    "org.reactivestreams" % "reactive-streams" % "1.0.3",
    "org.reactivestreams" % "reactive-streams-tck" % "1.0.3",
    // rxjava
    "io.reactivex.rxjava3" % "rxjava" % "3.1.3",
    "org.twitter4j" % "twitter4j-core" % "4.0.7",
    "org.twitter4j" % "twitter4j-stream" % "4.0.7",
    "org.asynchttpclient" % "async-http-client" % "2.12.3",
    // reactor
    "io.projectreactor" % "reactor-core" % "3.4.13",
    // A reactive (or non-blocking, or asynchronous) JSON parser
    "de.undercouch" % "actson" % "1.2.0",
    // reactive mongo driver
    "org.mongodb" % "mongodb-driver-reactivestreams" % "4.4.0",
    // akka streams
    "com.typesafe.akka" %% "akka-stream" % "2.6.18",
    "org.scalaz" %% "scalaz-core" % "7.3.5",
    "org.scalaz" %% "scalaz-concurrent" % "7.2.33",
    "io.circe" %% "circe-core" % "0.14.1",
    "io.circe" %% "circe-generic" % "0.14.1",
    "io.circe" %% "circe-parser" % "0.14.1",
    "io.circe" %% "circe-shapes" % "0.14.1",
    "io.circe" %% "circe-generic-extras" % "0.14.1",
    "org.scalatest" %% "scalatest" % "3.2.9" % "test",
    "org.scalatestplus" %% "testng-6-7" % "3.2.9.0" % "test",
    "org.scalatestplus" %% "scalacheck-1-14" % "3.2.2.0" % "test",
    // junit 4
    "junit" % "junit" % "4.13.2" % "test",
    // junit 5
    "org.awaitility" % "awaitility" % "4.1.1" % "test",
    "com.github.julien-truffaut" %% "monocle-core" % "2.1.0",
    "com.github.julien-truffaut" %% "monocle-macro" % "2.1.0",
    "com.github.julien-truffaut" %% "monocle-law" % "2.1.0" % "test",
    // scala meta
    "org.scalameta" %% "scalameta" % "4.4.31",
    "com.twitter" %% "finagle-http" % "21.12.0",
    // netty
    "io.netty" % "netty-all" % "4.1.72.Final",
    // armeria
    "com.linecorp.armeria" % "armeria" % "1.13.4",
    "com.linecorp.armeria" % "armeria-grpc" % "1.13.4",
    "com.linecorp.armeria" % "armeria-rxjava3" % "1.13.4",
    "com.linecorp.armeria" % "armeria-brave" % "1.13.4",
    "com.linecorp.armeria" %% "armeria-scalapb" % "1.13.4",
    // Reactor Scala extension
    "io.projectreactor" %% "reactor-scala-extensions" % "0.8.0",
    // logback
    "ch.qos.logback" % "logback-classic" % "1.2.10",
    // grpc
    "io.grpc" % "grpc-netty" % scalapb.compiler.Version.grpcJavaVersion,
    "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf",
    "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion,
    // tapir
    "com.softwaremill.sttp.client3" %% "core" % "3.3.18",
    // tapir
    "com.softwaremill.sttp.tapir" %% "tapir-core" % "0.19.3",
    // assertj
    "org.assertj" % "assertj-core" % "3.21.0" % "test",
    // ZIO
    "dev.zio" %% "zio" % "1.0.12",
    "dev.zio" %% "zio-streams" % "1.0.12",
    "org.scala-lang" % "scala-compiler" % scalaVersion.value
  ),
  addCompilerPlugin(("org.typelevel" %% "kind-projector" % "0.13.2").cross(CrossVersion.full)),
  Compile / PB.targets := Seq(
    scalapb.gen() -> (Compile / sourceManaged).value
  ),
  Compile / PB.protoSources += baseDirectory.value / "src/main/proto",
)

resolvers += Resolver.mavenLocal
