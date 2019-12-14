package algonote

import org.scalatest.{FunSuite, Matchers}

/**
  * Created by ikhoon on 30/07/2018.
  */
class Algo_2_1_StrongConnectedComponentsTest extends FunSuite with Matchers {

  import Algo_2_1_StrongConnectedComponents._

  /**
    * 1 → 2 → 5
    * ↓ ↘︎ ↓
    * 3 → 4
    */
  test("reverse graph") {
    val graph: Graph = Map(
      1 -> Vector(2, 3, 4),
      2 -> Vector(4, 5),
      3 -> Vector(4)
    )

    val revervsed: Graph = Map(
      2 -> Vector(1),
      3 -> Vector(1),
      4 -> Vector(1, 2, 3),
      5 -> Vector(2)
    )

    rev(graph) shouldBe revervsed
  }

  test("build") {
    val graph: Graph = Map(
      1 -> Vector(2, 3, 4),
      2 -> Vector(4, 5),
      3 -> Vector(4)
    )
    val inputs =
      """1 2
        |1 3
        |1 4
        |2 4
        |2 5
        |3 4
      """.stripMargin
    build(inputs.split("\n")) shouldBe graph
  }

  test("dfsLoop") {
    val graph: Graph = Map(
      1 -> Vector(2, 3, 4),
      2 -> Vector(4, 5),
      3 -> Vector(4)
    )
    val ctx = dfsLoop(graph)
    println(s"leader = ${ctx.leader}")
    println(s"f = ${ctx.f}")
  }

  test("simple") {
    val inputs =
      """
        |1 4
        |7 1
        |4 7
        |
        |9 7
        |
        |9 3
        |6 9
        |3 6
        |
        |8 6
        |
        |2 8
        |8 5
        |5 2
      """.stripMargin
    val graph = build(inputs.split("\n"))
    val ctx = dfsLoop(rev(graph))
    ctx.leader shouldBe Map(5 -> 9,
                            1 -> 7,
                            6 -> 9,
                            9 -> 9,
                            2 -> 9,
                            7 -> 7,
                            3 -> 9,
                            8 -> 9,
                            4 -> 7)
    ctx.f shouldBe Map(5 -> 2,
                       1 -> 7,
                       6 -> 5,
                       9 -> 6,
                       2 -> 3,
                       7 -> 9,
                       3 -> 1,
                       8 -> 4,
                       4 -> 8)

    val fReversed: Vector[(Int, Int)] =
      ctx.f.view.map { case (k, v) => v -> k }.toVector.sortWith {
        case ((k1, _), (k2, _)) =>
          k1 > k2
      }

    println(s"graph = ${graph}")
    val scc = fReversed.foldLeft(Vector.empty[Vector[Int]]) {
      case (components, (_, i)) =>
        val explored = components.flatten
        if (explored.contains(i)) components
        else components :+ dfsStrongComponent(graph, i, i, explored.toSet)
    }
    println(s"scc = ${scc}")
    scc.foreach(println)
  }

  test("problem") {
    val inputs = scala.io.Source
      .fromResource("strong_connected_components.txt")
      .getLines()
    val graph = build(inputs.toArray)
    val ctx = dfsLoop(rev(graph))

    val fReversed: Vector[(Int, Int)] =
      ctx.f.view.map { case (k, v) => v -> k }.toVector.sortWith {
        case ((k1, _), (k2, _)) =>
          k1 > k2
      }

//    val scc = fReversed.foldLeft(Vector.empty[Vector[Int]]) {
//      case (components, (_, i)) =>
//        val explored = components.flatten
//        if (explored.contains(i)) components
//        else components :+ dfsStrongComponent(graph, i, i, explored)
//    }

    val scc = fReversed.foldLeft((Set.empty[Int], Vector.empty[Vector[Int]])) {
      case ((explored, top5), (_, i)) =>
        if (explored.contains(i)) (explored, top5)
        else {
          val newComponent = dfsStrongComponent(graph, i, i, explored)
          val top6 = top5 :+ newComponent
          val nextTop5 = top6.sortWith(_.size > _.size).take(5)
          (explored ++ newComponent, nextTop5)
        }
    }

    println(scc._2.map(_.size))
//    scc
//      .map(_.size)
//      .sortWith(_ > _)
//      .take(5)
//      .foreach(println)
  }
}
