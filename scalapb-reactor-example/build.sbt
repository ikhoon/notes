name := "scalapb-reactor-example"

version := "0.1"

scalaVersion := "2.11.12"

libraryDependencies ++= Seq(
  "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % "0.9.8",
  "io.grpc" % "grpc-core" % scalapb.compiler.Version.grpcJavaVersion
)

PB.targets in Compile := Seq(
  scalapb.gen() -> (sourceManaged in Compile).value / "scalapb",
  scalapb.reactor.ReactorCodeGenerator -> (sourceManaged in Compile).value,
)
