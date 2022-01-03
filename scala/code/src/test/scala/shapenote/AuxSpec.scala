package shapenote

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

/**
class AuxSpec extends AnyWordSpec with Matchers{

  // http://gigiigig.github.io/posts/2015/09/13/aux-pattern.html

  "aux" should {

    "return type" in {
      // 예를 들어보자
      // 바꾸는 함수가 있다 하자

      // TODO 여기를 바꾸어야합니다.
      trait Converter[A, B] {
        def convert(a: A): B
      }

      //
      implicit val intConverter = new Converter[Int, String] {
        override def convert(a: Int): String = (a * 1000).toString
      }

      // reutrn 타입은 Converter에 의존성이 있는데 외부로 노출할 필요가 있는가?
//      def foo[A](a: A)(implicit cv: Converter[A]): cv.B = cv.convert(a)

//      println(foo(10))

    }

    "return type 2" in {
      trait Converter[A] {
        type B
        def convert(a: A): B
      }

      implicit val doubleConverter = new Converter[Int] {
        type B = Double
        override def convert(a: Int): B = (a * 4000).toDouble
      }

      // API가 간결해졌다.
      // 그리고 return 타입을 주입 받을수 있다
      def foo[A](a: A)(implicit cv: Converter[A]): cv.B = cv.convert(a)

      val a: Double = foo(10)
      println(a)

    }

    // 그러나
    // implicit 의 타입의 type member 다른 타입의 type parameter 의 input이 될수 없다.
    "chaining implict" in {
      trait Converter[A] {
        type B
        def convert(a: A): B
      }

      trait Encoder[F] {
        type T
        def encode(a: F): T
      }

      implicit val intConverter = new Converter[Int] {
        type B = String
        override def convert(a: Int): String = (a * 1000).toString
      }

      implicit val stringEncoder = new Encoder[String] {
        type T = Double
        override def encode(a: String): Double = a.toDouble
      }

//      // 안됨요
      def foo[A, B0](a: A)(
        implicit
          cv: Converter[A] { type B = B0 },
          enc: Encoder[B0]
      ): enc.T =
        enc.encode(cv.convert(a))
//
//      val a: Double = foo[Int](10)
//      println(a)

    }

    "chaining implicit with aux" in {
      // 두번 변화시키고 싶다.
      // A => B => C
      trait Converter[A] {
        type B
        def convert(a: A): B
      }
      object Converter {
        type Aux[A, B0] = Converter[A] { type B = B0 }
      }

      trait Encoder[F] {
        type T
        def encode(a: F): T
      }

      object Encoder {
        type Aux[F, T0] = Encoder[F] { type T = T0 }
      }

      implicit val intConverter = new Converter[Int] {
        type B = String
        override def convert(a: Int): String = (a * 1000).toString
      }

      implicit val stringEncoder = new Encoder[String] {
        type T = Double
        override def encode(a: String): Double = a.toDouble + 12345
      }

      def foo[A, B0](a: A)(
        implicit
        cv: Converter[A] { type B = B0 },
        enc: Encoder[B0]
      ): enc.T =
        enc.encode(cv.convert(a))

      // 안됨요
//      def foo[A, B](a: A)(
//        implicit
//          cv: Converter.Aux[A, B],
//          enc: Encoder[B]): enc.T = enc.encode(cv.convert(a))

//      val a = foo(10)
//      println(a)
    }

  }
}
  */
