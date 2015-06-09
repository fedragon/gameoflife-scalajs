import com.lihaoyi.workbench.Plugin._

enablePlugins(ScalaJSPlugin)

workbenchSettings

name := "gameoflife"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.5"

scalaJSStage in Global := FastOptStage

testFrameworks += new TestFramework("utest.runner.Framework")

bootSnippet := "com.github.fedragon.gameoflife.GameOfLife().main(document.getElementById('canvas'));"

updateBrowsers <<= updateBrowsers.triggeredBy(fastOptJS in Compile)

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.8.0",
  "com.lihaoyi"  %%% "utest" % "0.3.1"
)
