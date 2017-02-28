package geopicasso

import scalatags.Text.{svgAttrs}
import scalatags.Text.svgTags.{svg, rect, circle, polygon}
import scalatags.Text.implicits._
import scalatags.Text.svgAttrs.{width, height, x, y, cx, cy, r, fill, stroke}
import common.math.Helpers.toFixed
import geopicasso.util.Polygon.points
import geopicasso.Geopicasso.SVGElement


case class ShapeModel(cx: Double, cy: Double, r: Double)

case class FillStyle(color: String, opacity: Double)

case class StrokeStyle(color: String, opacity: Double, width: Double)

object ShapeModel {

	def toSvgElem(i: Int, shape: ShapeModel, fillStyle: FillStyle, strokeStyle: StrokeStyle, sidesInd: Int): SVGElement = {
		val precisionAmt = 4
		sidesInd match {
			case 0 => {
				circle(
					svgAttrs.id := "element" + i,
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
			case n if (n > 0) => {
				polygon(
					svgAttrs.id := "element" + i,
					svgAttrs.points := points(shape, sidesInd),
					svgAttrs.fillOpacity := fillStyle.opacity,
					svgAttrs.fill := fillStyle.color,
					svgAttrs.stroke := strokeStyle.color,
					svgAttrs.strokeWidth := strokeStyle.width,
					svgAttrs.strokeOpacity := strokeStyle.opacity
				)
			}
			case _ => throw new RuntimeException("Invalid number of sides.")
		}
	}
}

