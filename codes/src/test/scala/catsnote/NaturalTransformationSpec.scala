package catsnote

import org.scalatest.FunSuite

import scala.util.Try

class NaturalTransformationSpec extends FunSuite {

  test("test") {
    // M[A] => M[B]
    // map A => B
    // flatMap A => M[B]

    val t: Try[Int] = Try { 1 }

    // M[A] => N[A]

    val a = List(1, 2, 4)
//    a.foldMap(M[A] ~> M[B])

  }
}
