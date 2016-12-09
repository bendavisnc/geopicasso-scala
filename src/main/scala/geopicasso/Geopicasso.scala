package geopicasso

class Geopicasso(configPath: String) {

	private val config = Config.fromJson(configPath)

	private val littleR = config.r / config.n

	private def nextShape(previousShape: GeoShape): GeoShape = {
		val makeBigger = previousShape.cx + previousShape.r > 1
		lazy val biggerR = (previousShape.r / littleR + 1) * littleR
		previousShape match {
			case circle: Circle => {
				if (makeBigger) {
					circle.copy(cx = biggerR, r = biggerR)
				} else circle.copy(cx = circle.cx + circle.r * 2)
			}
			case _ => null // todo
		}
	}

	def unitShapes: Stream[GeoShape] = {
		val firstShape = Circle(cx = littleR, cy = 0.5, r = littleR)
		val lastShape = Circle(cx = config.r, cy = 0.5, r = config.r)
		def recur(lastShape: GeoShape): Stream[GeoShape] = {
			val ns = nextShape(lastShape)
			if (ns.r >= lastShape.r) Stream.empty else recur(ns)

		}
		firstShape #:: recur(firstShape)
	}

}
