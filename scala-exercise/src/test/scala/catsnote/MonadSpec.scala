package catsnote

import cats.Monad
import org.scalatest.{Matchers, WordSpec}
import cats.syntax.either._

import scala.annotation.tailrec

/**
  * Created by ikhoon on 2016. 7. 21..
  */
class MonadSpec extends WordSpec with Matchers {

  "Monad extends applicative and adding flatten method" should {

    "flatten in stdlib" in {
      Option(Option(1)).flatten shouldBe Option(1)
      Option(None).flatten shouldBe None
      List(List(1), List(2,3)).flatten shouldBe List(1, 2, 3)
    }

    "implement pure and flatMap for monad instance" in {
      import cats._
      import cats.instances.all._

      implicit val optionInstance: Monad[Option] = new Monad[Option] {
        override def pure[A](x: A): Option[A] = pure(x)

        override def flatMap[A, B](fa: Option[A])(f: (A) => Option[B]): Option[B] = fa match {
          case Some(x) => f(x)
          case None => None
        }

        @tailrec
        def tailRecM[A, B](a: A)(f: A => Option[Either[A, B]]): Option[B] =
          f(a) match {
            case None => None
            case Some(Left(a1)) => tailRecM(a1)(f)
            case Some(Right(b)) => Some(b)
          }

      }

      Monad[Option].flatMap(Option(1))(x => Some(x.toString)) shouldBe Option("1")
    }

    // Option[A] * -> *, A => Option[A]
    // Monad[F[_]] * -> * -> *, A => Option => Monad[Option[A]]

    // Function[I, ?] => ? => Function1 => Fuction1[I, ?]

    "function1 monad" in {
      implicit def function1Instance[I] : Monad[Function1[I, ?]] = {
        new Monad[({type L[O] = Function1[I, O]})#L] {

          override def flatMap[A, B](fa: I => A)(f: A => I => B): I => B =
            i => f(fa(i))(i)
          override def pure[A](x: A): (I) => A = _ => x

          def tailRecM[A, B](a: A)(fn: A => I => Either[A, B]): I => B =
            (t: I) => {
              @tailrec
              def step(thisA: A): B = fn(thisA)(t) match {
                case Right(b) => b
                case Left(nextA) => step(nextA)
              }
              step(a)
            }
        }
      }
      val f : Int => String = (x: Int) => x.toString
      val g : String => Int => (String, Int) = (y: String) => (x: Int) => (y, x)

      Monad[Function1[Int, ?]].flatMap(f)(g)(10) shouldBe ("10", 10)
    }

    "list monad" in {
      implicit val listMonad = new Monad[List] {
        override def flatMap[A, B](fa: List[A])(f: (A) => List[B]): List[B] = fa.map(f).flatten

        override def pure[A](x: A): List[A] = List(x)

        def tailRecM[A, B](a: A)(f: A => List[Either[A, B]]): List[B] = {
          val buf = List.newBuilder[B]
          @tailrec def go(lists: List[List[Either[A, B]]]): Unit = lists match {
            case (ab :: abs) :: tail => ab match {
              case Right(b) => buf += b; go(abs :: tail)
              case Left(a) => go(f(a) :: abs :: tail)
            }
            case Nil :: tail => go(tail)
            case Nil => ()
          }
          go(f(a) :: Nil)
          buf.result
        }
      }

      Monad[List].flatMap(List(1, 2, 3))(x => List(x, x)) shouldBe List(1, 1, 2, 2, 3, 3)
    }

    "if monad `ifM`" in {
      // flatMap을 수행하면서 true 값만 그결과값에 포함시킨다.
      // flatMap(F[Boolean])(if (_) ifTrue else ifFalse)
      import cats.instances.option._
      import cats.instances.list._

      Monad[Option].ifM(Option(true))(Option("truethy"), Option("falsy")) shouldBe Option("truethy")
      Monad[List].ifM(List(true, false, true))(List(1, 2), List(3, 4)) shouldBe List(1, 2, 3, 4, 1 ,2)

    }

    "monad composition, monad transformer with context bound" in {
      // context bound 방법을 쓰는게 implicit parameter를 받는것 보다 구현이 더 까다로운거 같다.
      case class OptionT[F[_], A](value: F[Option[A]])

      implicit def optionTMonad[F[_]: Monad]: Monad[OptionT[F, ?]] = new Monad[OptionT[F, ?]] {

        override def flatMap[A, B](fa: OptionT[F, A])(f: (A) => OptionT[F, B]): OptionT[F, B] = {
          OptionT(implicitly[Monad[F]].flatMap(fa.value){
            case Some(x) => f(x).value
            case None =>  implicitly[Monad[F]].pure(None)
          })
        }
        override def pure[A](x: A): OptionT[F, A] = OptionT(implicitly[Monad[F]].pure(Some(x)))

        def tailRecM[A, B](a: A)(f: A => OptionT[F, Either[A, B]]): OptionT[F, B] =
          OptionT(implicitly[Monad[F]].tailRecM(a)(a0 => implicitly[Monad[F]].map(f(a0).value)(
            _.fold(Either.right[A, Option[B]](None))(_.map(b => Some(b): Option[B]))
          )))
      }

      import cats.instances.list._
      optionTMonad[List].pure(42) shouldBe OptionT(List(Option(42)))
      Monad[OptionT[List, ?]].flatMap(OptionT(List(Option(10))))(x => OptionT(List(Option(x + 10)))).value shouldBe List(Option(20))

    }

    "monad transformer with implicit parameter" in {
      import cats.instances.list._
      case class ListT[F[_], A](value: F[List[A]])

      implicit def listTMonad[F[_]](implicit F: Monad[F]): Monad[ListT[F, ?]] = new Monad[ListT[F, ?]] {
        override def flatMap[A, B](fa: ListT[F, A])(f: (A) => ListT[F, B]): ListT[F, B] =
          ListT(
            F.flatMap(fa.value)(xs => F.map(F.sequence(xs.map(f(_).value)))(_.flatten))
          )

        override def pure[A](x: A): ListT[F, A] = ListT(F.pure(List(x)))

        override def tailRecM[A, B](a: A)(f: (A) => ListT[F, Either[A, B]]): ListT[F, B] = ???
      }

      import cats.instances.option._
      listTMonad[Option].pure(10) shouldBe ListT(Option(List(10)))

      import cats.syntax.all._

      // intellij에서 cannot resolve symbol flatMap 에러가 뜬다... 이런..
      ListT(Option(List(1,2))).flatMap {
        case x: Int => ListT(Option(List(x, x * 10)))
      } shouldBe ListT(Option(List(1, 10, 2, 20)))
    }


  }

}
