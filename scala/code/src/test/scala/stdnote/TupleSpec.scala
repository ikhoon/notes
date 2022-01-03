package stdnote

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

/**
  * Created by Liam.M(엄익훈) on 8/3/16.
  */
class TupleSpec extends AnyFunSuite with Matchers {

  test("tuple") {
    val t: (Int, Long, String, Some[Int]) = (1, 2L, "3", Some(1))
    val (one, two, three, four) = t
    val `Content-Type` = "application/json"
    println(`Content-Type`)
  }

}
