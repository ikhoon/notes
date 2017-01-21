package catsnote

import cats.{Applicative, Apply, Eval, Foldable, Later, Now}
import org.scalatest.{Matchers, WordSpec}
import cats.syntax.either._


/**
  * Created by ikhoon on 2016. 7. 22..
  */
class FoldableSpec extends WordSpec with Matchers {

  "Foldable" should {
    "foldLeft is an eager left-associative fold" in {
      import cats.instances.list._
      Foldable[List].foldLeft(List(1, 2, 3), 0)(_ + _) shouldBe (6)
      Foldable[List].foldLeft(List("a", "b", "c"), "")(_ + _ ) shouldBe "abc"
    }

    "foldRight is a lazy right-associative fold" in {
      import cats.instances.list._
      val lazyValue: Eval[Int] = Foldable[List].foldRight(List(1, 2, 3), Now(0))((x, rest) => Later(x + rest.value))
      lazyValue.value shouldBe 6
    }

    "fold is called combineAll" in {
      import cats.implicits._
      Foldable[List].fold(List(1, 2, 3)) shouldBe 6
      Foldable[List].fold(List("a", "b", "c")) shouldBe "abc"
    }

    "foldMap is similar to fold but maps every A in F[A]" in {
      import cats.instances.list._
      import cats.instances.string._
      import cats.instances.int._
      Foldable[List].foldMap(List(1, 2, 3))(_.toString) shouldBe "123"
      Foldable[List].foldMap(List("a", "b", "c"))(_.length) shouldBe 3
    }

    "foldK is similar to fold but using the given MonoidK[G] instead of Monoid[G]" in {

      import cats.instances.list._
      import cats.instances.option._
      Foldable[List].foldK(List(List(1, 2), List(3, 4, 5))) shouldBe List(1, 2, 3, 4, 5)
      Foldable[List].foldK(List(None, Option("two"), Option("three"))) shouldBe Option("two")
    }

    "find searches for the first element matching in the predicate" in {
      import cats.instances.list._
      Foldable[List].find(List(1, 2, 3, 4))(_ > 2) shouldBe Option(3)
      Foldable[List].find(List(1, 2, 3, 4))(_ > 5) shouldBe None
    }

    "exists checks whether at least one element satisfies the predicate" in {
      import cats.instances.list._
      Foldable[List].exists(List(1, 2, 3))(_ > 1) shouldBe true
      Foldable[List].exists(List(1, 2, 3))(_ > 5) shouldBe false

    }

    "forall checks where all elements satisfy the predicate" in {
      import cats.instances.list._
      Foldable[List].forall(List(1, 2, 3))(_ > 0) shouldBe true
      Foldable[List].forall(List(1, 2, 3))(_ > 1) shouldBe false
    }

    "toList convert F[A] to List[A]" in {

      import cats.instances.option._
      import cats.instances.set._
      Foldable[Set].toList(Set(1, 2, 3)) shouldBe List(1, 2, 3)
      Foldable[Option].toList(Option(1)) shouldBe List(1)
      Foldable[Option].toList(None) shouldBe Nil
    }

    "filter_ convert F[A] to List[A] only including elements that matches the predicate" in {
      import cats.instances.vector._
      import cats.instances.option._
      Foldable[Vector].filter_(Vector(1, 2, 3))(_ > 1) shouldBe List(2, 3)
      Foldable[Option].filter_(Option(2))(_ > 1) shouldBe List(2)
      Foldable[Option].filter_(Option(2))(_ > 3) shouldBe Nil
    }

    "traverse - remind" in {
      import cats.instances.vector._
      import cats.instances.list._
      import cats.instances.option._
      Applicative[Vector].traverse(Vector(1, 2, 3))(x => Vector(x)) shouldBe Vector(Vector(1, 2, 3))
      Applicative[Vector].traverse(Option(1))((x: Int) => Vector(x)) shouldBe Vector(Option(1))
      Applicative[Option].traverse(List(1, 2, 3))(x => Option(x * 10)) shouldBe Option(List(10, 20, 30))
      Applicative[Option].traverse(List[Int => Int](_ * 10, _ + 10))(f => Option(f(10))) shouldBe Option(List(100, 20))
    }

    "traverse_ is useful when G[_] represent an action or effect " in {
      import cats.instances.list._
      import cats.instances.option._
      def parseInt(s: String) : Option[Int] =
        Either.catchOnly[NumberFormatException](s.toInt).toOption
      Foldable[List].traverse_(List("1", "2", "3"))(parseInt) shouldBe Option(())
      Foldable[List].traverse_(List("a", "b", "c"))(parseInt) shouldBe None
    }

    "compose Foldable[F[_]] and Foldable[G[_]] to obtain Foldable[F[G]]" in {
      import cats.instances.option._
      import cats.instances.list._
      import cats.instances.int._
      import cats.instances.string._

      val listOptionFoldable = Foldable[List] compose Foldable[Option]

      listOptionFoldable.fold(List(Option(1), Option(2), Option(3), Option(4))) shouldBe 10
      listOptionFoldable.fold(List(Option(1), None, Option(3))) shouldBe 4
      listOptionFoldable.fold(List(Option("1"), None, Option("3"))) shouldBe "13"
    }

    "more foladble methods" in {
      import cats.instances.list._
      import cats.instances.option._
      Foldable[List].isEmpty(List(1, 2, 3)) shouldBe false
      Foldable[List].takeWhile_(List(1, 2, 3, 4))(_ < 3) shouldBe List(1, 2)
      Foldable[List].dropWhile_(List(1, 2, 3, 4))(_ < 3) shouldBe List(3, 4)
      Foldable[Option].dropWhile_(Option(1))(_ < 3) shouldBe List()
    }
  }


}
