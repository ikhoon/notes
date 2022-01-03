package stdnote

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

/**
  * Created by Liam.M(엄익훈) on 8/10/16.
  */
class ListSpec extends AnyFunSuite with Matchers {

  test("list") {

    // 동시성에 좋지가 않습니다.
    // 동기화 syncronize, lock => bottleneck =>

    // 자바 단점
    // 메모리를 실컷쓰지 못하고
    // CPU 도 못써요
    // BlockIO

    //

    // 불변성, 가변성

    // [immutable], <mutable>

    // 데이터가 만들어지고나서 바뀌는냐?
    // [1, 2, 3, 4]
    // List<String> list = new ArrayList<>; // []
    // list.add(1)  [1]
    // list.add(2)  [1, 2]
    // list.add(3)
    // list.add(4)

    val nil = Nil // List()
    val nil2 = List()

    // [New, .] -> [1, . ] -> [2, . ] -> [3, .] -> [4, .] -> Nil
    // cons // `::`

    val list = List(1, 2, 3, 4)
    val list2 = 1 :: 2 :: 3 :: 4 :: Nil
    val list4 = Nil.::(4).::(3).::(2).::(1)

    list shouldBe list2
    list shouldBe list4
    val list5 = list ::: List(5)
    list.::(5)
    list :: 5 :: Nil // Nil.::(5).::(list)

    // list (1, 2, 3, 4)
    // list5 (1, 2, 3, 4 ,5)

    // list 하나의 데이터를 추가
    // list.add(5)
    5 :: list

    // list.addAll(list2)
    println(list5 :: list)
    println(list5 ::: list)
    println(list ::: list5)

    // ** 패턴 매칭 **
    // fp

    val list6 = List(1, 2, 3)

    // (1) :: (2 :: 3 :: 4 :: Nil)
    list6 match {
      case a :: b :: c :: d :: Nil => println(a, b, c, d)
      case a :: cheese             => println(s"1개 이상 있나? $a, ($cheese)")
      case a :: b :: c :: Nil      => println(s"3나 있나? $a, $b, $c")
      case x                       => println(s"그냥 받는거 ? ($x)")
      case a :: b :: d :: Nil      => println(s"3나2 있나? $a, $b, $d")
      case a :: b :: Nil           => println(s"2나 있나? $a, $b")
      case a :: Nil                => println(s"하나 있나? $a")
      case Nil                     => println("암것도 없음")
    }

    // switch => break;
  }
}
