package shapeguide
package test

import shapeless.{:+:, ::, CNil, Coproduct, Generic, HList, HNil, Inl, Inr}

/**
  * Created by ikhoon on 10/21/16.
  */
sealed trait Shape
final case class Rectangle(width: Double, height: Double) extends Shape
final case class Circle(radius: Double) extends Shape

object encoders extends App {
  trait CsvEncoder[A] {
    def encode(value: A): List[String]
  }
  def writeCsv[A](values: List[A])(implicit encoder: CsvEncoder[A]): String =
    values.map(value => encoder.encode(value).mkString(",")).mkString("\n")

  // for product - hlist
  implicit val hnilEncoder = new CsvEncoder[HNil] {
    def encode(value: HNil): List[String] = Nil
  }

  implicit def hlistEncoder[H, T <: HList](
    implicit
      hEncoder: CsvEncoder[H],
      tEncoder: CsvEncoder[T]
  ): CsvEncoder[H :: T] = new CsvEncoder[H :: T] {
    def encode(value: H :: T): List[String] =
      value match {
        case h :: t => hEncoder.encode(h) ++ tEncoder.encode(t)
      }
  }

  implicit def genericEncoder[A, R](
    implicit
      gen: Generic[A] { type Repr = R },
      enc: CsvEncoder[R]
  ): CsvEncoder[A] = new CsvEncoder[A] {
    def encode(value: A): List[String] = {
      enc.encode(gen.to(value))
    }
  }

  implicit val doubleEncoder: CsvEncoder[Double] = new CsvEncoder[Double] {
    def encode(value: Double): List[String] = List(value.toString)
  }

  println(writeCsv(List(Rectangle(3.0, 4.0))))

  val shapes: List[Shape] = List(
    Rectangle(3.0, 4.0),
    Circle(1.0)
  )

  // coproduct
  implicit val cnilEncoder: CsvEncoder[CNil] = new CsvEncoder[CNil] {
    def encode(value: CNil): List[String] = throw new Exception("Can't reach here!")
  }
  implicit def cproductEncoder[H, T <: Coproduct](
    implicit
      hEncoder: CsvEncoder[H],
      tEncoder: CsvEncoder[T]
  ): CsvEncoder[H :+: T] = new CsvEncoder[H :+: T] {
    def encode(value: H :+: T): List[String] = value match {
      case Inl(h) => hEncoder.encode(h)
      case Inr(t) => tEncoder.encode(t)
    }
  }



  println(writeCsv(shapes))

}
