package geopicasso


import org.json.JSONObject
import us.bpsm.edn.parser.Parsers


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
	fills: List[(String, Double)],
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

		val fs = Option(jsonData.optJSONArray("fills")).map(
			jArr => {
				(for(i <- Range(0, jArr.length())) yield {
					val jObj = jArr.getJSONObject(i)
					(jObj.getString("color"), jObj.getDouble("opacity"))
				}).toList
			}
		).getOrElse(default.fills)

		Config(
			name = name,
			cx = jsonData.optDouble("cx", default.cx),
			cy = jsonData.optDouble("cy", default.cy),
			r = jsonData.optDouble("r", default.r),
			n = jsonData.optInt("n", default.n),
			bg = jsonData.optString("bg", default.bg),
			fills = fs,
			xRes = rx,
			yRes = ry
		)
	}

	def fromEdn(path: String): Config = { // an admitted mess...
		import scala.collection.JavaConverters._
		val name = path.split('.')(0)
		val ednStr = io.Source.fromInputStream(this.getClass.getResourceAsStream("/" + path)).mkString
		val parsable = Parsers.newParseable(ednStr)
		val parser = Parsers.newParser(Parsers.defaultConfiguration())
		val ednData = parser.nextValue(parsable).asInstanceOf[java.util.Map[String, Object]]
		val (rx, ry) = Option(ednData.get("res").asInstanceOf[java.util.Map[String, Long]]).map(
			(mVal: java.util.Map[String, Long]) => {
				(mVal.asScala.getOrElse("x", default.xRes.toLong).toInt, mVal.asScala.getOrElse("y", default.yRes.toLong).toInt)
			})
			.getOrElse((default.xRes, default.yRes))

		val fs = Option(ednData.get("fills").asInstanceOf[java.util.List[java.util.Map[String, Object]]]).map(
			arr => {
				(arr.asScala.map(
					cData => {
						(cData.get("color").asInstanceOf[String], cData.get("opacity").asInstanceOf[Double])
					}
				)).toList
			}
		).getOrElse(default.fills)

		Config(
			name = name,
			cx = ednData.asScala.getOrElse("cx", default.cx).asInstanceOf[Double],
			cy = ednData.asScala.getOrElse("cy", default.cy).asInstanceOf[Double],
			r = ednData.asScala.getOrElse("r", default.r).asInstanceOf[Double],
			n = ednData.asScala.getOrElse("n", default.n.toLong).asInstanceOf[Long].toInt,
			bg = ednData.asScala.getOrElse("bg", default.bg).asInstanceOf[String],
			fills = fs,
			xRes = rx,
			yRes = ry
		)
	}

	def from(path: String): Config = {
		if (path.contains("edn"))
			fromEdn(path)
		else if (path.contains("json"))
			fromJson(path)
		else
			throw new Exception("Unrecognized config type.")
	}

	val default =
		Config(
			name = "default",
			cx = 0.5,
			cy = 0.5,
			r = 0.4,
			n = 1000,
			bg = "rgb(0, 0, 0)",
			fills = ("black", 0.0) :: Nil,
			xRes = 1600,
			yRes = 1200
		)
}
