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

  def isEmpty: Boolean = underlying.isEmpty

  def size: Int = underlying.size

  def headOption: Option[A] = underlying.headOption

  def extract: Either[String, (A, Heap[A])] =
    if (underlying.isEmpty)
      Left("Empty Heap")
    else {
      val tail = underlying.tail
      val next = if (tail.isEmpty) tail else tail.last +: tail.init
      Right(underlying.head, Heap(next, op).bubbleDown(0))
    }

  private def parentIndex(index: Int): Int = (index - 1) / 2

  private def childIndex(index: Int): List[Int] = {
    val left = index * 2 + 1
    val right = index * 2 + 2
    List(left, right).filter(_ < underlying.size)
  }

  private def swap(vector: Vector[A], x: Int, y: Int): Vector[A] = {
    val valueX = vector(x)
    val valueY = vector(y)
    vector
      .updated(y, valueX)
      .updated(x, valueY)
  }

  def bubbleUp(index: Int): Heap[A] = {
    @tailrec
    def loop(vector: Vector[A], idx: Int): Vector[A] = {
      val parentIdx = parentIndex(idx)
      val current = vector(idx)
      val parent = vector(parentIdx)
      if (op(current, parent))
        loop(swap(vector, idx, parentIdx), parentIdx)
      else
        vector
    }
    Heap(loop(underlying, index), op)
  }

  def bubbleDown(index: Int): Heap[A] = {
    @tailrec
    def loop(vector: Vector[A], idx: Int): Vector[A] = {
      val childIndices = childIndex(idx)
      if (childIndices.isEmpty) vector
      else {
        val current = vector(idx)
        val childValues = childIndices.map(vector(_))
        val targetValue = childValues.sortWith(op).head
        if (op(current, targetValue)) vector
        else {
          val childIndex = vector.indexOf(targetValue)
          loop(swap(vector, idx, childIndex), childIndex)
        }
      }
    }
    Heap(loop(underlying, index), op)
  }

  def show: String = {
    @tailrec
    def loop(vector: Vector[A], n: Int, tree: List[String]): List[String] = {
      if (vector.isEmpty) tree
      else loop(vector.drop(n), n * 2, tree :+ vector.take(n).mkString(" "))
    }
    loop(underlying, 1, List.empty).mkString("\n")
  }
}

object Heap {
  def empty[A: Ordering]: Heap[A] = Heap(Vector.empty, implicitly[Ordering[A]].lt)
}

object MedianMaintenance {
  def median(vector: Vector[Int]): Int = {
    val minHeap = Heap[Int](op = _ < _)
    val maxHeap = Heap[Int](op = _ > _)
    val (newMinHeap, newMaxHeap) = vector.foldLeft((minHeap, maxHeap)) {
      case ((min, max), elem) =>
        val Right((el, nextMin)) = min.insert(elem).extract
        val nextMax = max.insert(el)
        if (nextMax.size > nextMin.size) {
          val Right((el2, nextNextMax)) = nextMax.extract
          (nextMin.insert(el2), nextNextMax)
        } else
          (nextMin, nextMax)
    }
    if (newMinHeap.size == newMaxHeap.size)
      newMaxHeap.extract.right.get._1
    else
      newMinHeap.extract.right.get._1

  }
}
