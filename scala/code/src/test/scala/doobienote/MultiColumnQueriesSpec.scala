package doobienote

import doobienote.Data.{Country, CountryHListType}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import scalaz.concurrent.Task

/**
  * Created by ikhoon on 2016. 9. 13..
  */
class MultiColumnQueriesSpec extends AnyWordSpec with Matchers {

  /*
  "multi column queries" should {
    import doobie.imports._

    import Data._

    Data.populate()


    "tuple" in {
      val (name, population, gnp) =
        sql"select name, population, gnp from country where code = 'ESP'"
          .query[(String, Int, Option[Double])]
          .unique
          .transact(xa)
          .unsafePerformSync

      gnp shouldBe None
    }

    "hlist" in {

      val hlist =
        sql"select name, population, gnp from country where code = 'FRA'"
          .query[CountryHListType]
          .unique
          .transact(xa)
          .unsafePerformSync

      hlist.head shouldBe "France"
    }

    "case class" in {
      val country: Country =
        sql"select code, name, population, gnp from country where name ='United Kingdom'"
          .query[Country]
          .unique
          .transact(xa)
          .unsafePerformSync
      country.code shouldBe "GBR"
    }

    "tuple of two case classes" in {

      val (code: Code, country: CountryInfo) =
        sql"select code, name, population, gnp from country where code = 'ESP'"
          .query[(Code, CountryInfo)]
          .unique
          .transact(xa)
          .unsafePerformSync

      country.name shouldBe "Spain"
    }

    "map" in {
      val notFoundCountry = CountryInfo("Not Found", 0, None)
      val countriesMap: Map[Code, CountryInfo] =
        sql"select code, name, population, gnp from country"
          .query[(Code, CountryInfo)]
          .list
          .transact(xa)
          .unsafePerformSync
          .toMap

      countriesMap.getOrElse(Code("DEU"), notFoundCountry).name shouldBe "Germany"
      countriesMap.get(Code("ITA")) shouldBe None

    }
  }
 */
}
