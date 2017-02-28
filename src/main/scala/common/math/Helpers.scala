package common.math

object Helpers {

	def toFixed(d: Double, amt: Int): Double = {
		val k = Math.pow(10, amt)
		Math.round(d * k) / k
	}

	def applyRotation(coord: (Double, Double), degrees: Double): (Double, Double) = {
		val theta = toRadians(degrees)
		val (x, y) = coord
		val rx = (x * Math.cos(theta)) + (y * Math.sin(theta))
		val ry = (x * Math.sin(theta) * -1) + (y * Math.cos(theta))
		(rx, ry)
	}

	def performRotation(coord: (Double, Double), degrees: Double, pivotCoord: (Double, Double)): (Double, Double) = {
		(applyTranslation(_: (Double, Double), pivotCoord._1 * -1, pivotCoord._2 * -1)) andThen
			(applyRotation(_: (Double, Double), degrees)) andThen
			(applyTranslation(_: (Double, Double), pivotCoord._1, pivotCoord._2)) apply coord
	}
	def toRadians(degrees: Double): Double = {
		degrees * (Math.PI / 180)
	}

	def applyTranslation(coord: (Double, Double), dx: Double, dy: Double ): (Double, Double) = {
		val (x, y) = coord
		(x + dx, y + dy)
	}

	def applyScale(coord: (Double, Double), s: Double): (Double, Double) = {
		val (x, y) = coord
		(x * s, y * s)
	}

	def diff(c1: (Double, Double), c2: (Double, Double)) = {
		val (x1, y1) = c1
		val (x2, y2) = c2
		(x1 - x2, y1 - y2)
	}

}
