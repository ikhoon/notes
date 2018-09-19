package stdnote

import org.scalatest.{FunSuite, Matchers}

/**
 * Created by Liam.M(엄익훈) on 8/1/16.
 */
class OptionSpec extends FunSuite with Matchers {

  test("optional") {
    // Optional
    val someValue : Option[String] = Some("I'm wrapped in something.")
    someValue shouldBe Some("I'm wrapped in something.")
    val noneValue : Option[String] = None
    noneValue shouldBe None
  }

  def maybeItWillReturnSomething(flag: Boolean): Option[String] = {
    if (flag) Some("Found value") else None
  }

  val value1 = maybeItWillReturnSomething(true)
  val value2 = maybeItWillReturnSomething(false)

  test("default value") {

    // Some(123) => getOrElse(0) => 123
    // None => getOrElse(0) => 0
    value1 getOrElse "No value" shouldBe "Found value"
    value2 getOrElse "No value" shouldBe "No value"

    value2.getOrElse {
      "default function"
    } shouldBe "default function"

  }

  test("empty?") {

    value1.isEmpty shouldBe false
    value2.isEmpty shouldBe true
  }

  // 패턴매칭?!!! 아주 중요하다.
  test("pattern matching") {
    val someValue: Option[Double] = Some(20.0) // Some[Double]

    val value = someValue match {
      case Some(v) ⇒ v
      case None ⇒ 0.0
    }

    val value2 = someValue.getOrElse(0.0)

    value shouldBe 20.0
    val noValue: Option[Double] = None
    val value1 = noValue match {
      case Some(v) ⇒ v
      case None ⇒ 0.0
    }
    value1 shouldBe 0.0
  }

  // 내부의 변화가 필요할때
  // 이것도 아주 중요
  test("map") {
    val number: Option[Int] = Some(3)
    val noNumber: Option[Int] = None
    val result1 = number.map(_ * 1.5)
    val result2 = noNumber.map(_ * 1.5)

    // ---------                     ------------
    // |   3   |  -- map(* 1.5) --> |    4.5    |
    // ---------                    ------------
    result1 shouldBe Some(4.5)
    result2 shouldBe None
  }

  // fold 이건 나중에 알아보자. 지금은 맛만
  // 하지만 함수형 언어의 기본
  test("fold") {
    val number: Option[Int] = Some(3)
    val noNumber: Option[Int] = None
    val result1 = number.fold(0)(_ * 3)
    val result2 = noNumber.fold(0)(_ * 3)

    result1 shouldBe 9
    result2 shouldBe 0
  }





}
