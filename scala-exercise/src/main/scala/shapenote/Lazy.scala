package shapenote

import shapeless.Lazy

/**
 * Created by Liam.M(엄익훈) on 8/22/16.
 */

sealed trait List0[+T]
case class Cons[T](hd: T, tl: List0[T]) extends List0[T]
sealed trait Nil extends List0[Nothing]
case object Nil extends Nil

trait Show[T] {
  def apply(t: T): String
}

object Show {
  implicit def showInt : Show[Int] = new Show[Int] {
    override def apply(t: Int): String = t.toString
  }
  implicit def showNil: Show[Nil] = new Show[Nil] {
    override def apply(t: Nil): String = "Nil"
  }

  // Case for Cons[T]: note (mutually) recursive implicit argument referencing Show[List[T]]
  implicit def showCons[T](implicit  st: Lazy[Show[T]], sl: Lazy[Show[List0[T]]]) : Show[Cons[T]] = new Show[Cons[T]] {
    override def apply(t: Cons[T]): String = s"Cons(${show(t.hd)(st.value)}, ${show(t.tl)(sl.value)})"
  }

  // Case for List[T]: note (mutually) recursive implicit argument referencing Show[Cons[T]]
  implicit def showList[T](implicit sc: Lazy[Show[Cons[T]]]) = new Show[List0[T]] {
    override def apply(t: List0[T]): String = t match {
      case n: Nil => show(n)
      case c: Cons[T] => show(c)(sc.value)
    }
  }

  def show[T](t: T)(implicit s: Show[T]) = s(t)


}
