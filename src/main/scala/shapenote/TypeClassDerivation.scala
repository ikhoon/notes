package shapenote

import shapeless.{::, HList, HNil, ProductTypeClass, ProductTypeClassCompanion}

/**
 * Created by Liam.M(엄익훈) on 8/20/16.
 */

trait Monoid[T] {
  def zero: T
  def append(a: T, b: T): T
}

object Monoid extends ProductTypeClassCompanion[Monoid] {
  def mzero[T](implicit mt: Monoid[T]): T = mt.zero

  implicit def booleanMonoid: Monoid[Boolean] = new Monoid[Boolean] {
    override def zero: Boolean = false
    override def append(a: Boolean, b: Boolean): Boolean = a || b
  }

  implicit def intMonoid: Monoid[Int] = new Monoid[Int] {
    override def zero: Int = 0
    override def append(a: Int, b: Int): Int = a + b
  }

  implicit def stringMonoid: Monoid[String] = new Monoid[String] {
    override def zero: String = ""
    override def append(a: String, b: String): String = a + b
  }

  implicit def doubleMonoid: Monoid[Double] = new Monoid[Double] {
    override def zero: Double = 0.0
    override def append(a: Double, b: Double): Double = a + b
  }

  object typeClass extends ProductTypeClass[Monoid] {
    override def emptyProduct: Monoid[HNil] = new Monoid[HNil] {
      override def zero: HNil = HNil
      override def append(a: HNil, b: HNil): HNil = HNil
    }
    override def product[H, T <: HList](ch: Monoid[H], ct: Monoid[T]): Monoid[H :: T] = new Monoid[H :: T] {
      override def append(a: H :: T, b: H :: T): H :: T = ch.append(a.head, b.head) :: ct.append(a.tail, b.tail)
      override def zero: H :: T = ch.zero :: ct.zero
    }

    override def project[F, G](instance: => Monoid[G], to: (F) => G, from: (G) => F): Monoid[F] = new Monoid[F] {
      override def zero: F = from(instance.zero)
      override def append(a: F, b: F): F = from(instance.append(to(a), to(b)))
    }
  }
}

trait MonoidSyntax[T] {
  def |+|(b: T): T
}

object MonoidSyntax {
  implicit def monoidSyntax[T](a: T)(implicit mt: Monoid[T]): MonoidSyntax[T] = new MonoidSyntax[T] {
    override def |+|(b: T): T = mt.append(a, b)
  }
}
