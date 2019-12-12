package fpinsnote

import cats.data.{Nested, Validated, ValidatedNel}
import cats.data.Validated.Valid
import org.scalatest.{Matchers, WordSpec}

/**
  * Created by ikhoon on 2017. 1. 24..
  */

sealed trait MyList[+A]
case object MyNil extends MyList[Nothing]
case class Cons[A](head: A, tail: MyList[A]) extends MyList[A]


trait A
trait B extends A

trait Covariant[+A]
trait Invariant[A]
trait Contravariant[-A]

class ListSpec extends WordSpec with Matchers {

  // Any >> Nothing

//  "list" should {
//    "a" in {
//
//      // B는 A의 하위 타입이다.
//      // Invariant[B]가 Invariant[A]의 하위 타입일까? no.
////      val c: Invariant[A] = new Invariant[B] {}
//`
//      // MyList[+A]
//      // Nothing는 Int의 하위 타입이다.
//      // MyList[Nothing]는 MyList[Int]의 하위 타입일까? yes.
//      val myIntList: MyList[Int] = new MyList[Nothing] {}
//
//      // MyList[-A]
//      // Any는 Int의 상위 타입이다.
//      // MyList[Any]는 MyList[Int]의 상위 타입일까? yes.
//      val myIntList2 = new MyList[Any] {}
//    }
//  }
  import cats.implicits._

  private val value1: Nested[Option, ValidatedNel[Throwable, *], Int] = Nested(Some(Valid(123)))
  value1.map(_ + 1)
}

