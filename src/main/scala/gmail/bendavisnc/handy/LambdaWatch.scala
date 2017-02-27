package gmail.bendavisnc.handy

import scala.collection.mutable.ListBuffer

object LambdaWatch {

	case class WatchedExecution(id: String, startTime: Long, span: Long)

	case class TalliedExecution(id: String, executions: List[WatchedExecution]) {
//		def talliedSpan: Long = executions.foldRight(0L)((a, b) => a.span + b)
		def talliedSpan: String = (executions.foldRight(0L)((a, b) => a.span + b) / 1000000000.0).toString + " seconds"
	}

	val watched: ListBuffer[WatchedExecution] = ListBuffer()

	def watch[A](id: String, space: => A): A = {
		val startTime = System.nanoTime()
		val realizedVal = space
		val finishTime = System.nanoTime()
		watched.append(WatchedExecution(id, startTime, finishTime - startTime))
		realizedVal
	}

	def printResults: String = {
		val sBuilder = new StringBuilder()
		val orderedResults: List[TalliedExecution] =
			watched
				.toList
				.groupBy((we) => we.id)
				.map(
					{
						case (k, v) => TalliedExecution(k, v)
					}
				)
				.toList
				.sortWith(_.talliedSpan > _.talliedSpan)

		sBuilder.append("\n----------------------------------------\n")
		for (result <- orderedResults) {
			sBuilder.append(s"𝝺: ${result.id}: time: ${result.talliedSpan}\n")
		}
		sBuilder.append("----------------------------------------\n")
		sBuilder.toString
	}

}
