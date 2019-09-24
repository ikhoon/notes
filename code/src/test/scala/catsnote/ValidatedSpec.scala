package catsnote

import cats.{Monad, Semigroup, SemigroupK}
import cats.data.{NonEmptyList, Validated}
import org.scalatest.{Matchers, WordSpec}
import ParallelValidation._
import cats.data.Validated.{Invalid, Valid}
import cats.syntax.either._

/**
  * Created by ikhoon on 2016. 8. 2..
  */
class ValidatedSpec extends WordSpec with Matchers {


  "Validated" should {

    import cats.instances.list._
    implicit val intRead: Read[Int] = Read.intRead
    implicit val stringRead: Read[String] = Read.stringRead

    implicit val nelSemigroup = SemigroupK[NonEmptyList].algebra[ConfigError]

    "parallel validation" in {


      val config = Config(Map("url" -> "127.0.0.1", "port" -> "1337"))
      val valid = parallelValidate(
        config.parse[String]("url").toValidatedNel,
        config.parse[Int]("port").toValidatedNel
      )(ConnectionParams.apply)

      valid.isValid shouldBe true
      valid.getOrElse(ConnectionParams("", 0)) shouldBe ConnectionParams("127.0.0.1", 1337)

    }

    "invalid" in {

      val config = Config(Map("endpoint" -> "127.0.0.1", "port" -> "not a number"))

      val invalid: Validated[NonEmptyList[ConfigError], ConnectionParams] = parallelValidate(
        config.parse[String]("url").toValidatedNel,
        config.parse[Int]("port").toValidatedNel
      )(ConnectionParams.apply)

      invalid.isValid shouldBe false

      val errors = NonEmptyList.of(MissingConfig("url"), ParseError("port"))
      invalid == Validated.invalid(errors) shouldBe true
    }

    import cats.Applicative

    implicit def validatedApplicative[E: Semigroup] : Applicative[Validated[E, ?]] =
      new Applicative[Validated[E, ?]] {
        override def pure[A](x: A): Validated[E, A] = Valid(x)

        override def ap[A, B](ff: Validated[E, (A) => B])(fa: Validated[E, A]): Validated[E, B] =
          (ff, fa) match {
            case (Valid(f), Valid(x)) => Valid(f(x))
            case (Valid(_), i @ Invalid(_)) => i
            case (i @ Invalid(_), Valid(_)) => i
            case (Invalid(e1), Invalid(e2)) => Invalid(Semigroup[E].combine(e1, e2))
          }
      }

    val config = Config(Map(
      "name" -> "cat",
      "age" -> "not a number",
      "houseNumber" -> "1234",
      "lane" -> "feline street"
    ))

    "apply" in {

      import cats.Apply

      import cats.data.ValidatedNel
      implicit val nelSemigroup: Semigroup[NonEmptyList[ConfigError]] =
        SemigroupK[NonEmptyList].algebra[ConfigError]


      case class Address(houseNumber: Int, street: String)
      case class Person(name: String, age: Int, address: Address)

      val personFromConfig: ValidatedNel[ConfigError, Person] =
        Apply[ValidatedNel[ConfigError, ?]].map4(
          config.parse[String]("name").toValidatedNel,
          config.parse[Int]("age").toValidatedNel,
          config.parse[Int]("houseNumber").toValidatedNel,
          config.parse[String]("lane").toValidatedNel
        ){ case (name, age, houseNumber, lane) => Person(name, age, Address(houseNumber, lane)) }

      personFromConfig.isValid shouldBe false

    }


    "flatMap and Xor" in {
      implicit def validatedMonad[E]: Monad[Validated[E, ?]] = new Monad[Validated[E, ?]] {
        override def pure[A](x: A): Validated[E, A] = Valid(x)

        override def flatMap[A, B](fa: Validated[E, A])(f: (A) => Validated[E, B]): Validated[E, B] =
          fa match {
          case Valid(x) => f(x)
          case i @ Invalid(_) => i
        }

        override def tailRecM[A, B](a: A)(f: (A) => Validated[E, Either[A, B]]): Validated[E, B] = ???
      }

      val v: Validated[NonEmptyList[String], (Int, Double)] = validatedMonad
        .tuple2(Validated.invalidNel[String, Int]("oops"), Validated.invalidNel[String, Double]("uh oh"))
      v.isInvalid shouldBe true

    }

    "sequential validation - andThen 1" in {
      val houseNumber = config.parse[Int]("houseNumber") andThen { n =>
        if (n > 0) Valid(n)
        else Invalid(ParseError("houseNumber"))
      }

      houseNumber.isValid shouldBe true

    }

    "sequential validation - andThen 2" in {
      val config1 = Config(Map("houseNumber" -> "-42"))
      val houseNumber = config1.parse[Int]("houseNumber") andThen { n =>
        if (n > 0) Valid(n)
        else Invalid(ParseError("houseNumber"))
      }

      houseNumber.isValid shouldBe false
    }

    "withXor" in {
      def positive(field: String, i: Int): ConfigError Either Int = {
        if(i >= 0) Either.right(i)
        else Either.left(ParseError(field))
      }

      val config1 = Config(Map("houseNumber" -> "-42"))

      val houseNumber = config1.parse[Int]("houseNumber").withEither { (xor: ConfigError Either Int) =>
        xor.flatMap(i => positive("houseNumber", i))
      }

      houseNumber.isInvalid shouldBe true
    }

  }

}
