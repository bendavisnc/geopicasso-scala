package geopicasso

import java.io.{FileOutputStream, ByteArrayInputStream, File}
import java.nio.charset.Charset

import org.apache.batik.transcoder.{TranscoderOutput, TranscoderInput}
import org.apache.batik.transcoder.image.PNGTranscoder
import org.apache.commons.io.FileUtils

import scala.annotation.tailrec
import common.math.Helpers._
import scalatags.Text.TypedTag
import scalatags.Text.svgTags.{svg, rect}
import scalatags.Text.short._
import scalatags.Text.svgAttrs.{width, height, x, y, fill, stroke, xmlns}

class Geopicasso(config: Config) {

	def firstAndLastShapes: (GeoShape, GeoShape) = {
		val littleR = 0.5 / config.n
		(Circle(cx = littleR, cy = 0.5, r = littleR), Circle(cx = 0.5, cy = 0.5, r = 0.5))
	}

	private def nextShape(previousShape: GeoShape): GeoShape = {
		val (firstShape, lastShape) = firstAndLastShapes
		val makeBigger = toFixed(previousShape.cx + 3 * previousShape.r, 4) > toFixed(lastShape.cx + lastShape.r, 4)
		if (makeBigger) {
			val biggerR = (previousShape.r / firstShape.r + 1) * firstShape.r
			previousShape.copy(cx = biggerR, r = biggerR)
		}
		else previousShape.copy(cx = previousShape.cx + previousShape.r * 2)
	}

	def unitShapes: List[GeoShape] = {
		val (firstShape, lastShape) = firstAndLastShapes
		@tailrec
		def recur(previousShape: GeoShape, acc: List[GeoShape]): List[GeoShape] = {
			val ns = nextShape(previousShape)
			if (ns.r >= lastShape.r) ns :: acc else recur(ns, previousShape :: acc)
		}
		recur(firstShape, Nil)
	}

	def projectShape(shape: GeoShape): GeoShape = {
			val configUnitScale = (d: Double) => config.r / 0.5 * d
			val configUnitXMove = (d: Double) => config.cx - configUnitScale(0.5) + d
			val configUnitYMove = (d: Double) => config.cy - configUnitScale(0.5) + d
			val unitToProjectedXScale = (d: Double) => config.xRes * d
			val unitToProjectedYScale  = (d: Double) => config.yRes * d
			val xTransform = configUnitScale andThen configUnitXMove andThen unitToProjectedXScale
			val yTransform = configUnitScale andThen configUnitYMove andThen unitToProjectedYScale
			val rTransform = configUnitScale andThen unitToProjectedXScale
			shape.copy(
				cx = xTransform(shape.cx),
				cy = yTransform(shape.cy),
				r = rTransform(shape.r)
			)
	}

	private def createSvgDoc(childContent: List[TypedTag[String]]): TypedTag[String] = {
		svg(xmlns := "http://www.w3.org/2000/svg", width := config.xRes, height := config.yRes)(
			rect(x := 0, y := 0, width := config.xRes, height := config.yRes, fill := config.bg),
			childContent
		)
	}

	def svgDocToPng(svgDoc: TypedTag[String], config: Config): Unit = {
		val pngConverter = new PNGTranscoder()
		val svgInput: TranscoderInput = new TranscoderInput(new ByteArrayInputStream(svgDoc.toString.getBytes))
		val svgOutFile = new FileOutputStream("renders/" + config.name + ".png")
		val svgOut = new TranscoderOutput(svgOutFile)
		pngConverter.transcode(svgInput, svgOut)
		svgOutFile.flush()
		svgOutFile.close()
	}

	def go(): Unit = {
		val shapes: List[TypedTag[String]] = unitShapes
			.map(projectShape)
			.map(_.toSvgElem(config))

		svgDocToPng(createSvgDoc(shapes), config)
	}

}

object Geopicasso {

	def main(args: Array[String]): Unit = {
		new Geopicasso(
			args.headOption.map(Config.from).getOrElse(Config.default))
			.go()

	}
}
