package catsnote

import cats.Monoid
import cats.implicits._

/**
  * Created by Liam.M on 2017. 12. 26..
  */

/*
trait Monoid[A] {
  // 항등원
  def empty: A
  // 합치는 거
  def combine(x: A, y: A): A
}

object Test extends App {

  val xs1 = List(1, 2, 3)

  //
  val sum = xs1.foldLeft(0) { _ + _ }

  val intAdder = new Monoid[Int] {
    def empty: Int = 0
    def combine(x: Int, y: Int): Int = x + y
  }
  val intMul = new Monoid[Int] {
    def empty: Int = 1
    def combine(x: Int, y: Int): Int = x * y
  }

  def total2[A](xs: List[A])(adder: Monoid[A]): A =
    xs.foldLeft(adder.empty) { (x, y) => adder.combine(x, y) }

  def total3[A](xs: List[A])(implicit adder: Monoid[A]): A =
    xs.foldLeft(adder.empty) { (x, y) => adder.combine(x, y) }

  total2(xs1)(intAdder)
  total2(xs1)(intMul)

  implicit val intAdder2 = new Monoid[Int] {
    def empty: Int = 0
    def combine(x: Int, y: Int): Int = x + y
  }
  total3(xs1)

}

*/

object Test2 extends App {
  def total3[A](xs: List[A])(implicit adder: Monoid[A]): A =
    xs.foldLeft(adder.empty) { (x, y) => adder.combine(x, y) }

  val xss = List(List(1, 2), List(2, 3))
  println(total3(xss))
}











