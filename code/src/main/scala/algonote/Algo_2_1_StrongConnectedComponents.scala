package algonote

import pprint.Tree.Ctx

import scala.annotation.tailrec
import scala.collection.mutable

/**
  * Created by ikhoon on 30/07/2018.
  */
object Algo_2_1_StrongConnectedComponents {

  type Graph = Map[Int, Vector[Int]]
  def build(lines: Array[String]): Graph = {
    lines
      .map(_.replaceFirst("^\\s+", ""))
      .filterNot(_.isEmpty)
      .map(_.split("\\s+"))
      .map(_.map(_.toInt).toList)
      .map { case x :: y :: Nil => (x, y) }
      .foldLeft(Map.empty: Graph) {
        case (acc, (vertex, edge)) =>
          modify[Int, Vector[Int]](acc, vertex, Vector.empty, _ :+ edge)
      }
  }

  def rev(graph: Graph): Graph = {
    graph.foldLeft(Map.empty: Graph) {
      case (acc, (vertex, edges)) =>
        edges.foldLeft(acc) {
          case (newGraph, edge) =>
            modify[Int, Vector[Int]](newGraph, edge, Vector.empty, _ :+ vertex)
        }
    }
  }

  case class SCCContext(i: Int,
                        s: Int,
                        explored: mutable.Set[Int],
                        leader: mutable.Map[Int, Int],
                        f: Map[Int, Int],
                        t: Int)

  def dfs(graph: Graph, ctx: SCCContext): SCCContext = {
    ctx.explored += ctx.i
    ctx.leader += (ctx.i -> ctx.s)
    var marked = ctx
//    var marked = ctx.copy(explored = ctx.explored + ctx.i,
//                          leader = ctx.leaderupdated(ctx.i, ctx.s))

    var adj = graph.getOrElse(marked.i, Vector.empty)
    while (adj.nonEmpty) {
      val j = adj.head
      if (!marked.explored.contains(j)) {
        marked = dfs(graph, marked.copy(i = j))
      }
      adj = adj.tail
    }

    val incT = marked.t + 1
    marked.copy(t = incT, f = marked.f + (ctx.i -> incT))

    /**
    val traversed = graph.getOrElse(marked.i, Vector.empty).foldLeft(marked) {
      case (curr, j) =>
        if (!isExplored(j, curr.explored)) dfs(graph, curr.copy(i = j))
        else curr
    }

    val incT = traversed.t + 1
    traversed.copy(t = incT, f = traversed.f + (ctx.i -> incT))
    */
  }

  def isExplored(node: Int, explored: Set[Int]): Boolean =
    explored.contains(node)

  def dfsLoop(graph: Graph): SCCContext = {

    val zero =
      SCCContext(0, 0, mutable.Set.empty, mutable.Map.empty, Map.empty, 0)

    val keys: Vector[Int] = graph.keys.toVector
    keys
      .sortWith(_ > _)
      .foldLeft(zero) {
        case (ctx, i) =>
          if (!ctx.explored.contains(i)) dfs(graph, ctx.copy(i = i, s = i))
          else ctx
      }
  }

  def modify[K, V](map: Map[K, V], key: K, default: V, f: V => V): Map[K, V] = {
    val value = map.getOrElse(key, default)
    map.updated(key, f(value))
  }

  def dfsStrongComponent(graph: Graph,
                         start: Int,
                         node: Int,
                         explored: Set[Int],
  ): Vector[Int] = {

    Option
    def dfs0(graph: Graph,
             start: Int,
             node: Int,
             explored: Set[Int],
             component: Vector[Int]): (Set[Int], Vector[Int]) = {
      if (node % 100 == 0)
        println(s"start = ${start}, node = $node, explored = ${explored.size}")
      if (explored.contains(node)) (explored, component)
      else {
        val newExplored: Set[Int] = explored + node
        val newComponent: Vector[Int] = component :+ node
        graph
          .getOrElse(node, Vector.empty)
          .foldLeft((newExplored, newComponent)) {
            case ((currExp: Set[Int], currComp: Vector[Int]), i) =>
              if (currExp.contains(i)) (currExp, currComp)
              else dfs0(graph, start, i, currExp, currComp)
          }
      }
    }

    dfs0(graph, start, node, explored, Vector.empty)._2
  }
}
