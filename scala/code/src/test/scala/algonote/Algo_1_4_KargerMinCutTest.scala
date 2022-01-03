package algonote

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.collection.immutable

/**
  * Created by ikhoon on 15/07/2018.
  */
class Algo_1_4_KargerMinCutTest extends AnyFunSuite with Matchers {

  import Algo_1_4_KargerMinCut._
  test("build graph") {
    val matrix =
      """
        |1 2 3
        |2 1 3 4
        |3 1 2 4
        |4 2 3
      """.stripMargin
        .split("\n")

    val graph = Algo_1_4_KargerMinCut.build(matrix)
    graph.size shouldBe 4
    graph(Set(1)) shouldBe Set(2, 3)
    graph(Set(3)) shouldBe Set(1, 2, 4)

  }

  /**
    *  1 --- 2
    *  |  /  |
    *  | /   |
    *  3 --- 4
    */
  test("find min 1") {
    val matrix =
      """
        |1 2 3
        |2 1 3 4
        |3 1 2 4
        |4 2 3
      """.stripMargin
        .split("\n")

    val graph = Algo_1_4_KargerMinCut.build(matrix)
    (1 to 100).foreach { i =>
      val cuts = Algo_1_4_KargerMinCut.findMinCut(graph)
      println(s"$i cuts = ${cuts}")
    }
  }

  /**
    * 1 --- 2 ---- 3 ---- 4
    * | \  /|      | \  / |
    * | /  \|      | /  \ |
    * 5 --- 6      7 ---- 8
    */
  test("find min 2") {
    val matrix =
      """
        |1 2 5 6
        |2 1 3 5 6
        |3 2 4 7 8
        |4 3 7 8
        |5 1 2 6
        |6 1 2 5
        |7 3 4 8
        |8 3 4 7
      """.stripMargin
        .split("\n")

    val graph = Algo_1_4_KargerMinCut.build(matrix)
    val all: immutable.Seq[Graph] = (1 to 100).map { i =>
      val cuts = Algo_1_4_KargerMinCut.findMinCut(graph)
      println(s"$i cuts = ${cuts}")
      cuts
    }
    val sorted = all.sortBy(graph => {
      graph.head._2.size
    })
    println("sorted!!!")
    sorted.foreach(println)
  }

  test("find mincut submit") {
    val matrix = scala.io.Source
      .fromResource("karger_mincut_input.txt")
      .getLines()
      .toArray
    val graph = Algo_1_4_KargerMinCut.build(matrix)

    val all: immutable.Seq[Graph] = (1 to 100).map { i =>
      Algo_1_4_KargerMinCut.findMinCut(graph)
    }
    val sorted = all.sortBy(graph => {
      graph.head._2.size
    })
    println("sorted!!!")
    sorted.foreach(println)
    val minCutGraph: Graph = sorted.head
    println(minCutGraph.head._2.size)

  }

  test("string split") {
    val str = "1 2  3 4"
    str.split("\\s+").toList shouldBe List("1", "2", "3", "4")
  }
}
