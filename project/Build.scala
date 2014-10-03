import sbt._
import Keys._

object SNABuild extends Build {
  lazy val scCore = ProjectRef(file("../signal-collect"), id = "signal-collect")
  lazy val scSNA = ProjectRef(file("../SignalCollectSNA"), id = "signal-collect-sna")
  val scSignalCollectSNAEvaluation = Project(id = "signal-collect-sna-evaluation",
    base = file(".")) dependsOn (scCore, scSNA)
}