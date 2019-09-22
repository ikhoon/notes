package stdnote

import org.scalatest.{FunSuite, Matchers}

import scala.collection.immutable.Set

/**
 * Created by Liam.M(엄익훈) on 8/17/16.
 */
class SetSpec extends FunSuite with Matchers {

  test("set") {
    val list = List(1, 1, 1, 1, 1, 1)
    val set1 = Set(1, 1, 1, 1, 1, 1)
    println(list)
    println(set1)

    val set2 = set1 + 2
    println(set2)
    val set3 = Set(2,3,4,5)
    val set4 = set2 ++ set3
    println(set4)

    // 집합
    // 교집합, 차집합, 합집합
    println(set4.intersect(set2)) // 교집합
    val set5 = Set(2, 3, 4, 5)
    println(set5.union(set2))
    println(set5.diff(set2))
    println(set2.diff(set5))

    // 자바의 6, primitive, int <string?> switch(xxxx)
    // 자바 7 switch string!!!! 3
    // match 100가지
  }

}
