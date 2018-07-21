package algonote

import scala.annotation.tailrec
import scala.collection.immutable
import scala.util.Random
import algonote.Algo_1_4_KargerMinCut._

/**
  * Created by ikhoon on 15/07/2018.
  */

class Algo_1_4_KargerMinCut {


  def pickRandom2Edges(grp: Graph): (Int, Int) = {
    val vertices = grp.keys.toIndexedSeq
    val vertexIndex = Random.nextInt(vertices.size)
    val vertex = vertices(vertexIndex)
    val edges = grp(vertex)
    val edgeIndex = Random.nextInt(edges.size)
    edges(edgeIndex)
  }


  def findVertex(grp: Graph, u: Int): (Set[Int], Vector[(Int, Int)]) = {
    grp.filterKeys(xs => xs.contains(u)).head
  }

  def mergeEdges(grp: Graph)(u: Int, v: Int): Graph = {
    val (vertexU, edgeU) = findVertex(grp, u)
    val (vertexV, edgeV) = findVertex(grp, v)
    val newEdge = edgeU ++ edgeV
    val newVerTex = vertexU ++ vertexV
    // update v edges to u
    grp.filterNot(p => p._1 == vertexU || p._1 == vertexV)
      .updated(newVerTex, newEdge)
  }

  def removeSelfLoop(grp: Graph): Graph = {
    grp.map { case (k, edges) =>
      k -> edges.filterNot { case (u, v) =>
        k.contains(u) && k.contains(v)
      }
    }
  }


  @tailrec
  final def minCut(grp: Graph):Graph = {
    if(grp.size > 2) {
      val (u, v) = pickRandom2Edges(grp)
      val merged = mergeEdges(grp)(u, v)
      val removed = removeSelfLoop(merged)
      minCut(removed)
    }
    else grp
  }

}

object Algo_1_4_KargerMinCut {

  type Graph = Map[Set[Int], Vector[(Int, Int)]]

  def build(lines: Array[String]): Graph = {
    lines
      .map(_.replaceFirst("^\\s+", ""))
      .filterNot(_.isEmpty)
      .map(_.split("\\s+"))
      .map(_.map(_.toInt).toList)
      .map { case x :: xs => (Set(x), xs.toVector.map(x -> _)) }
      .toMap
  }

  def findMinCut(underlying: Graph): Graph = {
    new Algo_1_4_KargerMinCut().minCut(underlying)
  }
}

