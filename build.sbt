name := "conformal"

version := "0.1"

organization := "org.waman.conformal"

scalaVersion := "2.10.0"

libraryDependencies ++= Seq(
  "org.spire-math" %% "spire" % "0.3.0",
  "org.apache.commons" % "commons-math3" % "3.0" % "test",
  "junit" % "junit" % "4.10" % "test",
  "org.scala-lang" % "scala-swing" % "2.10.0" % "test",
  "org.jfree" % "jfreechart" % "1.0.14" % "test",
  "com.github.insubstantial" % "substance" % "7.2" % "test"
)

javacOptions ++= Seq("-source", "1.7", "-target", "1.7", "-encoding", "UTF-8")

scalacOptions ++= Seq("-deprecation", "-encoding", "UTF-8")

fork := true

crossPaths := false