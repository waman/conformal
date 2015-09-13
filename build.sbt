name := "conformal"

version := "0.1"

organization := "org.waman.conformal"

scalaVersion := "2.11.6"


//***** Custom settings *****
val javaVersion = settingKey[String]("javac source/target version")

val encoding = settingKey[String]("source encoding")

javaVersion := "1.8"

encoding := "UTF-8"

//***** Options & Dependencies *****
javacOptions ++= Seq(
  "-source", javaVersion.value,
  "-target", javaVersion.value,
  "-encoding", encoding.value
)

scalacOptions ++= Seq(
  "-Xlint",
  "-deprecation",
  "-unchecked",
  "-feature",
  "-encoding", encoding.value
)

libraryDependencies ++= Seq(
  "org.spire-math" % "spire_2.11" % "0.10.1",
  "org.apache.commons" % "commons-math3" % "3.5" % "test",
  //"org.scala-lang" % "scala-library" % scalaVersion.value,
  //"org.scala-lang" % "scala-swing" % "2.9.2",
  "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"
)

fork in Global := true

crossPaths := false
