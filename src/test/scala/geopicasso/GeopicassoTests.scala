package geopicasso

import utest._
import scalatags.Text.TypedTag
import scalatags.Text.svgTags.{svg, rect}
import scalatags.Text.tags.{div, body, h1, h2, p}
import scalatags.Text.svgAttrs.{width, height, x, y, fill, stroke}
import scalatags.Text.short._


object GeopicassoTests extends TestSuite {

	val tests = this {
		'whatevs {
			svg(height := "800", width := "500")(
			)
		}
		'whatevs2 {
			val posts = Seq(
				("alice", "i like pie"),
				("bob", "pie is evil i hate you"),
				("charlie", "i like pie and pie is evil, i hat myself")
			)
			body(
				h1("This is my title"),
				div("posts"),
				for ((name, text) <- posts) yield div(
					h2("Post by ", name),
					p(text)
				)
			)
		}
	}

}

