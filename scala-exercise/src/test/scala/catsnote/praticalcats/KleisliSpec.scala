package catsnote.praticalcats

import cats.data.Kleisli
import cats.implicits._
import org.scalatest.{Matchers, WordSpec}

/**
  * Created by Liam.M(엄익훈) on 02/01/2017.
  */
class KleisliSpec extends WordSpec with Matchers{

  "kleisli" should {
    "compose" in {
      val f : Int => String = x => (x * 10).toString
      val g : String => Boolean = x => x.length % 2 == 0

      println(g(f(10)))
      println(g(f(100)))
      def gf(x: Int): Boolean = g(f(x))
      val gf1: (Int) => Boolean = f.andThen(g)
      val gf2 = g.compose(f) // g . f(x)
    }

    "compose with effect" in {
      val f : Int => Option[String] = x => Some((x * 10).toString)
      val g : String => Option[Boolean] = x => Some(x.length % 2 == 0)

//      f.andThen(g)

      def gf(x: Int): Option[Boolean] = {
        for {
          a <- f(x)
          b <- g(a)
        } yield b
      }

      /**
      final case class Kleisli[F[_], A, B](run: A => F[B]) {
        def compose[Z](k: Kleisli[F, Z, A])(implicit F: FlatMap[F]): Kleisli[F, Z, B] =
          Kleisli[F, Z, B](z => k.run(z).flatMap(run))
      }
      */
        //                               String     ==>     Boolean
      val kleisli: Kleisli[Option, Int, Boolean] = Kleisli[Option, Int, String](f).andThen(g)
      println(kleisli.run(10))
      println(kleisli.run(100))

      type Config = String
      trait AccountRepository {
        def getId: Int
      }

      /**
      val f : Int => Option[String] = x => Some((x * 10).toString)
      val g : String => Option[Boolean] = x => Some(x.length % 2 == 0)
      **/
      // Int => Option[String]
      // config
      def foo(a: Int) : Kleisli[Option, AccountRepository, String] = Kleisli(accountRepository => {
        println(Some(accountRepository + " " + a.toString))
        Some(accountRepository + " " + a.toString)
      })

      def bar(b: String) : Kleisli[Option, AccountRepository, Boolean] = Kleisli(accountRepository=> {
        println(Some((accountRepository + " " + b)))
        Some((accountRepository + " " + b).length % 2 == 0)
      })


      object AccountRepositoryImpl extends AccountRepository {
        override def getId: Int = 10
      }
      
      val cc: Kleisli[Option, AccountRepository, Boolean] = foo(10).flatMap(bar)
      val run2: Option[Boolean] = cc.run(AccountRepositoryImpl)
      println(run2)

      Kleisli[Option, AccountRepository, String] { ac =>
        foo(1).run(ac)
      }
    }
  }

}
