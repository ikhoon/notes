package shapenote


import shapeless._

/**
  * Created by ikhoon on 06/08/2017.
  */
trait Poly { self =>

  def apply[A, B](a: A)(implicit C: this.Case[A, B]): B = C(a)
  def at[A] = new {
    def apply[B](f: A => B): Case[A, B] = new Case[A, B] {
      def apply(a: A): B = f(a)
    }
  }

  sealed trait Case[A, B] {
    def apply(a: A): B
  }
}

object PolyTest {
  def main(args: Array[String]): Unit = {

      object square extends Poly {
        implicit val int = at[Int] { i => i * i }
        implicit val str = at[String] { s => s + s }
      }

    println(square(10))
    println(square("hello"))

  }
}

trait Mapper[L <: HList, P <: Poly] {
  type Out <: HList

  def apply(xs: L): Out
}

object Mapper {
  type Aux[L <: HList, P <: Poly, Out0 <: HList] = Mapper[L, P] { type Out = Out0 }

  implicit def base[P <: Poly]: Aux[HNil, P, HNil] = new Mapper[HNil, P] {
    type Out = HNil
    def apply(xs: HNil): Out = xs
  }

  implicit def corecurse[A, B, L <: HList, P <: Poly]
    (implicit M: Mapper[L, P], C: P#Case[A, B]): Aux[A :: L, P, B :: M.Out] =
    new Mapper[A :: L, P] {
      type Out = B :: M.Out
      def apply(xs: A :: L): B :: M.Out = C.apply(xs.head) :: M.apply(xs.tail)
    }
}

object RichMapper {
  implicit class MapperSyntax[L <: HList](self: L) {
    def mapping[P <: Poly](p: P)(implicit M: Mapper[L, P]): M.Out = M.apply(self)
  }
}

object RichMapperTest {
  def main(args: Array[String]): Unit = {
    object square extends Poly {
      implicit val int = at[Int] { i => i * i }
      implicit val str = at[String] { s => s + s }
    }
    import RichMapper._
    val hlist = 10 :: "hello" :: HNil

    println(hlist.mapping(square))

  }
}
