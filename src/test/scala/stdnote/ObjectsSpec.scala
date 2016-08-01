package stdnote

import org.scalatest.{FunSuite, Matchers}

/**
 * Created by Liam.M(엄익훈) on 8/1/16.
 */
class ObjectsSpec extends FunSuite with Matchers {

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

    x eq y shouldBe true

    val z = Greeting

    x eq z shouldBe true

  }

  test("companion object") {
    class Movie(val name: String, val year: Short)

    object Movie {
      def academyAwardBestMoviesForYear(x: Short) = {
        //This is a match statement, more powerful than a Java switch statement!
        x match {
          case 1930 ⇒ Some(new Movie("All Quiet On the Western Front", 1930))
          case 1931 ⇒ Some(new Movie("Cimarron", 1931))
          case 1932 ⇒ Some(new Movie("Grand Hotel", 1932))
          case _ ⇒ None
        }
      }
    }

    Movie.academyAwardBestMoviesForYear(1932).get.name shouldBe "Grand Hotel"
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
