package catsnote

import cats.Monad
import cats.data.OptionT
import cats.effect.{Effect, IO}

import scala.concurrent.Future
import cats.implicits._

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by ikhoon on 29/03/2018.
  */
object SemigroupKExample extends App {

  val res2 = OptionT(1.some.pure[Future]) <+> OptionT(none[Int].pure[Future])
  println(res2.value)

  case class User(id: Int, name: String, email: String)

  def fetchLocalCache[F[_]](id: Int)(implicit F: Effect[F]): F[Option[User]] =
    F.delay {
      println("cached local run")
      if (id < 4)
        User(id, s"cached-name-$id", s"$id@gmail.com").some
      else none
    }
    .handleError(ex => {
      ex.printStackTrace()
      None
    })


  def fetchRemoteCache[F[_]](id: Int)(implicit F: Effect[F]): F[Option[User]] =
    F.delay {
      println("cached remote run")
      if (id < 8)
        User(id, s"cached-name-$id", s"$id@gmail.com").some
      else none
    }
      .handleError(ex => {
        ex.printStackTrace()
        None
      })

  // combineK을 이용한 cache miss 처리
  def fetchCacheError[F[_]](id: Int)(implicit F: Effect[F]): OptionT[F, User] =
    OptionT(F.delay {
      throw new Exception("hello")
      println("cached run")
      if (id % 2 == 0)
        User(id, s"cached-name-$id", s"$id@gmail.com").some
      else none[User]
    })

  def fetchDB[F[_]](id: Int)(implicit F: Effect[F]): F[Option[User]] =
    F.delay {
      println("db run")
      User(id, s"db-name-$id", s"$id@gmail.com").some
    }

  def fetch[F[_]: Effect](id: Int): F[Option[User]] = (
   OptionT(fetchLocalCache(id))  <+>
   OptionT(fetchRemoteCache(id)) <+>
   OptionT(fetchDB(id))
  ).value

  println(fetch[IO](11).unsafeRunSync())


  def fetch1[F[_]](id: Int)(implicit F: Effect[F]): F[Option[User]] = {
    fetchLocalCache(id).flatMap {
      case cached @ Some(v) => F.pure(cached)
      case None =>
        fetchRemoteCache(id).flatMap {
          case cached @ Some(v) => F.pure(cached)
          case None =>
            fetchDB(id)
        }
    }
  }
  val res = IO.raiseError(new Exception("First Error")) <+> IO { "Second" }
  println(res.unsafeRunSync())

  val res3 = IO { "First" } <+> IO { "Second" }
  println(res3.unsafeRunSync())

  val res4 = 1.some <+> none
  println(res4)

  val res5 = 1.some <+> 2.some
  println(res5)

  val res6 = none <+> 2.some
  println(res6)
}
