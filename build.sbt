name := "geopicasso"

version := "0.0.1"

scalaVersion := "2.11.8"


libraryDependencies ++= Seq(
  "org.specs2" %% "specs2-core" % "3.8.3" % "test",
	"org.json" % "json" % "20140107",
	"org.apache.xmlgraphics" % "batik-svg-dom" % "1.8"

)

scalacOptions in Test ++= Seq("-Yrangepos")

// Read here for optional dependencies:
// https://etorreborre.github.io/specs2/guide/SPECS2-3.8.3/org.specs2.guide.Installation.html#other-dependencies

resolvers ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo)

