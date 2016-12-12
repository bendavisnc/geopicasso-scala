package geopicasso

import org.json.JSONObject


/**
 * @param name The name of the rendered result (filename).
 * @param cx The x coord of the largest circle.
 * @param cy The y coord of the largest circle.
 * @param r The radius of the largest circle.
 * @param n The number of circles along the largest circle's diameter.
 * @param bg The background color.
 * @param xRes The x resolution.
 * @param yRes The y resolution.
 */
case class Config(
	name: String,
	cx: Double,
	cy: Double,
	r: Double,
	n: Int,
	bg: String,
	xRes: Int,
	yRes: Int
	) {
}

object Config {

	/**
	 * @param path The path to a json config file (as a resource).
	 * @return A Config object loaded from the json data.
	 */
	def fromJson(path: String): Config = {
		val name = path.split('.')(0)
		val jsonStr = io.Source.fromInputStream(getClass.getResourceAsStream("/" + path)).mkString
		val jsonData = new JSONObject(jsonStr)
		val (rx, ry) = Option(jsonData.optJSONObject("res")).map(
			jObj => {
				(jObj.optInt("x", default.xRes), jObj.optInt("y", default.yRes))
			})
			.getOrElse((default.xRes, default.yRes))

		Config(
			name = name,
			cx = jsonData.optDouble("cx", default.cx),
			cy = jsonData.optDouble("cy", default.cy),
			r = jsonData.optDouble("r", default.r),
			n = jsonData.optInt("n", default.n),
			bg = jsonData.optString("bg", default.bg),
			xRes = rx,
			yRes = ry
		)
	}

	val default =
		Config(
			name = "default",
			cx = 0.5,
			cy = 0.5,
			r = 0.4,
			n = 1000,
			bg = "rgb(0, 0, 0)",
			xRes = 1600,
			yRes = 1200
		)
}
