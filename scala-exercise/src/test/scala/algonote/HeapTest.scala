package algonote
import org.scalatest.{FunSuite, Matchers}

/**
  * Created by ikhoon on 29/08/2018.
  */
class HeapTest extends FunSuite with Matchers {

  test("heap insert") {
    val heap = (1 to 10).reverse.foldLeft(Heap.empty[Int]) {
      case (acc, i) =>
        acc.insert(i)
    }
    println(heap)

  }
}
