package shapenote

/**
  * Created by ikhoon on 2016. 8. 9..
  */
import shapeless._

object size extends Poly1 {
  implicit def caseInt = at[Int](x => 1)
  implicit def caseString = at[String](_.length)
  implicit def caseTuple[T, U](implicit st: Case.Aux[T, Int], su: Case.Aux[U, Int]) =
    at[(T, U)](t => size(t._1) + size(t._2))
}

object addSize extends Poly2 {
  // it does not work if method has return type signature.
  // implicit def default[T](implicit st: size.Case.Aux[T, Int]): addSize.Case[Int, T]
  implicit def default[T](implicit st: size.Case.Aux[T, Int]) = {
    at[Int, T] { case (acc, t) => acc + size(t) }
  }
}

object CovariantHelper {
  trait Fruit
  case class Apple() extends Fruit
  case class Pear() extends Fruit

  type FFFF = Fruit :: Fruit :: Fruit :: Fruit :: HNil
  type APAP = Apple :: Pear :: Apple :: Pear :: HNil

  val a = Apple()
  val p = Pear()

  val apap : APAP = a :: p :: a :: p :: HNil
}

object Main extends App {
  val l = 23 :: "foo" :: (13, "wibble") :: HNil
  println(l.foldLeft(0)(addSize))

}