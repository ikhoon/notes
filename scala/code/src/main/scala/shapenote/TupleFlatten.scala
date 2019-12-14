package shapenote

import shapeless.{Lazy, Poly1}
import shapeless.ops.tuple.{FlatMapper}


/**
  * Created by Liam.M on 2017. 12. 28..
  * codes in shapeless
  * https://github.com/milessabin/shapeless/blob/master/examples/src/main/scala/shapeless/examples/flatten.scala#L70
  */

object TupleFlatten {

  trait LowPriorityFlatten extends Poly1 {
    implicit def default[T] = at[T](Tuple1(_))
  }
  object flatten extends LowPriorityFlatten {
    implicit def caseTuple[P <: Product](implicit lfm: Lazy[FlatMapper[P, flatten.type]]) =
      at[P](lfm.value(_))
  }

  case class Foo(a: Int, b: String)

  def main(args: Array[String]): Unit = {
    val a = Foo(1, "a")
    val b = Foo(2, "b")
    val c = (a, b, (3, "c", (4)))
    println(flatten(c))


  }
}

/*
예전 버전
import shapeless._

trait Flatten[I, O <: HList] {
  def apply(i: I): O
}

trait FlattenLow {
  implicit def otherFlatten[I] = new Flatten[I, I :: HNil] {
    def apply(i: I) = i :: HNil
  }
}

object FlattenHigh extends FlattenLow {
  implicit object hnilFlatten extends Flatten[HNil, HNil] {
    def apply(i: HNil) = HNil
  }

  implicit def hlistFlatten[H, T <: HList, HO <: HList, TO <: HList, O <: HList](implicit
                                                                                 hev: Flatten[H, HO],
                                                                                 tev: Flatten[T, TO],
                                                                                 pre: Prepend[HO, TO, O]
                                                                                ) = new Flatten[H :: T, O] {
    def apply(i: H :: T) = pre(hev(i.head), tev(i.tail))
  }

  implicit def tupleFlatten[P <: Product, L <: HList, O <: HList](implicit
                                                                  lev: HListerAux[P, L],
                                                                  fev: Flatten[L, O]
                                                                 ) = new Flatten[P, O] {
    def apply(i: P) = fev(lev(i))
  }
}

*/
