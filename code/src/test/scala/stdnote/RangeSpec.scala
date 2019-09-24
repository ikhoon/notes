package stdnote

import org.scalatest.{FunSuite, Matchers}

/**
 * Created by Liam.M(엄익훈) on 9/7/16.
 */
class RangeSpec extends FunSuite with Matchers {

  test("range") {
    val ten = Range(1, 10)
    println(ten)
    val ten2 = 1 to 10
    println(ten2)
    val ten3 = Range(1, 10, 2)
    println(ten3)
    val ten4 = 1 until 10
    println(ten4)
    val ten5 = Range('a', 'z')
    ten5.foreach(x => print(x.toChar))
  }

}
