package stdnote

import org.scalatest.{FunSuite, Matchers}

/**
  * Created by Liam.M(엄익훈) on 12/7/16.
  */


case class MyList[A](list: List[A]) {
  // List[A] ==> f: A => List[B] ===> List[B]
  def flatMap[B](f: A => MyList[B]) : MyList[B] = {
    MyList(list.flatMap(i => f(i).list))
  }
}


class ForSpec extends FunSuite with Matchers {

  test("mylist") {
    val my1 = MyList(List(1, 2, 3))
    val my2 = my1.flatMap((i: Int) => MyList(List(i + 10)))
    my2 shouldBe MyList(List("11", "12", "13"))


  }

  test("for basic") {

    val numList = List(1,2,3)
    val list1 =  for {
      num <- numList
    } yield num.toString

    val list2 = numList.map(_.toString)

    list1 shouldBe list2

    val expr1 = for (num <- numList if num % 2 == 0) yield num * 10
    val expr2 = numList.filter(_ % 2 == 0).map(_ * 10)
    expr1 shouldBe expr2

    val numsList = List(List(0), List(1, 3, 5), List(2, 4))

    val strings1 = for {
      nums <- numsList
      num <- nums
    } yield num.toString

    val string2 = numsList.flatMap(nums => nums.map(_.toString))
    strings1 shouldBe string2
  }
}
