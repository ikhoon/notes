package shapenote

import shapeless._

/**
  * Created by ikhoon on 06/08/2017.
  */


trait Nat {
  type N <: Nat
}

class Zero extends Nat {
  type N = Zero

  override def toString: String = "Zero"
}
case class Succ[P <: Nat]() extends Nat {
  type N = Succ[P]
}

object Nat {
//  type Zero = shapenote.Zero
  val Zero : Zero = new Zero

}
sealed trait ToInt[N <: Nat] {
  val value : Int
}
object ToInt {

  implicit def base: ToInt[Zero] = new ToInt[Zero] {
    val value: Int = 0
  }
  implicit def succ[N <: Nat]
    (implicit N: ToInt[N]): ToInt[Succ[N]] =
    new ToInt[Succ[N]]{
      val value: Int = N.value + 1
    }
}

trait Nther[L <: HList, N <: Nat] {
  type Out
  def apply(self: L): Out
}
object Nther {
  type Aux[L <: HList, N <: Nat, Out0] = Nther[L, N] { type Out = Out0 }

  implicit def base[H, L <: HList]: Aux[H :: L, Zero, H] =
    new Nther[H :: L, Zero] {
      type Out = H
      def apply(self: H :: L): Out = self.head
    }

  implicit def corecurse[H, L <: HList, N <: Nat]
    (implicit N: Nther[L, N]) : Aux[H :: L, Succ[N], N.Out] =
    new Nther[H :: L, Succ[N]] {
      type Out = N.Out
      def apply(self : H :: L) = N(self.tail)
    }

}

object Nth {

  def toInt[N <: Nat](implicit N: ToInt[N]): Int = N.value
  implicit def fromInt(i: Int): Nat = macro macronote.NatMacros.materialize


  implicit class NthSyntax[L <: HList](self: L) {
    def nth[N <: Nat](implicit N: Nther[L, N]): N.Out = N(self)
    def nth(n: Nat)(implicit N: Nther[L, n.N]): N.Out = N(self)
  }
}


object NthTest  {
  import Nth._
  import Nat.Zero
  def main(args: Array[String]): Unit = {
    type five = Succ[Succ[Succ[Succ[Succ[Zero]]]]]
    println(Nth.toInt[five])
    assert(Nth.toInt[five] == 5)
    println(Nth.fromInt(10))
    val four : Nat = 4
    val list = 1 :: "hello" :: true :: 10.1 :: Zero :: four :: HNil

    println(list.nth(Nth.fromInt(4)))
    println(list.nth(5))
    println(list.at(5))
  }
}
