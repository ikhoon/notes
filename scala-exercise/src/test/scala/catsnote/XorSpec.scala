package catsnote

import org.scalatest.{Matchers, WordSpec}
import cats._
import cats.implicits._

import cats.syntax.either._
/**
  * Created by ikhoon on 2016. 8. 1..
  */
class XorSpec extends WordSpec with Matchers {

  /*
  "xor" should {

    "why not either" in {
      val e1 : Either[String, Int] = Right(5)
      val e2 : Either[String, Int] = Left("hello")

      // map과 flatMap같은 함수를 Either에서 바로 사용할수 없다.
      // e1.flatMap
      // e1.map
      e1.right.map(_ + 1) shouldBe Right(6)
      e2.left.map(_ + " world") shouldBe Left("hello world")

      type Xor[A, B] = Either[A, B]
      // 하지만 Either에서는 바로 사용할수 있다.
      val x1 : Either[String, Int] = Either.right(5)
      val x2 : Either[String, Int] = Either.left("hello")

      x1.map(_ + 1) shouldBe Either.right(6)

      // right에 관한 값만 map의 대상이 된다. 왼쪽값은 영향을 받지 않는다.
      x2.map(_ + 1) shouldBe Either.left("hello")

      // 조금더 직관적으로 타입을 표현할수 있다.
      val x3 : String Either Int = Either.right(10)
      val x4 : String Either Int = Either.left("world")

      x3 shouldBe Either.right(10)
      x4 shouldBe Either.left("world")
    }

    "right-biased, it is possible to define monad instance" in {
      val x1 : String Either Int = Either.right(10)
      x1.flatMap(x => Either.right(x + 10)) shouldBe Either.right(20)
    }

    "scala infix type" in {
      // 여기서 잠깐 String Xor Int란 표현이 신기하다.
      // 어떻게 되는걸까?
      // 참조 1. : http://stackoverflow.com/questions/3200380/whats-the-name-of-this-scala-infix-syntax-for-specifying-type-params
      // 참조 2. : http://jim-mcbeath.blogspot.kr/2008/11/scala-type-infix-operators.html
      // 두개의 타입 파라메터를 가지는 타입을 만들어 보자

      case class Plus[T, U](t: T, u: U)

      val a : Plus[String, Int] = Plus[String, Int]("hello", 10)
      // 잘된다. 오호 신기..
      val b : String Plus Int = Plus[String, Int]("world", 20)
      a shouldBe Plus("hello", 10)
      b shouldBe Plus("world", 20)
    }

    // 에러를 만나면 throw 한다.
    // 늘하던 방식이다.
    object ExceptionStyle {
      def parse(s: String): Int =
        if (s.matches("-?[0-9]+")) s.toInt
        else throw new NumberFormatException(s"${s} is not a valid integer.")

      def reciprocal(i: Int): Double =
        if (i == 0) throw new IllegalArgumentException("Cannot take reciprocal of 0.")
        else 1.0 / i

      def stringify(d: Double): String = d.toString

    }

    // Xor를 이용하여 에러에 대해서는 left에 저장한다.
    object XorStyle {
      def parse(s: String): Either[NumberFormatException, Int] =
        if (s.matches("-?[0-9]+")) Either.right(s.toInt)
        else Either.left(new NumberFormatException(s"${s} is not a valid integer."))

      def reciprocal(i: Int): Either[IllegalArgumentException, Double] =
        if (i == 0) Either.left(new IllegalArgumentException("Cannot take reciprocal of 0."))
        else Either.right(1.0 / i)

      def stringify(d: Double): String = d.toString

      // 그러면 마술이 일어난다.
      // 에러핸들링을 아주 멋지게 할수 있다.
      def magic(s: String): Either[Exception, String] =
        parse(s).flatMap(reciprocal).map(stringify)

    }

    "using xor instead of exceptions" in {
      XorStyle.parse("Not a number").isRight shouldBe false
      XorStyle.parse("2").isRight shouldBe true

      XorStyle.magic("0").isRight shouldBe false
      XorStyle.magic("1").isRight shouldBe true
      XorStyle.magic("Not a number").isRight shouldBe false
    }

    "pattern matching" in {
      import XorStyle._

      val result = magic("2") match {
        case Either.left(_: NumberFormatException) ⇒ "Not a number!"
        case Either.left(_: IllegalArgumentException) ⇒ "Can't take reciprocal of 0!"
        case Either.left(_) ⇒ "Unknown error"
        case Either.right(r) ⇒ s"Got reciprocal: $r"
      }
      result shouldBe "Got reciprocal: 0.5"
    }

    object XorStyleWithAdts {
      sealed abstract class Error
      final case class NotANumber(string: String) extends Error
      case object NoZeroReciprocal extends Error

      def parse(s: String): Either[Error, Int] =
        if (s.matches("-?[0-9]+")) Either.right(s.toInt)
        else Either.left(NotANumber(s))

      def reciprocal(i: Int): Either[Error, Double] =
        if (i == 0) Either.left(NoZeroReciprocal)
        else Either.right(1.0 / i)

      def stringify(d: Double): String = d.toString

      def magic(s: String): Either[Error, String] =
        parse(s).flatMap(reciprocal).map(stringify)
    }

    "instead of using exceptions as our error value" in {
      import XorStyleWithAdts._

      val result = magic("2") match {
        case Either.left(NotANumber(_)) ⇒ "Not a number!"
        case Either.left(NoZeroReciprocal) ⇒ "Can't take reciprocal of 0!"
        case Either.right(r) ⇒ s"Got reciprocal: ${r}"
      }
      result shouldBe "Got reciprocal: 0.5"
    }

    "leftMap and map" in {
      val right: String Either Int = Either.right(41)
      right.map(_ + 1) shouldBe Either.right(42)

      val left: String Either Int = Either.left("Hello")
      left.map(_ + 1) shouldBe Either.left("Hello")
      left.leftMap(_.reverse) shouldBe Either.left("olleH")
    }

    "catch only" in {
      // error 핸들링을 Xor를 이용해서 아래와 같이 할수 있다. 하지만 길다.
      val xor: Either[NumberFormatException, Int] =
        try {
          Either.right("abc".toInt)
        } catch {
          case nfe: NumberFormatException => Either.left(nfe)
        }

      // 더 간단히 해보자.
      // NumberFormatException만 catch 하자
      val xor1: Either[NumberFormatException, Int] = Either.catchOnly[NumberFormatException]("abc".toInt)

      // 이것도 귀찮은면 fatal만 제외한 에러는 다 잡자.
      val xor2: Either[Throwable, Int] = Either.catchNonFatal(1 / 0)

      xor1.isLeft shouldBe true
      xor2.isLeft shouldBe true

    }
  }
  */

}
