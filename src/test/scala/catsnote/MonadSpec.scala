package catsnote

import cats.Monad
import org.scalatest.{Matchers, WordSpec}

/**
  * Created by ikhoon on 2016. 7. 21..
  */
class MonadSpec extends WordSpec with Matchers {

  "Monad extends applicative and adding flatten method" should {

    "flatten in stdlib" in {
      Option(Option(1)).flatten shouldBe Option(1)
      Option(None).flatten shouldBe None
      List(List(1), List(2,3)).flatten shouldBe List(1, 2, 3)
      // flatMap == map + flatten
    }

    "foo monad instance" in {
      case class Foo[T](a: T)

      val fooMonadInstance = new Monad[Foo] {
        override def pure[A](x: A): Foo[A] = Foo(x)
        override def flatMap[A, B](fa: Foo[A])(f: (A) => Foo[B]): Foo[B] = f(fa.a)
      }


      val optionMonadInstance = new Monad[Option] {
        override def pure[A](x: A): Option[A] = Option[A](x) // Some(x)

        // fa.a
        // Option(a) => ???
        override def flatMap[A, B](fa: Option[A])
                                  (f: (A) => Option[B]): Option[B] = {
          // fa 에서 a를 끄집어 내서 f에 적용시킨다 f(a)
          fa match {
            case Some(x) => f(x)
            case None => None
          }
        }

      }
      // fooMonadInstance.flatMap()

    }

    "트래킹 정보 알고 싶어" in {

      def getAddress(userId: Long) : Option[String] = if(userId > 10) Some("팡요") else None

      def getShipment(address: String): Option[String] = if(address == "팡요") Some("ship-123") else None

      def getTracking(shipment: String): Option[String] = if(shipment == "ship-123") Some("track-12345") else None

      val userId = 1

      val track: Option[String] =
        getAddress(userId)
        .flatMap(getShipment)
        .flatMap(getTracking)
      println(track)

      val userId2 = 11

      val track2: Option[String] = getAddress(userId2)
        .flatMap(getShipment)
        .flatMap(getTracking)
      println(track2)

      val track3: Option[String] = for {
        address <- getAddress(userId)
        shipment <- getShipment(address)
        track <- getTracking(shipment)
      } yield track

      println(track3)
    }




    "implement pure and flatMap for monad instance" in {
      import cats._

      implicit def optionInstance: Monad[Option] = new Monad[Option] {
        override def pure[A](x: A): Option[A] = pure(x)

        override def flatMap[A, B](fa: Option[A])(f: (A) => Option[B]): Option[B] = fa match {
          case Some(x) => f(x)
          case None => None
        }
      }

      Monad[Option].flatMap(Option(1))(x => Some(x.toString)) shouldBe Option("1")
    }

    "function1 monad" in {
      implicit def function1Instance[I] : Monad[({type L[O]=Function1[I, O]})#L] = {
        new Monad[({type L[O] = Function1[I, O]})#L] {

          override def flatMap[A, B](fa: I => A)(f: A => I => B): I => B =
            i => f(fa(i))(i)
          override def pure[A](x: A): (I) => A = _ => x
        }
      }
      val f : Int => String = (x: Int) => x.toString
      val g : String => Int => (String, Int) = (y: String) => (x: Int) => (y, x)

      Monad[({type L[O] = Function1[Int, O]})#L].flatMap(f)(g)(10) shouldBe ("10", 10)
    }

    "list monad" in {
      implicit val listMonad = new Monad[List] {
        override def flatMap[A, B](fa: List[A])(f: (A) => List[B]): List[B] = fa.map(f).flatten  // bind

        override def pure[A](x: A): List[A] = List(x)  // unit
      }

      Monad[List].flatMap(List(1, 2, 3))(x => List(x, x)) shouldBe List(1, 1, 2, 2, 3, 3)
    }

    "if monad `ifM`" in {
      // flatMap을 수행하면서 true 값만 그결과값에 포함시킨다.
      // flatMap(F[Boolean])(if (_) ifTrue else ifFalse)
      import cats.std.option.optionInstance
      import cats.std.list.listInstance

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
      }

      import cats.std.list._
      optionTMonad[List].pure(42) shouldBe OptionT(List(Option(42)))
      Monad[OptionT[List, ?]].flatMap(OptionT(List(Option(10))))(x => OptionT(List(Option(x + 10)))).value shouldBe List(Option(20))

    }

    "monad transformer with implicit parameter" in {
      import cats.std.list._
      case class ListT[F[_], A](value: F[List[A]])

      implicit def listTMonad[F[_]](implicit F: Monad[F]): Monad[ListT[F, ?]] = new Monad[ListT[F, ?]] {
        override def flatMap[A, B](fa: ListT[F, A])(f: (A) => ListT[F, B]): ListT[F, B] =
          ListT(
            F.flatMap(fa.value)(xs => F.map(F.sequence(xs.map(f(_).value)))(_.flatten))
          )

        override def pure[A](x: A): ListT[F, A] = ListT(F.pure(List(x)))
      }

      import cats.std.option._
      listTMonad[Option].pure(10) shouldBe ListT(Option(List(10)))

      import cats.syntax.all._

      // intellij에서 cannot resolve symbol flatMap 에러가 뜬다... 이런..
      ListT(Option(List(1,2))).flatMap {
        case x: Int => ListT(Option(List(x, x * 10)))
      } shouldBe ListT(Option(List(1, 10, 2, 20)))
    }


  }

}
