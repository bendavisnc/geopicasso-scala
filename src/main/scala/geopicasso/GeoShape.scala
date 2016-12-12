package geopicasso

import scalatags.Text.{svgAttrs, TypedTag}
import scalatags.Text.svgTags.{svg, rect, circle}
import scalatags.Text.implicits._
import scalatags.Text.svgAttrs.{width, height, x, y, cx, cy, r, fill, stroke}

sealed abstract class GeoShape {
	def cx: Double
	def cy: Double
	def r: Double

	def copy(cx: Double = this.cx, cy: Double = this.cy, r: Double = this.r): GeoShape

	def toSvgElem(config: Config): TypedTag[String]

}

class Circle(val cx: Double, val cy: Double, val r: Double) extends GeoShape {


	def copy(cx: Double = this.cx, cy: Double = this.cy, r: Double = this.r): Circle = {
		Circle(cx, cy, r)
	}

	override def toSvgElem(config: Config): TypedTag[String] = {
		val strokeWidth = Math.max(0.05, config.r * config.n * 0.0005)
		circle(
			svgAttrs.id := this.toString,
			svgAttrs.cx := cx,
			svgAttrs.cy := cy,
			svgAttrs.r := r,
			svgAttrs.fillOpacity := 0,
			svgAttrs.stroke := "white",
			svgAttrs.strokeWidth := strokeWidth
			)
	}

}

object Circle {

	def apply(cx: Double, cy: Double, r: Double): Circle = {
		new Circle(cx, cy, r)
	}

}
