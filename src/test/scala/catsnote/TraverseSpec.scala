package catsnote

import cats.data.Validated.{Invalid, Valid}
import cats.data._
import cats.{Applicative, Semigroup, Traverse}
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.language.postfixOps

/**
  * Created by ikhoon on 2016. 7. 24..
  */
class TraverseSpec extends WordSpec with Matchers {

  "Traverse" should {

    // sequence + map
    // 질문 sequence : flatten(x),
    // List[Future[String]] => Future[List[String]]
    // F[G[T]] --- sequence ---> G[F[T]]
    // F[G[T]] --- sequence + map[T -> R] ---> G[F[R]]  // traverse


    // List(Option(1), Option(2), Option(3)) => Option(List(1, 2, 3))


    // map         A => B
    // ap        F[A => B]
    // flatMap(f)(fa)  fa = Future, Option, List, :: f : A => F[B]


    def parseIntXor(s: String) : Xor[NumberFormatException, Int] =
      Xor.catchOnly[NumberFormatException](s.toInt)

    def parseIntValidated(s: String) : ValidatedNel[NumberFormatException, Int] =
      Validated.catchOnly[NumberFormatException](s.toInt).toValidatedNel


    "traverseU - Xor - unapply with traverse for Applicative[F[A,B]]" in {

      import cats.std.list.listInstance
      import cats.std.option._
      import cats.syntax.all._
      println(List(Option(1), Option(2), Option(3)).traverse(identity))
      println(List(Option(1), Option(2), Option(3)).sequence)


      // does not compile, required G[B] but found G[A, B]
      // Traverse[List].traverse(List("1", "2", "3"))(parseIntXor)

     // Traverse[List].traverseU(List("1", "2", "3"))(parseIntXor) shouldBe Xor.Right(List(1, 2, 3))
     // Traverse[List].traverseU(List("1", "abc", "3"))(parseIntXor).isLeft shouldBe true
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

    // userIds를 가지고 User를 가져오면 됩니다.
    // ** Future[List[User]] **
    // 1번째 미션 userIds를 가지고 getUser함수를 호출해서 나오는 결과값을 출력해봅시다.
    // map 사용하면 됩니다.
    // future.foreach
    // Await.result(future, Duration.Inf)

    import cats.syntax.all._
    import cats.std.all._
    // List[Future[User]] => Future[List[User]] => 출력해보세요~

    //

    val expected = List(User(1, "name-1"), User(2, "name-2"), User(3, "name-3"), User(4, "name-4"))

    "parallel programming with future" in {
      import cats.std.future.futureInstance
      import cats.std.list.listInstance

      // map + sequence
      val listFutureUser: List[Future[User]] = userIds.map(getUser)
      val futureListUser: Future[List[User]] =
        Applicative[Future].sequence(listFutureUser)

      // traverse
      val traverse: Future[List[User]] = Traverse[List].traverse(userIds)(getUser)
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
