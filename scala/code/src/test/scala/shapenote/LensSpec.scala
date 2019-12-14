package shapenote

import org.scalatest.{Matchers, WordSpec}

/**
  * Created by ikhoon on 2016. 8. 17..
  */
class LensSpec extends WordSpec with Matchers {

  "Boilerplate-free lenses for arbitrary case class" should {

    import shapeless.lens

    case class Address(street: String, city: String, postcode: String)
    case class Person(name: String, age: Int, address: Address)

    // some lenses for person/address

    val nameLens = lens[Person] >> 'name
    val ageLens = lens[Person] >> 'age
    val addressLens = lens[Person] >> 'address

    val streetLens = lens[Person] >> 'address >> 'street
    val cityLens = lens[Person] >> 'address >> 'city
    val postcodeLens = lens[Person] >> 'address >> 'postcode

    val person = Person("Joe Grey", 37, Address("Southover Street", "Brighton", "BN2 9UA"))

    "Read a field" in {
      ageLens.get(person) shouldBe 37
    }

    "Update a field" in {
      val updatedPerson = ageLens.set(person)(38)
      updatedPerson.age shouldBe 38
    }

    "Transform a field" in {
      val updatedPerson = ageLens.modify(person)(_ + 1)
      updatedPerson.age shouldBe 38
    }

    "Read a nested field" in {
      streetLens.get(person) shouldBe "Southover Street"
    }

    "Update a nested field" in {
      val updatedPerson: Person = streetLens.set(person)("Montpelier Road")
      updatedPerson.address.street shouldBe "Montpelier Road"
    }
  }

}
