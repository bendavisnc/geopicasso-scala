
name := "geopicasso"

version := "0.0.1"

scalaVersion := "2.11.8"


libraryDependencies ++= Seq(
	"com.lihaoyi" %% "utest" % "0.4.3" % "test",
	"org.json" % "json" % "20140107",
	"com.lihaoyi" %% "scalatags" % "0.6.1",
	"commons-io" % "commons-io" % "2.5"

)

testFrameworks += new TestFramework("utest.runner.Framework")

mainClass in (Compile, run) := Some("geopicasso.Geopicasso")

