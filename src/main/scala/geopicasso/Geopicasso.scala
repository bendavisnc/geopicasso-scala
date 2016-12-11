package geopicasso

import org.scalajs.dom
import org.singlespaced.d3js.{d3, Selection}
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport
import common.math.Helpers._

@JSExport
class Geopicasso(configPath: String) {

//	private val config = Config.fromJson(configPath)
	private val config = Config.testConfig

	def firstAndLastShapes: (GeoShape, GeoShape) = {
		val littleR = config.r / config.n
		(Circle(cx = littleR, cy = 0.5, r = littleR), Circle(cx = 0.5, cy = 0.5, r = 0.5))
	}

	private def nextShape(previousShape: GeoShape): GeoShape = {
		val (firstShape, lastShape) = firstAndLastShapes
		val makeBigger = toFixed(previousShape.cx + 3 * previousShape.r, 4) > toFixed(lastShape.cx + lastShape.r, 4)
		lazy val biggerR = (previousShape.r / firstShape.r + 1) * firstShape.r
		if (makeBigger) {
				previousShape.copy(cx = biggerR, r = biggerR)
		} else previousShape.copy(cx = previousShape.cx + previousShape.r * 2)
	}

	def unitShapes: List[GeoShape] = {
		val (firstShape, lastShape) = firstAndLastShapes
		def recur(previousShape: GeoShape): List[GeoShape] = {
			val ns = nextShape(previousShape)
			if (ns.r >= lastShape.r) ns :: Nil else previousShape :: recur(ns)
		}
		recur(firstShape)
	}

//	def projectShape(shape: GeoShape): GeoShape = {
//		val xInterp = d3.scale.linear()
//			.domain(js.Array(0, 1))
//			.range(js.Array(0, config.xRes))
//		val yInterp = d3.scale.linear()
//			.domain(js.Array(0, 1))
//			.range(js.Array(0, config.yRes))
//		shape.copy(
//			cx = xInterp(shape.cx),
//			cy = yInterp(shape.cy),
//			r = xInterp(shape.r)
//		)
//	}

	def projectShape(shape: GeoShape): GeoShape = {
			val sF = (d: Double) => config.r / 0.5 * d
			val mxF = (d: Double) => config.cx - sF(0.5) + d
			val myF = (d: Double) => config.cy - sF(0.5) + d
			val sxF = (d: Double) => config.xRes * d
			val syF = (d: Double) => config.yRes * d
			val pcx = sxF(mxF(sF(shape.cx)))
			val pcy = syF(myF(sF(shape.cy)))
			val pr = sxF(sF(shape.r))
			shape.copy(pcx, pcy, pr)
	}


	private def createSvg: Selection[dom.EventTarget] = {
		d3.select("#container")
			.append("svg")
			.attr("width", config.xRes + "px")
			.attr("height", config.yRes + "px")
			.style("background-color", "black") // todo
	}


	@JSExport
	def go(): Unit = {
		val svgDoc = createSvg
		unitShapes
			.map(projectShape)
			.map(_.addToDoc(svgDoc))
	}

}
