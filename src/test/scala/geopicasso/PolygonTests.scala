package geopicasso

import utest._
import geopicasso.util.Polygon.{unitPolygonPoints, pointsSerialized}
import common.math.Helpers.toFixed


object PolygonTests extends TestSuite {

//	(testing "unit-polygon-points"
//	(is
//		(=
//		(trim-all-x-places (unit-polygon-points 3) 3)
//	[[0.933 0.75] [0.067 0.75] [0.5 0.0]]
//	)))
//
//	(testing "points-serializer"
//	(is
//		(=
//		(points-serialized [[0 1] [2, 3]])
//	"0,1 2,3")))

	def trimAllXPlaces(all: List[(Double, Double)], xAmt: Int): List[(Double, Double)] = {
		all.map(
			(c) => {
				val (x, y) = c
				(
					toFixed(x, xAmt),
					toFixed(y, xAmt)
				)
			}
		)
	}

	val tests = this {

		'unitPolygonPoints {
			assert(
				trimAllXPlaces(unitPolygonPoints(3), 3) == (0.933, 0.75) :: (0.067, 0.75) :: (0.5, 0.0) :: Nil
			)
		}

		'pointsSerialized {
			assert(
				pointsSerialized((0.0, 1.0) :: (2.0, 3.0) :: Nil) == "0.0,1.0 2.0,3.0 "
			)
		}

	}

}
