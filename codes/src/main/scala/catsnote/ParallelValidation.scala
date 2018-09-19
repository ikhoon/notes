package catsnote

import cats.Semigroup
import cats.data.Validated
import cats.data.Validated.{Invalid, Valid}

/**
  * Created by ikhoon on 2016. 8. 2..
  */

case class ConnectionParams(url: String, port: Int)

trait Read[A] {
  def read(s: String): Option[A]
}

object Read {
  def apply[A](implicit A: Read[A]) : Read[A] = A

  implicit val intRead = new Read[Int] {
    override def read(s: String): Option[Int] = if (s.matches("-?[0-9]+")) Some(s.toInt) else None
  }

  implicit val stringRead = new Read[String] {
    override def read(s: String): Option[String] = Some(s)
  }
}

sealed abstract class ConfigError
final case class MissingConfig(field: String) extends ConfigError
final case class ParseError(field: String) extends ConfigError

case class Config(map: Map[String, String]) {
  def parse[A: Read](key: String) : Validated[ConfigError, A] = {
    map.get(key) match {
      case None => Invalid(MissingConfig(key))
      case Some(value) => Read[A].read(value) match {
        case None => Invalid(ParseError(key))
        case Some(a) => Valid(a)
      }
    }
  }
}


object ParallelValidation {
  def parallelValidate[E: Semigroup, A, B, C](v1: Validated[E, A], v2: Validated[E, B])(f: (A, B) => C): Validated[E, C] = {
    (v1, v2) match {
      case (Valid(a), Valid(b)) => Valid(f(a, b))
      case (Valid(_), i @ Invalid(_)) => i
      case (i @ Invalid(_), Valid(_)) => i
      case (Invalid(e1), Invalid(e2)) => Invalid(Semigroup[E].combine(e1, e2))
    }
  }


}
