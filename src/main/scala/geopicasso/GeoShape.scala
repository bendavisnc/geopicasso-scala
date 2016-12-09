package geopicasso

sealed abstract class GeoShape {
	def cx: Double
	def cy: Double
	def r: Double
//	def toSVGElement:
}

case class Circle(cx: Double, cy: Double, r: Double) extends GeoShape {
}

