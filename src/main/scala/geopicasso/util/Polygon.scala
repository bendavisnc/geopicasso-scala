package geopicasso.util

import common.math.Helpers.{performRotation, applyScale, diff, applyTranslation}
import geopicasso.ShapeModel

import scala.annotation.tailrec

object Polygon {

	/**
	 * Given a number of sides ind n, return a list of points that represent a polygon.
	 */
	def unitPolygonPoints(n: Int): List[(Double, Double)] = {
		val firstPoint = (0.5, 0.0)
		val theta = 360.0 / n
		def nextPoint(previousPoint: (Double, Double)) = {
			performRotation(previousPoint, theta, (0.5, 0.5))
		}
		@tailrec
		def recur(points: List[(Double, Double)]): List[(Double, Double)] = {
			if (points.length == n) {
				points
			}
			else {
				recur(nextPoint(points.head) :: points)
			}
		}
		recur(firstPoint :: Nil)
	}

	/**
	 * Given a list of points, return a string in the format for the svg polygon points attribute value.
	 */
	def pointsSerialized(points: List[(Double, Double)]): String = {
//		points.reduceLeft(
		points.foldLeft("")(
			(acc: String, nextPoint: (Double, Double)) => {
				val (x, y) = nextPoint
				acc + x + "," + y + " "
			}
		)
	}


	/**
	 * Return a string that represents the points for a polygon corresponding to the shapemodel and side ind."
	 */
	def points(shape: ShapeModel, sidesInd: Int): String = {
		val scaleAmt = shape.r / 0.5
		val (dx, dy) = diff((shape.cx, shape.cy), applyScale((0.5, 0.5), scaleAmt))
		pointsSerialized(
			unitPolygonPoints(sidesInd).map(
				(unitPoint) => {
					applyTranslation(
						applyScale(unitPoint, scaleAmt),
						dx, dy
					)
				}
			)
		)
	}
}
