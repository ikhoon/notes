package catsnote

import cats.{Monoid, MonoidK, SemigroupK}

/*
/**
  * Created by Liam.M on 2018. 03. 08..
  */
object MonoidInstance extends App {

  val intPlusMonoidInstance = new Monoid[Int] {
    def empty: Int = 0

    def combine(x: Int, y: Int): Int = x + y
  }






  implicit val intMulMonoidInstance = new Monoid[Int] {
    def empty: Int = 1
    def combine(x: Int, y: Int): Int = x * y
  }

  import cats.syntax.all._
  import cats.implicits._
  val x = 10
  val y = 20
  val z = x |+| y
  println(z)


  val xs = List(1, 2, 3)
  xs.foldLeft(Monoid[Int].empty)(_ |+| _)

  def foldLeft[A](xs: List[A])(implicit M: Monoid[A]): A =
    xs.foldLeft(Monoid[A].empty)(_ |+| _)

  def foldLeft1[A: Monoid](xs: List[A]): A =
    xs.foldLeft(Monoid[A].empty)(_ |+| _)
  def test[A: Either](xs: List[A]): A = ???

  case class Foo(a: Int)
  implicit val fooMonoidInstance = new Monoid[Foo] {
    def empty: Foo = Foo(Monoid[Int].empty)
    def combine(x: Foo, y: Foo): Foo = Foo(x.a |+| y.a)
  }

  // F[_] => higher kinded type
  // Monoid => Int, String, Foo
  MonoidK[Option]

  SemigroupK[Option]


  // Plus
  // SemigroupK

  val careA: Option[Int] = Some(1)

  val careB: Option[Int] = None

 // |+|
  careA <+> careB






}

*/
