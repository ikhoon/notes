package algonote
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import scala.collection.mutable

/**
  * Created by ikhoon on 2018-09-26.
  */
class Algo_2_4_2SumTest extends AnyFunSuite with Matchers {

  test("2sum") {
    val inputs = scala.io.Source
      .fromResource("twosum.txt")
      .getLines()
      .toVector
      .map(_.toLong)

    val sets = inputs.toSet

    val MAX = 10000L
    val MIN = -10000L
    val TARGETS = (MIN to MAX).toList
    val results = mutable.Set.empty[Long]
    val res = inputs
      .foreach { x =>
        if (x % 100 == 0) println(s"x = $x")
        var t = MIN
        while (t <= MAX) {
          val y: Long = t - x
          if (sets.contains(y))
            results += t
          t += 1
        }
      }
    println(results.size)
  }
}
