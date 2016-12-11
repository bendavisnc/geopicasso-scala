//package geopicasso
//
//object ConfigTests extends Specification {
//
//	"Basic Config Tests" >> {
//		"minimalist" >> {
//			(Config.fromJson("minimalist.json")
//				must_==
//				Config(
//					cx = 0.5,
//					cy = 0.5,
//					r = 0.8,
//					n = 4,
//					bg = 0,
//					xRes = 800,
//					yRes = 600
//				))
//		}
//
//		"superminimalist" >> {
//			(Config.fromJson("superminimalist.json")
//				must_==
//				Config(
//					cx = 0,
//					cy = 0,
//					r = 0,
//					n = 0,
//					bg = 0,
//					xRes = 0,
//					yRes = 0
//				))
//		}
//	}
//}
