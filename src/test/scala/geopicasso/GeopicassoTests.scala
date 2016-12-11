package geopicasso

import org.singlespaced.d3js.d3
import utest._

import scala.scalajs.js

object GeopicassoTests extends TestSuite {

	val tests = this {
		'unitShapes {
			new Geopicasso(null).unitShapes.length
		}
		'd3scale {
			d3.scale.linear()
				.domain(js.Array(0, 1))
				.range(js.Array(0, 1200))
				.apply(0.5)
		}
	}

}

