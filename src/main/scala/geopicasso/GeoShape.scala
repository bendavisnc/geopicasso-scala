package geopicasso

import scalatags.Text.{svgAttrs, TypedTag}
import scalatags.Text.svgTags.{svg, rect, circle}
import scalatags.Text.implicits._
import scalatags.Text.svgAttrs.{width, height, x, y, cx, cy, r, fill, stroke}
import common.math.Helpers.toFixed

//case class GeoShape(model: ShapeModel, convert: (ShapeModel, Config) => TypedTag[String]) {
//	def cx: Double = model.cx
//	def cy: Double = model.cy
//	def r: Double = model.r
//}

case class ShapeModel(cx: Double, cy: Double, r: Double)

case class FillStyle(color: String, opacity: Double)

case class StrokeStyle(color: String, opacity: Double, width: Double)

object ShapeModel {

	def toSvgElem(shape: ShapeModel, fillStyle: FillStyle, strokeStyle: StrokeStyle, config: Config): TypedTag[String] = {
		val precisionAmt = 4
		val strokeWidth = Math.max(0.05, config.r * config.n * 0.0005)
		circle(
			svgAttrs.cx := toFixed(shape.cx, precisionAmt),
			svgAttrs.cy := toFixed(shape.cy, precisionAmt),
			svgAttrs.r := toFixed(shape.r, precisionAmt),
			svgAttrs.fillOpacity := fillStyle.opacity,
			svgAttrs.fill := fillStyle.color,
			svgAttrs.stroke := strokeStyle.color,
			svgAttrs.strokeWidth := strokeStyle.width,
			svgAttrs.strokeOpacity := strokeStyle.opacity
		)
	}
}

//object Circle {
//
//	def shapeModel(cx: Double, cy: Double, r: Double): ShapeModel = {
//		ShapeModel(cx, cy, r)
//	}
//
//	def toSvgElem(circleShape: ShapeModel, config: Config): TypedTag[String] = {
//		val strokeWidth = Math.max(0.05, config.r * config.n * 0.0005)
//		circle(
//			svgAttrs.cx := circleShape.cx,
//			svgAttrs.cx := circleShape.cx,
//			svgAttrs.cy := circleShape.cy,
//			svgAttrs.r := circleShape.r,
//			svgAttrs.fillOpacity := 0,
//			svgAttrs.stroke := "white",
//			svgAttrs.strokeWidth := strokeWidth
//		)
//	}
//
//}
//sealed abstract class GeoShape {
//	def cx: Double
//	def cy: Double
//	def r: Double
//
//	def copy(cx: Double = this.cx, cy: Double = this.cy, r: Double = this.r): GeoShape
//
//	def toSvgElem(config: Config): TypedTag[String]
//
//}
//
//class Circle(val cx: Double, val cy: Double, val r: Double) extends GeoShape {
//
//
//	def copy(cx: Double = this.cx, cy: Double = this.cy, r: Double = this.r): Circle = {
//		Circle(cx, cy, r)
//	}
//
//	override def toSvgElem(config: Config): TypedTag[String] = {
//		val strokeWidth = Math.max(0.05, config.r * config.n * 0.0005)
//		circle(
////			svgAttrs.id := this.toString,
//			svgAttrs.cx := cx,
//			svgAttrs.cy := cy,
//			svgAttrs.r := r,
//			svgAttrs.fillOpacity := 0,
//			svgAttrs.stroke := "white",
//			svgAttrs.strokeWidth := strokeWidth
//			)
//	}
//
//}
//
//object Circle {
//	def apply(cx: Double, cy: Double, r: Double): Circle = {
//		new Circle(cx, cy, r)
//	}
//}
