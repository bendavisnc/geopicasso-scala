package geopicasso

import org.scalajs.dom
import org.singlespaced.d3js.{Selection, d3}

sealed abstract class GeoShape {
	def cx: Double
	def cy: Double
	def r: Double

	def copy(cx: Double = this.cx, cy: Double = this.cy, r: Double = this.r): GeoShape

	def addToDoc(doc: Selection[dom.EventTarget]): Selection[dom.EventTarget]
}

class Circle(val cx: Double, val cy: Double, val r: Double) extends GeoShape {

	override def addToDoc(doc: Selection[dom.EventTarget]): Selection[dom.EventTarget] = {
		println(this)
		doc
			.append("circle")
			.attr("id", this.toString)
			.attr("cx", cx)
			.attr("cy", cy)
			.attr("r", r)
			.style("stroke", "white") // todo
			.style("stroke-width", "0.2") // todo
			.style("fill-opacity", 0) // todo
	}

	def copy(cx: Double = this.cx, cy: Double = this.cy, r: Double = this.r): Circle = {
		Circle(cx, cy, r)
	}

}

object Circle {

	def apply(cx: Double, cy: Double, r: Double): Circle = {
		new Circle(cx, cy, r)
	}

}
