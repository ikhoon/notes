package algonote
import org.scalatest.{FunSuite, Matchers}

/**
  * Created by ikhoon on 29/08/2018.
  */
class Algo_2_3_MedianMaintenanceTest extends FunSuite with Matchers {

  test("bubble up1") {
    val heap = Heap[Int](Vector(0, 1, 5, 3, 4, 2), _ < _)
    heap.bubbleUp(5).underlying shouldBe Vector(0, 1, 2, 3, 4, 5)
  }
  test("bubble up2") {
    val heap = Heap[Int](Vector(2, 3, 4, 5, 6, 1), _ < _)
    heap.bubbleUp(5).underlying shouldBe Vector(1, 3, 2, 5, 6, 4)
  }

  test("bubble down") {
    val heap = Heap[Int](Vector(6, 2, 3, 4, 5), _ < _)
    heap.bubbleDown(0).underlying shouldBe Vector(2, 4, 3, 6, 5)
  }

  test("bubble down2") {
    val heap = Heap[Int](Vector(8, 2, 5, 4, 3, 9, 6, 10, 7), _ < _)
    heap.bubbleDown(0).underlying shouldBe Vector(2, 3, 5, 4, 8, 9, 6, 10, 7)
  }

  test("bubble down3") {

    val heap = Heap[Int](Vector(7, 3, 5, 4, 8, 9, 6, 10), _ < _)
    heap.bubbleDown(0).underlying shouldBe Vector(3, 4, 5, 7, 8, 9, 6, 10)
  }

  test("heap insert") {
    val heap = (1 to 10).reverse.foldLeft(Heap.empty[Int]) {
      case (acc, i) =>
        acc.insert(i)
    }
    println(heap)
    println(heap.show)
  }

  def extractAll[A](heap: Heap[A]): Vector[A] = {
    heap.extract match {
      case Left(v)             => Vector.empty
      case Right((elem, next)) => elem +: extractAll(next)
    }
  }

  test("extract") {
    val heap = (1 to 10).reverse.foldLeft(Heap.empty[Int]) {
      case (acc, i) =>
        acc.insert(i)
    }
    val Right((elem, next)) = heap.extract
    elem shouldBe 1
  }
  test("heap sort") {
    val heap = (1 to 10).reverse.foldLeft(Heap.empty[Int]) {
      case (acc, i) =>
        acc.insert(i)
    }
    println(heap.show)
    extractAll(heap) shouldBe Vector(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
  }

  test("median") {
    val vector = Vector(5, 3, 2, 1)
    MedianMaintenance.median(vector) shouldBe 2
  }

  test("median1") {
    val vector = Vector(5, 3, 4, 2, 1)
    MedianMaintenance.median(vector) shouldBe 3
  }

  test("submit") {
    val (median, _) = scala.io.Source
      .fromResource("Median.txt")
      .getLines()
      .map(_.toInt)
      .foldLeft((0, Vector.empty[Int])) {
        case ((sum, xs), x) =>
          val xxs = xs :+ x
          println(xxs.size)
          (sum + MedianMaintenance.median(xxs), xxs)
      }
    println(median % 10000)

  }

}
