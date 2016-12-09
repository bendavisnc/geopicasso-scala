package geopicasso

import org.json.JSONObject

/**
 * @param cx The x coord of the largest circle.
 * @param cy The y coord of the largest circle.
 * @param r The radius of the largest circle.
 * @param n The number of circles along the largest circle's diameter.
 * @param bg The background color.
 * @param xRes The x resolution.
 * @param yRes The y resolution.
 */
case class Config(
	cx: Double,
	cy: Double,
	r: Double,
	n: Int,
	bg: Int,
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
		val jsonStr = io.Source.fromInputStream(getClass.getResourceAsStream("/" + path)).mkString
		val jsonData = new JSONObject(jsonStr)
		val (rx, ry) = Option(jsonData.optJSONObject("res")).map(
			jObj => {
				(jObj.optInt("x", 0), jObj.optInt("y", 0))
			})
			.getOrElse((0, 0))

		Config(
			cx = jsonData.optDouble("cx", 0),
			cy = jsonData.optDouble("cy", 0),
			r = jsonData.optDouble("r", 0),
			n = jsonData.optInt("n", 0),
			bg = jsonData.optInt("bg", 0),
			xRes = rx,
			yRes = ry
		)
	}

}
