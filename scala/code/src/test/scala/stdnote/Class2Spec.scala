package stdnote

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

/**
  * Created by ikhoon on 2017. 3. 13..
  */
class Class2Spec extends AnyFunSuite with Matchers {

  test("class style") {
    //Typical java style class
    class Person {
      var name = "Noname"
      var age = -1

      def setName(name: String) {
        this.name = name
      }

      def setAge(age: Int) {
        this.age = age
      }

      def getName(): String = {
        name
      }

      def getAge(): Int = {
        age
      }

    }

    val p1 = new Person
    p1.getAge shouldBe -1
    p1.setAge(20)
    p1.getAge shouldBe 20
    // getter, setter, 기본값
    // 왜 public 이 기본일까?
    class Person2 private (
      var name: String = "Noname",
      var age: Int = -1
    )
    object Person2 {
      val p2 = new Person2
    }
    val p2 = Person2.p2

    p2.age shouldBe -1
    p2.age = 20
    p2.age shouldBe 20

    // val = getter
    // var = getter & setter

    class Person3(val name: String, var age: Int = 10)
    class Person6(val name: String, var age: Int = 10) {
      def value: String = s"name = $name, age = $age"
      private def value2: String = s"name = $name, age = $age"
    }

    val p6 = new Person6(name = "abc")

    val p3 = new Person3(name = "abc")
    println(p3.name) // 컴파일 안됨요
    p3.age = 10
    case class Person4(name: String = "Noname", age: Int = -1)
    val p4 = Person4()
    p4.name

    class Person5(val name: String, var age: Int)(implicit val key: Int, val key2: String)

  }

}
