name := "conformal"

version := "0.1"

organization := "org.waman.conformal"

scalaVersion := "2.11.7"


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
  "org.spire-math" % "spire_2.11" % "0.11.0",
  "org.apache.commons" % "commons-math3" % "3.5" % "test",
  "org.waman" % "scalatest-util" % "0.4" % "test"
)

fork in Global := true

crossPaths := false
