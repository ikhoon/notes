package algonote

import scala.annotation.tailrec
import scala.collection.immutable
import scala.util.Random
import algonote.Algo_1_4_KargerMinCut._

/**
  * Created by ikhoon on 15/07/2018.
  */

case class Algo_1_4_KargerMinCut(graph: Graph) {

  def sizeOfVertices: Int = graph.size

  def pickRandom2Edges(grp: Graph): (Int, Int) = {
    val vertices: Seq[Int] = grp.keys.toIndexedSeq
    val vertexIndex = Random.nextInt(vertices.size)
    val vertex = vertices(vertexIndex)
    val edges = grp(vertex)
    val edgeIndex = Random.nextInt(edges.size)
    edges(edgeIndex)
  }

  def trackToNewVertex(contractMap: ContractMap, vertex: Int): Int = {
    contractMap.get(vertex)
      .fold(vertex)(trackToNewVertex(contractMap, _))
  }

  def mergeEdges(grp: Graph, contractMap: ContractMap)(u: Int, v: Int): (Graph, ContractMap) = {
    // merge edges
    val trackU = trackToNewVertex(contractMap, u)
    val trackV = trackToNewVertex(contractMap, v)

    val uv = grp(trackU) ++ grp(trackV)

    // update v edges to u
    val newGraph: Graph = grp.filterNot(_._1 == trackV).updated(trackU, uv)

    val newContractMap: ContractMap = contractMap + (v -> u)

    newGraph -> newContractMap
  }

  def removeSelfLoop(grp: Graph, contractMap: ContractMap)(u: Int, v: Int): Graph = {
    // merge edges
    contractMap.get(u)
      .fold(graph) {
        trackU =>
          val uEdges = grp(trackU)
          val removed = uEdges.filter(edge => edge != (u, v) && edge != (v, u))
          removeSelfLoop(grp.updated(trackU, removed), contractMap)(trackU, v)
      }
  }
  def removeEmpty(grp: Graph):Graph = {
    grp.filter(_._2.nonEmpty)
  }


  @tailrec
  final def minCut(grp: Graph, contractMap: ContractMap):Graph = {
    if(grp.size > 2) {
      val (u, v) = pickRandom2Edges(grp)
      val (merged, newContractMap) = mergeEdges(grp, contractMap)(u, v)
      val removed = removeSelfLoop(merged, newContractMap)(u, v)
//      val nonEmpty = removeEmpty(removed)
      minCut(removed, newContractMap)
    }
    else grp
  }

  def findMinCut(): Graph = {
    minCut(graph, Map.empty)
  }
}

object Algo_1_4_KargerMinCut {

  type ContractMap = Map[Int, Int]
  type Graph = Map[Int, Vector[(Int, Int)]]

  def build(lines: Array[String]): Algo_1_4_KargerMinCut = {
    val underlying = lines
      .map(_.replaceFirst("^\\s+", ""))
      .filterNot(_.isEmpty)
      .map(_.split("\\s+"))
      .map(_.map(_.toInt).toList)
      .map { case x :: xs => (x, xs.toVector.map(x -> _)) }
      .toMap
    Algo_1_4_KargerMinCut(underlying)
  }
}

