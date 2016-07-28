package catsnote

import cats.data.Validated.{Invalid, Valid}
import cats.{Applicative, Semigroup, Traverse}
import cats.data._
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.language.postfixOps

/**
  * Created by ikhoon on 2016. 7. 24..
  */
class TraverseSpec extends WordSpec with Matchers {

  "Traverse" should {

    def parseIntXor(s: String) : Xor[NumberFormatException, Int] =
      Xor.catchOnly[NumberFormatException](s.toInt)

    def parseIntValidated(s: String) : ValidatedNel[NumberFormatException, Int] =
      Validated.catchOnly[NumberFormatException](s.toInt).toValidatedNel


    "traverseU - Xor - unapply with traverse for Applicative[F[A,B]]" in {
      import cats.std.list.listInstance

      // does not compile, required G[B] but found G[A, B]
      // Traverse[List].traverse(List("1", "2", "3"))(parseIntXor)

      Traverse[List].traverseU(List("1", "2", "3"))(parseIntXor) shouldBe Xor.Right(List(1, 2, 3))
      Traverse[List].traverseU(List("1", "abc", "3"))(parseIntXor).isLeft shouldBe true
    }

    "traverseU - Validated" in {
      import cats.std.list.listInstance

      implicit def nelSemigroup[A]: Semigroup[NonEmptyList[A]] =
        OneAnd.oneAndSemigroupK[List].algebra[A]

      Traverse[List].traverseU(List("1", "2", "3"))(parseIntValidated) shouldBe Valid(List(1, 2, 3))
      Traverse[List].traverseU(List("1", "2", "3"))(parseIntValidated).isValid shouldBe true

      Traverse[List].traverseU(List("1", "abc", "3", "def"))(parseIntValidated).isValid shouldBe false


    }

    import scala.concurrent.ExecutionContext.Implicits.global
    val userIds = List(1, 2, 3, 4)
    case class User(id: Int, name: String)
    def getUser(id: Int): Future[User] = Future { User(id, s"name-$id") }

    val expected = List(User(1, "name-1"), User(2, "name-2"), User(3, "name-3"), User(4, "name-4"))

    "parallel programming with future" in {
      import cats.std.future.futureInstance
      import cats.std.list.listInstance

      // map
      val listFutureUser: List[Future[User]] = userIds.map(getUser)
      // sequence
      val futureListUser: Future[List[User]] = Applicative[Future].sequence(listFutureUser)

      Await.result(futureListUser, Duration.Inf) shouldBe expected

    }

    "easy parallel programming with traverse and future" in {
      import scala.concurrent.ExecutionContext.Implicits.global
      import cats.std.list.listInstance
      import cats.std.future.futureInstance

      // traverse == sequence with map
      // sequence == traverse with identity

      // Traverse[List].traverse(userIds)(getUser) return Future[List[User]]
      val futureListUser: Future[List[User]] = Traverse[List].traverse(userIds)(getUser)
      Await.result(futureListUser, Duration.Inf) shouldBe expected

    }

    "sequence" in {

      import cats.std.option.optionInstance
      import cats.std.list.listInstance

      Traverse[List].traverse(List(Option(1), Option(2), Option(3)))(ga => ga) shouldBe Option(List(1, 2, 3))
      Traverse[List].traverse(List(Option(1), None, Option(3)))(ga => ga) shouldBe None

      Traverse[List].sequence(List(Option(1), Option(2), Option(3))) shouldBe Option(List(1, 2, 3))
      Traverse[List].sequence(List(Option(1), None, Option(3))) shouldBe None
    }

    "ignore value produced" in {
      import cats.std.list.listInstance
      import cats.std.option.optionInstance
      Traverse[List].sequence_(List(Option(1), Option(2), Option(3))) shouldBe Some(())
      Traverse[List].sequence_(List(Option(1), None, Option(3))) shouldBe None
    }
  }

}