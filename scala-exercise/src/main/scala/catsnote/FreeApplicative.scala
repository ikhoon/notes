package catsnote

import cats.free.{Free, FreeApplicative}

/**
  * Created by ikhoon on 04/08/2017.
  * http://typelevel.org/cats/datatypes/freeapplicative.html
  */

// Free Applicative는 Free(monad)와 유사하다.
// 연산을 데이터로 표현하고 내부 DSL을 만드는데 유용하다.
// Applicative와 Monad가 구분되어 있는것 처럼

class freeapplicative {

  val a: PartialFunction[Int, Boolean] = { case 1 => true }
  val b: PartialFunction[String, Boolean] = { case "a" => false }
  val c = a.orElse(b)

  // 우선 ADT를 만들자

  sealed abstract class ValidationOps[A]
  case class Size(size: Int) extends ValidationOps[Boolean]
  case object HasNumber extends ValidationOps[Boolean]

  // 이번엔 smart constructors
  type Validation[A] = FreeApplicative[ValidationOps, A]
  def size(n: Int): Validation[Boolean] =
    FreeApplicative.lift(Size(n))



}
