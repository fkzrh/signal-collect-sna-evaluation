import AssemblyKeys._ 
assemblySettings

name := "SignalCollectSNA-EvaluationSuite"
 
version := "0.1-SNAPSHOT"
 
organization := "com.signalcollect"
 
scalaVersion := "2.11.2"

scalacOptions ++= Seq("-optimize", "-Yinline-warnings", "-feature", "-deprecation", "-Xelide-below", "INFO" , "-target:jvm-1.7")

parallelExecution in Test := false

EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource

EclipseKeys.withSource := true

/** Dependencies */
libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-library" % "2.11.2" % "compile",
  "junit" % "junit" % "4.10" % "test",
  "org.jfree" % "jfreechart" % "1.0.14" % "compile"
  )