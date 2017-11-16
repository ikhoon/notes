package doobienote

import org.scalatest.{Matchers, WordSpec}

/**
  * Created by ikhoon on 2016. 9. 11..
  */
class ConnectingToDatabaseSpec extends WordSpec with Matchers {

  "First programs" should {

    // TODO FIXME
    /*
    import doobie.imports._
    import scalaz._, Scalaz._
    import scalaz.concurrent.Task

    val xa = DriverManagerTransactor[Task](
      driver = "org.h2.Driver",
      url = "jdbc:h2:mem:test1"
    )

    "lets start" in {
      val program: ConnectionIO[Int] = 42.point[ConnectionIO]

      val task: Task[Int] = program.transact(xa)
      task.unsafePerformSync shouldBe 42
    }

    "connect to a database" in {
      sql"select 42"
        .query[Int]
        .unique
        .transact(xa)
        .unsafePerformSync shouldBe 42
    }

    "more than one thing" in {
      val largerProgram = for {
        a <- sql"select 42".query[Int].unique
        b <- sql"select power(5, 2)".query[Int].unique
      } yield (a, b)

      largerProgram.transact(xa).unsafePerformSync shouldBe (42, 25)
    }

    "applicative functor" in {
      val oneProgram = sql"select 42".query[Int].unique
      val anotherProgram = sql"select power(5, 2)".query[Int].unique
      (oneProgram |@| anotherProgram) { _ + _ }.transact(xa).unsafePerformSync shouldBe 67
    }
  */
  }

}
