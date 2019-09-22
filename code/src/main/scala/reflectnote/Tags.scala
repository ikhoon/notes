package reflectnote

/**
 * Created by Liam.M on 2018. 10. 25..
 */

class Tags {


  def main(args: Array[String]): Unit = {

  }

 def filterByType[T](xs: List[_]): List[_] = {
    ???
  }


  // universal default
  class Outer1[T]

  // existential default
  class Outer2 {
    type T
  }

  // Universal
  def foo1[A](outer: Outer1[A]) = ???
  def foo2[A](outer: Outer2 { type T = A }) = ???

  // Existential
  def foo31(outer: Outer1[_]) = ???
  def foo4(outer: Outer2): outer.T = ???











}
