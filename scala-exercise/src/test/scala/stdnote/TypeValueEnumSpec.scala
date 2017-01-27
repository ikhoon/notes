package stdnote

import org.scalatest.{Matchers, WordSpec}
import cats.instances.all._
import cats.syntax.all._

/**
  * Created by liam on 24/01/2017.
  */
class TypeValueEnumSpec extends WordSpec with Matchers {

  "enums" should {
    "type parameter" in {
      case class Foo(code: Int, name: Option[String], description: Option[String]) extends TypeValue[Int]
      object Foos extends TypeValueEnum[Int] {
        val _1 = Enum(Foo(1, "a".some, "b".some))
      }
      val foo: TypeValue[Int] = Foos._1
    }

    "type member" in {
      object Foos extends TypeValueEnumWithMember.Aux[Int] {
        type Code = Int

        sealed abstract class EnumVal(code: Int, name: Option[String], description: Option[String]) extends TypeValueWithMember.Aux[Int]
        val _1 = Enum(new EnumVal(1, "a".some, "b".some))
      }
    }

  }

}
