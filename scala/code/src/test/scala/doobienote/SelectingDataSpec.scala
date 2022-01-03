package doobienote

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import scalaz.concurrent.Task
import scalaz._, Scalaz._

/**
  * Created by ikhoon on 2016. 9. 12..
  */
class SelectingDataSpec extends AnyWordSpec with Matchers {

  import doobie.implicits._

  /*
  "select data" should {

    import Data.xa
    Data.populate()

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
 */

}
