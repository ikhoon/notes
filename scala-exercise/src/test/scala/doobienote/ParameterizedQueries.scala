package doobienote

import org.scalatest.{Matchers, WordSpec}

import scalaz.NonEmptyList

/**
  * Created by ikhoon on 2016. 9. 18..
  */
//noinspection SqlDialectInspection,SqlNoDataSourceInspection
class ParameterizedQueries extends WordSpec with Matchers {

  "parameterized queries" should {

    import doobie.imports._

    import Data._

    Data.populate()

    "adding a parameter" in {
      def biggerThan(minPop: Int): Query0[Country] =
        sql"""
          select code, name, population, gnp
          from country
          where population > $minPop
          order by population asc
          """.query[Country]

      val countriesNames = biggerThan(75000000)
        .list
        .transact(xa)
        .unsafePerformSync
        .map(_.name)

      countriesNames shouldBe List("Germany", "United States of America")
    }

    "multiple parameters" in {
      def populationIn(range: Range): Query0[Country] =
        sql"""
          select code, name, population, gnp
          from country
          where population > ${range.min} and population < ${range.max}
          order by population asc
        """.query[Country]

      val countriesName = populationIn(25000000 to 75000000)
        .list
        .transact(xa)
        .unsafePerformSync
        .map(_.name)

      countriesName shouldBe List("Spain", "France", "United Kingdom")
    }

    "dealing with in clauses" in {
      /*
      def populationIn(range: Range, codes: NonEmptyList[String]): Query0[Country] = {
        implicit val codesParam = Param.many(codes)
        sql"""
          select code, name, population, gnp
          from country
          where population > ${range.min}
          and   population < ${range.max}
          and   code in (${codes: codes.type})
        """.query[Country]
      }

      val countriesNames = populationIn(25000000 to 75000000, NonEmptyList("ESP", "USA", "FRA"))
        .list
        .transact(xa)
        .unsafePerformSync
        .map(_.name)

      countriesNames shouldBe List("Spain", "France")
      */

    }
  }
}
