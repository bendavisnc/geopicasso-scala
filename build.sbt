enablePlugins(ScalaJSPlugin)

name := "geopicasso"

version := "0.0.1"

scalaVersion := "2.11.8"


libraryDependencies ++= Seq(
	"com.lihaoyi" %%% "utest" % "0.4.3" % "test",
	"org.singlespaced" %%% "scalajs-d3" % "0.3.3",
	"org.scala-js" %%% "scalajs-dom" % "0.9.0"
)

testFrameworks += new TestFramework("utest.runner.Framework")



