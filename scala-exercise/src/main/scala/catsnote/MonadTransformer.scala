package catsnote

import cats.data.{EitherT, Nested, OptionT}
import cats.{Applicative}
import cats.implicits._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by Liam.M on 2018. 04. 12..
  */
object MonadTransformer extends App {

  val futureOption = Future.successful(Option(1))
  /** 컴파일 안됨
  val fo2 = for {
    option <- futureOption
    x <- option
  } yield x + 1
  */

  val xs = List(1, 2, 3)
  val maybe = Option(1)

  // DOTTY
  /**
  for {
    x <- xs
    y <- maybe
  } yield x + y
  */

  val eventual: Future[Option[Int]] =
    Applicative[Future]
      .compose[Option]
      .map(futureOption)(x => x + 1)

  // ListT

  val l: Option[List[Int]] = Some(List(1, 2, 3, 4, 5))
  val nl = Nested(l)

  type FutureEither[A] = EitherT[Future, String, A]

  val a: Future[Either[String, Option[Int]]] =
    Future.successful(Either.right(Option(1)))

  val b: EitherT[Future, String, Option[Int]] = EitherT(a)
  val c: OptionT[FutureEither, Int] = OptionT[FutureEither, Int](b)
  c.map(x => x + 1)

  // Option =>
  // Writer[Reader[Either[Option[]]]

  // Either =>
  // Writer[Reader[Either[Option[]]]

  type FutureEitherOption[A] = OptionT[FutureEither, A]




}
