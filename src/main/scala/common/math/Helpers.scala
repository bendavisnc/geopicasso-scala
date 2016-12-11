package common.math

object Helpers {

	def toFixed(d: Double, amt: Int): Double = {
		val k = Math.pow(10, amt)
		Math.round(d * k) / k
	}

	def applyTranslation(coord: (Double, Double), dx: Double, dy: Double ): (Double, Double) = {
		val (x, y) = coord
		(x + dx, y + dy)
	}

	def applyScale(coord: (Double, Double), s: Double): (Double, Double) = {
		val (x, y) = coord
		(x * s, y * s)
	}

}
