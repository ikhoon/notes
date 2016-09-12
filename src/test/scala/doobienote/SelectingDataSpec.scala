package doobienote

import org.scalatest.{Matchers, WordSpec}
import scalaz.concurrent.Task
import scalaz._, Scalaz._

/**
  * Created by ikhoon on 2016. 9. 12..
  */
class SelectingDataSpec extends WordSpec with Matchers {

  import doobie.imports._

  "select data" should {

    val xa = DriverManagerTransactor[Task](
      driver = "org.h2.Driver",
      url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"

    )

    val drop: Update0 = sql"""
      drop table if exists COUNTRY
    """.update

    val create: Update0 = sql"""
      CREATE TABLE COUNTRY(
      code VARCHAR(3) NOT NULL,
      name VARCHAR(255) NOT NULL,
      population BIGINT NOT NULL,
      gnp DECIMAL(10, 2))
    """.update

    (drop.run.transact(xa) *> create.run.transact(xa)).unsafePerformSync

    case class Country(code:String, name: String, population: Long, gnp: Option[Double])

    val countries = List(
      Country("DEU", "Germany", 82164700, Some(2133367.00)),
      Country("ESP", "Spain", 39441700, None),
      Country("FRA", "France", 59225700, Some(1424285.00)),
      Country("GBR", "United Kingdom", 59623400, Some(1378330.00)),
      Country("USA", "United States of America", 278357000, Some(8510700.00))
    )

    val insert = """
      INSERT INTO country(code, name, population, gnp)
      VALUES (?, ?, ?, ?)
    """

    Update[Country](insert).updateMany(countries).transact(xa).unsafePerformSync

    "unique" in {

      val countryName =
        sql"select name from country where code = 'ESP'"
        .query[String]
        .unique
        .transact(xa)
        .unsafePerformSync
      countryName shouldBe "Spain"
    }

    "option" in {
      val maybeCountryName =
        sql"select name from country where code = 'ITA'"
          .query[String]
          .option
          .transact(xa)
          .unsafePerformSync

      maybeCountryName shouldBe None
    }

    "list" in {
      val countryNames =
        sql"select name from country order by name"
          .query[String]
          .list
          .transact(xa)
          .unsafePerformSync

      countryNames.head shouldBe "France"
    }

    "process" in {
      val countryNames =
        sql"select name from country order by name"
          .query[String]
          .process
          .take(3)
          .list
          .transact(xa)
          .unsafePerformSync

      countryNames.size shouldBe 3

    }

  }

}
