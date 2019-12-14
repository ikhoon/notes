import cats.data.Validated.Valid
import cats.data.{Nested, Validated}
import cats.implicits._
val nested: Nested[Option, Validated[String, *], Int] = Nested(Some(Valid(123)))
nested.map(_.toString).value
