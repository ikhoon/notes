package catsnote

import cats.data.Validated.{Invalid, Valid}
import cats.{Applicative, Semigroup, Traverse}
import cats.data._
import org.scalatest.{Matchers, WordSpec}
import cats.syntax._
import cats.syntax.either._
import cats.implicits._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.language.postfixOps

/**
  * Created by ikhoon on 2016. 7. 24..
  */
class TraverseSpec extends WordSpec with Matchers {

  "Traverse" should {

    def parseIntEither(s: String) : Either[NumberFormatException, Int] =
      Right(s.toInt)

    def parseIntValidated(s: String) : ValidatedNel[NumberFormatException, Int] =
      Validated.catchOnly[NumberFormatException](s.toInt).toValidatedNel


    "traverseU - Either - unapply with traverse for Applicative[F[A,B]]" in {
      import cats.instances.list._

      // does not compile, required G[B] but found G[A, B]
      // Traverse[List].traverse(List("1", "2", "3"))(parseIntEither)

//      Traverse[List].traverseU(List("1", "2", "3"))(parseIntEither) shouldBe Either.right(List(1, 2, 3))
      Traverse[List].traverse(List("1", "abc", "3"))(parseIntEither).isLeft shouldBe true
    }

    "traverseU - Validated" in {
      import cats.instances.list._

      Traverse[List].traverse(List("1", "2", "3"))(parseIntValidated) shouldBe Valid(List(1, 2, 3))
      Traverse[List].traverse(List("1", "2", "3"))(parseIntValidated).isValid shouldBe true

      Traverse[List].traverse(List("1", "abc", "3", "def"))(parseIntValidated).isValid shouldBe false


    }

    import scala.concurrent.ExecutionContext.Implicits.global
    val userIds = List(1, 2, 3, 4)
    case class User(id: Int, name: String)
    def getUser(id: Int): Future[User] = Future { User(id, s"name-$id") }

    val expected = List(User(1, "name-1"), User(2, "name-2"), User(3, "name-3"), User(4, "name-4"))

    "parallel programming with future" in {
      import cats.instances.future._
      import cats.instances.list._

      // map
      val listFutureUser: List[Future[User]] = userIds.map(getUser)
      // sequence
      val futureListUser = Traverse[List].sequence[Future, User](listFutureUser)

      Await.result(futureListUser, Duration.Inf) shouldBe expected

    }

    "easy parallel programming with traverse and future" in {
      import scala.concurrent.ExecutionContext.Implicits.global
      import cats.instances.list._
      import cats.instances.future._

      // traverse == sequence with map
      // sequence == traverse with identity

      // F[A] ==> (A => G[B]) ==> F[G[B]] => G[F[B]]
      // Traverse[List].traverse(userIds)(getUser) return Future[List[User]]
      val futureListUser: Future[List[User]] = Traverse[List].traverse(userIds)(getUser)
      Await.result(futureListUser, Duration.Inf) shouldBe expected

    }

    "sequence" in {

      import cats.instances.option._
      import cats.instances.list._

      Traverse[List].traverse(List(Option(1), Option(2), Option(3)))(ga => ga) shouldBe Option(List(1, 2, 3))
      Traverse[List].traverse(List(Option(1), None, Option(3)))(ga => ga) shouldBe None

      Traverse[List].sequence(List(Option(1), Option(2), Option(3))) shouldBe Option(List(1, 2, 3))
      Traverse[List].sequence(List(Option(1), None, Option(3))) shouldBe None
    }

    "ignore value produced" in {
      import cats.instances.list._
      import cats.instances.option._
      Traverse[List].sequence_(List(Option(1), Option(2), Option(3))) shouldBe Some(())
      Traverse[List].sequence_(List(Option(1), None, Option(3))) shouldBe None
    }
  }

}
