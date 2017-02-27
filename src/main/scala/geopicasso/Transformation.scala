package geopicasso

import common.math.Helpers.{applyRotation, performRotation}

object Transformation {

	def rotation(degrees: Double) = {
		(shapeModel: ShapeModel) => {
//			val (newCx, newCy) = applyRotation((shapeModel.cx, shapeModel.cy), degrees)
			val (newCx, newCy) = performRotation((shapeModel.cx, shapeModel.cy), degrees, (0.5, 0.5))
			shapeModel.copy(cx= newCx, cy = newCy)
		}
	}

	trait Trans[A] {
		def f: A => ShapeModel => ShapeModel
		def withParameter(a: Any): ShapeModel => ShapeModel = {
			f(a.asInstanceOf[A])
		}
	}


//	def from(string: String): ShapeModel => ShapeModel = {
	def from(string: String): Trans[_] = {
		string match {
			case "rotate" => {
				new Trans[Double] {
					def f = rotation
				}
			}
			case _ => throw new Exception("Unsupported transformation.")
		}
	}


}
