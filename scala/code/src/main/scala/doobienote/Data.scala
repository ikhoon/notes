package doobienote

import shapeless.HNil

import scalaz.concurrent.Task

/**
  * Created by ikhoon on 2016. 9. 13..
  */
object Data {

  import scalaz._, Scalaz._
  import doobie.implicits._
  import shapeless._

  type CountryHListType = String :: Int :: Option[Double] :: HNil

  case class Country(code:String, name: String, population: Long, gnp: Option[Double])

  case class Code(code: String)
  case class CountryInfo(name: String, pop: Int, gnp: Option[Double])

  // TODO FIXME
  /*
  val xa = DriverManagerTransactor[Task](
    driver = "org.h2.Driver",
    url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"

  )


  def populate(): Unit = {
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
    ()
  }
  */
}
