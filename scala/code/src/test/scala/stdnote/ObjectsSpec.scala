package stdnote

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

/**
  * Created by Liam.M(엄익훈) on 8/1/16.
  */
class ObjectsSpec extends AnyFunSuite with Matchers {

  // singleton 객체의 인스턴스가 하나이다.
  // + object, - static
  object Greeting {
    def english = "Hi"

    def espanol = "Hola"

    def deutsch = "Hallo"

    def magyar = "Szia"
  }

  test("what is object") {
    Greeting.english shouldBe "Hi"
    Greeting.espanol shouldBe "Hola"
    Greeting.deutsch shouldBe "Hallo"
    Greeting.magyar shouldBe "Szia"
  }

  test("object is singleton") {
    val x = Greeting
    val y = x

    // == 자바(레퍼런스를 비교), 값을 비교합니다.
    // equals 자바에서 사용하는 값비교
    // eq 레퍼런스 비
    x eq y shouldBe true
    val z = Greeting

    x eq z shouldBe true

    class Foo

    val a = new Foo

    val b = new Foo

    a eq b shouldBe false

  }

  test("companion object") {
    class Movie(val name: String, val year: Short)

    object Movie {
      def apply(year: Short, name: String): Movie = new Movie(name, year)
    }

    Movie(1992, "강시") // new Movie("강시", 1992)
  }

  test("companion object can access private value") {
    class Person(val name: String, private val superheroName: String) //The superhero name is private!

    object Person {
      def showMeInnerSecret(x: Person) = x.superheroName
    }

    val clark = new Person("Clark Kent", "Superman")
    val peter = new Person("Peter Parker", "Spiderman")
    val bruce = new Person("Bruce Wayne", "Batman")
    val diana = new Person("Diana Prince", "Wonder Woman")

    Person.showMeInnerSecret(clark) shouldBe "Superman"
    Person.showMeInnerSecret(peter) shouldBe "Spiderman"
    Person.showMeInnerSecret(bruce) shouldBe "Batman"
    Person.showMeInnerSecret(diana) shouldBe "Wonder Woman"
  }
}
