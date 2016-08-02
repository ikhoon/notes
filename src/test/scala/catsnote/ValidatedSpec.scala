package catsnote

import cats.SemigroupK
import cats.data.{NonEmptyList, Validated}
import org.scalatest.{Matchers, WordSpec}
import ParallelValidation._
import cats.data.Validated.{Invalid, Valid}

/**
  * Created by ikhoon on 2016. 8. 2..
  */
class ValidatedSpec extends WordSpec with Matchers {


  "Validated" should {

    import cats.implicits.listInstance
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

      val errors = NonEmptyList(MissingConfig("url"), ParseError("port"))
      invalid == Validated.invalid(errors) shouldBe true
    }
  }

}
