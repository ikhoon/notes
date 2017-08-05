package shapenote

import shapeless._
import Remover.Aux
/**
  * Created by ikhoon on 05/08/2017.
  */


trait Remover[A, L <: HList] {
  type Out <: HList
  def apply(xs: L): Out
}

trait RemoverLowPriorityImplicits {
  implicit def hnilRemove[A]: Aux[A, A :: HNil, HNil] =
    new Remover[A, A :: HNil] {
      type Out = HNil
      def apply(xs: A :: HNil): Out = HNil
    }

  implicit def base[A, L <: HList] : Aux[A, A :: L, L] =
    new Remover[A, A :: L] {
      type Out = L
      def apply(xs: A :: L): Out = xs.tail
    }
}

object RichRemover {
  implicit class RemoverSyntax[L <: HList](self: L) {
    def remove[A](implicit R: Remover[A, L]): R.Out = R(self)
  }
}


object Remover extends RemoverLowPriorityImplicits {
  type Aux[A, L <: HList, Out0 <: HList] = Remover[A, L] { type Out = Out0 }

//  def apply[A, L <: HList](list: L)(implicit R: Remover[A, L]): R.Out = R(list)

  def corecurseRemove[A, L <: HList]
    (implicit R: Remover[A, L]): Aux[A, A :: L, R.Out] =
    new Remover[A, A :: L] {
      type Out = R.Out
      def apply(xs : A :: L): R.Out = R(xs.tail)
    }

  implicit def corecurseRebuild[A, B, L <: HList]
    (implicit R: Remover[A, L]): Aux[A, B :: L, B :: R.Out] = {
      new Remover[A, B :: L] {
        type Out = B :: R.Out

        def apply(xs: B :: L) = xs.head :: R(xs.tail)
      }
  }
}

object RemoveTest {

  import RichRemover._
  def main(args: Array[String]): Unit = {
    val hlist = 1 :: "hello" :: true :: 10 :: HNil


    println(hlist.remove[Int])
    println(hlist.remove[String])
    println(hlist.remove[Boolean])
    println(hlist.removeElem[Int])
    println(hlist.removeAll[Int :: HNil])

  }
}

