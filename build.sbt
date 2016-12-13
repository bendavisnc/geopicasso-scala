
name := "geopicasso"

version := "0.0.1"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
	"com.lihaoyi" %% "utest" % "0.4.3" % "test",
	"org.json" % "json" % "20140107",
	"com.lihaoyi" %% "scalatags" % "0.6.1",
	"commons-io" % "commons-io" % "2.5",
	"org.apache.xmlgraphics" % "batik-svggen" % "1.7",
	"org.apache.xmlgraphics" % "batik-transcoder" % "1.7",
	"org.apache.xmlgraphics" % "batik-codec" % "1.7",
	"us.bpsm" % "edn-java" % "0.5.0"

)

testFrameworks += new TestFramework("utest.runner.Framework")

mainClass in (Compile, run) := Some("geopicasso.Geopicasso")




