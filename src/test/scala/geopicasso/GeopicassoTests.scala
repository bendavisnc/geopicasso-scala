package geopicasso

import utest._
import scalatags.Text.TypedTag
import scalatags.Text.svgTags.{svg, rect}
import scalatags.Text.tags.{div, body, h1, h2, p}
import scalatags.Text.svgAttrs.{width, height, x, y, fill, stroke}
import scalatags.Text.short._


object GeopicassoTests extends TestSuite {

	val tests = this {
		'unitShapes_length {
			new Geopicasso(Config.from("minimalist2.edn")).unitShapes.length
		}
		'readyShapes_length {
			new Geopicasso(Config.from("minimalist2.edn")).readyToDrawShapes.length
		}
	}

}

