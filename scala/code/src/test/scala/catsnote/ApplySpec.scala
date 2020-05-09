package catsnote

import cats.{Apply, Functor}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

/**
  * Created by ikhoon on 2016. 7. 19..
  */
class ApplySpec extends AnyWordSpec with Matchers {

  "apply extends functor and implement `ap`" should {

    val intToString: Int => String = _.toString
    val double: Int => Int = _ * 2
    val addTwo: Int => Int = _ + 2
    val addArity2: (Int, Int) => Int = _ + _
    val addArity3: (Int, Int, Int) => Int = _ + _ + _

    "implement option and list apply" in {
      import cats._
      implicit val optionApply = new Apply[Option] {
        override def ap[A, B](ff: Option[(A) => B])(fa: Option[A]): Option[B] = fa.flatMap(a => ff.map(_(a)))
        override def map[A, B](fa: Option[A])(f: (A) => B): Option[B] = fa.map(f)
        override def product[A, B](fa: Option[A], fb: Option[B]): Option[(A, B)] =
          ap(map(fa)(a => (b: B) => (a, b)))(fb)
      }

      implicit val listApply = new Apply[List] {
        override def ap[A, B](ff: List[(A) => B])(fa: List[A]): List[B] = fa.flatMap(a => ff.map(_(a)))
        override def map[A, B](fa: List[A])(f: (A) => B): List[B] = fa.map(f)
        override def product[A, B](fa: List[A], fb: List[B]): List[(A, B)] = ap(fa.map(a => (b: B) => (a, b)))(fb)
      }

      Apply[Option].ap(Option((x: Int) => x + 100))(Option(10)) shouldBe Option(110)
      Apply[Option].ap(None)(Option(10)) shouldBe None

      Apply[List].ap(List((x: String) => x + "!", (x: String) => x + "@"))(List("a", "b", "c")) shouldBe List(
        "a!",
        "a@",
        "b!",
        "b@",
        "c!",
        "c@"
      )

    }

    "apply has functor's map" in {
      import cats.instances.all._

      Apply[Option].map(Some(1))(intToString) shouldBe Some("1")
      Apply[Option].map(Some(1))(double) shouldBe Some(2)
      Apply[Option].map(Some(1))(addTwo) shouldBe Some(3)
      Apply[Option].map(None)(addTwo) shouldBe None

    }

    "compose" in {
      import cats.instances.list._, cats.instances.option._
      val listOpt: Apply[Lambda[X => List[Option[X]]]] = Apply[List].compose(Apply[Option])
      val plusOne: Int => Int = _ + 1
      listOpt.ap(List(Some(plusOne)))(List(Some(1), None, Some(3))) shouldBe List(Some(2), None, Some(4))
    }

    "Apply only have ap function" in {
      import cats.instances.option._
      Apply[Option].ap(Some(intToString))(Some(1)) shouldBe Some("1")
      Apply[Option].ap(Some(double))(Some(1)) shouldBe Some(2)
      Apply[Option].ap(Some(double))(None) shouldBe None
      Apply[Option].ap(None)(Some(1)) shouldBe None
      Apply[Option].ap(None)(None) shouldBe None
    }

    "ap2, ap3.. apN accept arity N function" in {

      import cats.instances.option._
      Apply[Option].ap2(Some(addArity2))(Some(1), Some(2)) shouldBe Some(3)
      Apply[Option].ap2(Some(addArity2))(Some(1), None) shouldBe None

      Apply[Option].ap3(Some(addArity3))(Some(1), Some(2), Some(3)) shouldBe Some(6)

    }

    "Apply has map2, map3 " in {
      import cats.instances.option._
      Apply[Option].map2(Some(1), Some(2))(addArity2) shouldBe Some(3)
      Apply[Option].map3(Some(1), Some(2), Some(3))(addArity3) shouldBe Some(6)
    }

    "tupleN function are available" in {
      import cats.instances.option._
      Apply[Option].tuple2(Some(1), Some(2)) shouldBe Some(1, 2)
      Apply[Option].tuple3(Some(1), Some(2), Some(3)) shouldBe Some(1, 2, 3)
    }

    "Apply builder `|@|` for N arity apN, tupleN, mapN" in {
      import cats.implicits._
      val option2 = (Option(1), Option(2)).tupled
      val option3 = (option2, Option.empty[Int]).tupled.map { case ((x, y), z) => (x, y, z) }
      option2.map(addArity2.tupled) shouldBe Option(3)
      option3.map(addArity3.tupled) shouldBe None
    }
  }

}
