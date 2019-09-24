package circenote

import shapeless._
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.shapes._
/**
  * Created by Liam.M on 2018. 08. 07..
  */
object CirceCoproduct {
  def main(args: Array[String]): Unit = {
    case class A(a: Int)
    case class B(b: String)
    case class C(c: Boolean, d: Int)

    type ABC = A :+: B :+: C :+: CNil
    trait TC[A] {
      def foo: String
    }

    case class A1(a: Int)
    case class B1(b: String)
    case class C1(c: Boolean, d: Int)
    type ABC1 = A1 :+: B1 :+: C1 :+: CNil

    implicit val abc = new TC[ABC] {
      override def foo: String = "hello"
    }

    implicit val abc1 = new TC[ABC1] {
      override def foo: String = "world"
    }

    def derived[A <: Coproduct](implicit T: TC[A]) = T.foo
    println(implicitly[TC[ABC]].foo)
    println(implicitly[TC[ABC1]].foo)
    println(derived[ABC])

  }
}
