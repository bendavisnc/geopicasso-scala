package geopicasso

import utest._

object ConfigTests extends TestSuite {

	val tests = this {
		'minimalist  {
			assert(
				Config.fromJson("minimalist.json")
				==
				Config(
					name = "minimalist",
					cx = 0.5,
					cy = 0.5,
					r = 0.8,
					n = 4,
					bg = "rgb(0, 0, 0)",
					xRes = 800,
					yRes = 600
				))
		}

		'superminimalist  {
			assert(
				Config.fromJson("superminimalist.json")
					==
					Config.default.copy(name = "superminimalist")
			)
		}

		'ednVsJson_minimalist {
			assert(
				Config.from("minimalist.json")
					==
				Config.from("minimalist.edn")
			)
		}

		'ednVsJson_superminimalist {
			assert(
				Config.from("superminimalist.json")
					==
					Config.from("superminimalist.edn")
			)
		}
	}

}
