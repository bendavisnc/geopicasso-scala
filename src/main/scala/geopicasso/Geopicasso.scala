package geopicasso

import java.io.{FileOutputStream, ByteArrayInputStream, File}
import java.nio.charset.Charset

import org.apache.batik.transcoder.{TranscoderOutput, TranscoderInput}
import org.apache.batik.transcoder.image.PNGTranscoder
import org.apache.commons.io.FileUtils

import scala.annotation.tailrec
import common.math.Helpers.toFixed
import scalatags.Text.TypedTag
import scalatags.Text.svgTags.{svg, rect}
import scalatags.Text.short._
import scalatags.Text.svgAttrs.{width, height, x, y, fill, stroke, xmlns}

class Geopicasso(config: Config) {

	def firstAndLastShapes: (ShapeModel, ShapeModel) = {
		val littleR = 0.5 / config.n
		(ShapeModel(cx = littleR, cy = 0.5, r = littleR), ShapeModel(cx = 0.5, cy = 0.5, r = 0.5))
	}

	private def nextShape(previousShape: ShapeModel): ShapeModel = {
		val (firstShape, lastShape) = firstAndLastShapes
		val makeBigger = toFixed(previousShape.cx + 3 * previousShape.r, 4) > toFixed(lastShape.cx + lastShape.r, 4)
		if (makeBigger) {
			val biggerR = (previousShape.r / firstShape.r + 1) * firstShape.r
			previousShape.copy(cx = biggerR, r = biggerR)
		}
		else previousShape.copy(cx = previousShape.cx + previousShape.r * 2)
	}

	def unitShapes: List[ShapeModel] = {
		val (firstShape, lastShape) = firstAndLastShapes
		@tailrec
		def recur(previousShape: ShapeModel, acc: List[ShapeModel]): List[ShapeModel] = {
			val ns = nextShape(previousShape)
			if (ns.r >= lastShape.r) ns :: acc else recur(ns, previousShape :: acc)
		}
		recur(firstShape, Nil)
	}

	def fillStyles: Stream[FillStyle] = {
		val fills = config.fills.map(
			cData => FillStyle(cData._1, cData._2)
		)
		Stream.continually(fills).flatten
	}

	def strokeStyles: Stream[StrokeStyle] = {
		val strokes = config.strokes.map(
			cData => StrokeStyle(cData._1, cData._2, cData._3)
		)
		Stream.continually(strokes).flatten
	}

	def projectShape(shape: ShapeModel): ShapeModel = {
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

	def readyToDrawShapes: List[TypedTag[String]] = {
		val shapeModels =
			unitShapes
				.map(projectShape).toStream
		val readyToDraw: Stream[TypedTag[String]] =
			(shapeModels, fillStyles,strokeStyles).zipped.map(
				{
					case (shapeModel: ShapeModel, fillStyle: FillStyle, strokeStyle: StrokeStyle) => {
						ShapeModel.toSvgElem(shapeModel, fillStyle, strokeStyle, config)
					}
				}
			)
		readyToDraw.toList
	}

	private def createSvgDoc(childContent: List[TypedTag[String]]): TypedTag[String] = {
		svg(xmlns := "http://www.w3.org/2000/svg", width := config.xRes, height := config.yRes)(
			rect(x := 0, y := 0, width := config.xRes, height := config.yRes, fill := config.bg),
			childContent
		)
	}

	def spitPng(svgDoc: TypedTag[String], config: Config): Unit = {
		val pngConverter = new PNGTranscoder()
		val svgInput: TranscoderInput = new TranscoderInput(new ByteArrayInputStream(svgDoc.toString.getBytes))
		val svgOutFile = new FileOutputStream("renders/" + config.name + ".png")
		val svgOut = new TranscoderOutput(svgOutFile)
		pngConverter.transcode(svgInput, svgOut)
		svgOutFile.flush()
		svgOutFile.close()
	}

	def spitSvg(svgDoc: TypedTag[String], config: Config): Unit = {
		FileUtils.write(
			new File("renders/" + config.name + ".svg"),
			svgDoc.toString,
			Charset.defaultCharset())
	}


	def go(): Unit = {
		val svgDoc = createSvgDoc(readyToDrawShapes)
		spitSvg(svgDoc, config)
		spitPng(svgDoc, config)
	}

}

object Geopicasso {

	def main(args: Array[String]): Unit = {
		new Geopicasso(
			args.headOption.map(Config.from).getOrElse(Config.default))
			.go()

	}
}
