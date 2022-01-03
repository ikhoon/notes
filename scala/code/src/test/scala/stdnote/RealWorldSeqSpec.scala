package stdnote

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.collection.immutable.Seq

/**
  * Created by Liam.M(엄익훈) on 16/01/2017.
  */
class RealWorldSeqSpec extends AnyFunSuite with Matchers {

  /**

    // tiara
    account_id, page
    1, home
    1, hotdeal
    1, best
    2, home
    2, best
    3, home
    3, best
    3, giftbox
    3, hotdeal
    4, home
    4, best
    .....
    .....


    home-hotdeal-best 1
    home-best 2
    home-best-giftbox-hotdeal 1

    */
  test("test") {

    // print it
    val file = "/Users/liamm/IdeaProjects/scala-exercise-note/src/test/scala/stdnote/visit.csv"
    val lines: List[String] = scala.io.Source.fromFile(file).getLines().toList

    val s1: Seq[(String, String)] = lines.map(line => {
      val tokens = line.split(",")
      val key = tokens(0)
      val value = tokens(1)
      (key, value)
    })
    s1.foreach(println)
    println("")
    val s2: Map[String, Seq[(String, String)]] = s1.groupBy { case (key, value) => key }
    s2.foreach(println)
    println("")
    val s3 = s2.mapValues(value => value.map(_._2))
    s3.foreach(println)
    // "1, home" => map => split => List(1, "home")
    //

    // mkString
    val s4 = s3.mapValues(_.mkString("-"))
    println("")
    s4.foreach(println)
    val s5 = s4.map { case (key, value) => value }

    println("")
    s5.foreach(println)

    val s6 = s5.groupBy(x => x).mapValues(_.size)
    println("")
    s6.foreach(println)

    // sorting
    // sort
    println(s6.toList.sorted)
    println()
    println(s6.toList.sortBy(_._2))
    println()
    println(s6.toList.sortWith { case (a, b) => a._1 > b._1 })
    println()

    // reverse sort
    /**
      key, count

      home-hotdeal-best 1
      home-best 2
      home-best-giftbox-hotdeal 1

      *
      */
    // Map
    /**
      *
      1, List(home, hotdeal, best)
      2, List(home, best)
      3, List(home, best, giftbox , hotdeal)
      */

//      Joiner(",").join(collection)
    // list join => ???
    // 1, home-hotdeal-best
    // 2, home-best
    // 3, home-best-giftbox-hotdeal
    // home-hotdeal-best
    // home-best

  }
}
