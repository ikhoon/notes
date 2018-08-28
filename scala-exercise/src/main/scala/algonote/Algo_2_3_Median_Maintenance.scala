package algonote
import scala.annotation.tailrec

/**
  * Created by ikhoon on 28/08/2018.
  */
case class Heap[A](underlying: Vector[A] = Vector.empty, op: (A, A) => Boolean) {
  def insert(a: A): Heap[A] = {
    Heap(underlying :+ a, op)
      .bubbleUp(underlying.size)
  }

  def extract: Either[String, (A, Heap[A])] =
    if (underlying.isEmpty)
      Left("Empty Heap")
    else {
      val tail = underlying.tail
      Right(underlying.head, Heap(tail.init ++ tail.init, op).bubbleDown(0))
    }

  private def parentIndex(index: Int): Int = (index - 1) / 2

  private def childIndex(index: Int): List[Int] = {
    val left = index * 2 + 1
    val right = index * 2 + 2
    List(left, right).filter(_ <= underlying.size)
  }

  private def swap(vector: Vector[A], x: Int, y: Int): Vector[A] = {
    val valueX = vector(x)
    val valueY = vector(y)
    vector
      .updated(y, valueX)
      .updated(x, valueY)
  }

  private def bubbleUp(index: Int): Heap[A] = {
    @tailrec
    def loop(vector: Vector[A], idx: Int): Vector[A] = {
      val parentIdx = parentIndex(idx)
      val current = vector(idx)
      val parent = vector(parentIdx)
      if (op(current, parent))
        loop(swap(underlying, idx, parentIdx), parentIdx)
      else
        vector
    }
    Heap(loop(underlying, index), op)
  }

  private def bubbleDown(index: Int): Heap[A] = {
    @tailrec
    def loop(vector: Vector[A], idx: Int): Vector[A] = {
      val childIndices = childIndex(idx)
      if (childIndices.isEmpty) vector
      else {
        val childValues = childIndices.map(vector(_))
        val targetValue = childValues.sortWith(op).head
        val childIndex = childValues.indexOf(targetValue)
        loop(swap(vector, idx, childIndex), childIndex)
      }
    }
    Heap(loop(underlying, index), op)
  }

}

object Heap {
  def empty[A: Ordering]: Heap[A] = Heap(Vector.empty, implicitly[Ordering[A]].lt)
}
