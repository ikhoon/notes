package shapenote


import org.scalatest.{Matchers, WordSpec}
import shapeless._
import CovariantHelper._


/**
  * Created by ikhoon on 2016. 8. 8..
  */
class HeterogenousListSpec extends WordSpec with Matchers {

  "Heterogenous List" should {
    "map" in {
      import poly._
      object choose extends (Set ~> Option) {
        override def apply[T](f: Set[T]): Option[T] = f.headOption
      }

      val sets = Set(1) :: Set("foo") :: Set(true) :: HNil

      val opts = sets map choose

      opts shouldBe (Option(1) :: Option("foo") :: Option(true) :: HNil)

    }

    "flatMap" in {
      import shapeless.poly.identity
      val l = (1 :: "foo" :: HNil) :: HNil :: (true :: HNil) :: HNil
      l.flatMap(identity) shouldBe 1 :: "foo" :: true :: HNil
    }

    "fold" in {

      val l = 23 :: "foo" :: (13, "wibble") :: HNil
      //      FIXME not work :-(
//      l.foldLeft(0)(addSize)
    }
    "zipper" in {
      import shapeless.syntax.zipper._
      val l = 1 :: "foo" :: 3.0 :: HNil
      (l.toZipper)
      println(l.toZipper.right)
      // put == replace
      println(l.toZipper.right.put(("wibble", 45)))
      println(l.toZipper.right.insert(("wibble", 45)))
      l.toZipper.right.put(("wibble", 45)).reify shouldBe 1 :: ("wibble", 45) :: 3.0 :: HNil

      l.toZipper.right.delete.reify shouldBe 1 :: 3.0 :: HNil
    }

    "covariant" in {
      import CovariantHelper._
      import scala.reflect.runtime.universe._

      implicitly[TypeTag[APAP]].tpe.typeConstructor <:< typeOf[FFFF] shouldBe true

    }

    "instance of" in {
      apap.isInstanceOf[FFFF] shouldBe true
      apap.unify.isInstanceOf[FFFF] shouldBe true
    }

    "to list" in {
      apap.toList shouldBe List(Apple(), Pear(), Apple(), Pear())
    }

    "typable" in {
      import syntax.typeable._
      val ffff: FFFF = apap.unify

      val precise : Option[APAP] = ffff.cast[APAP]
      precise shouldBe Some(apap)
    }
  }

}
