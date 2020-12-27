package macronote
object Main {
  val debugEnabled: Boolean = true
  import Debug._
  def main(args: Array[String]): Unit = {
    hello()
    val a = 10
    val b = 20
    debugHello(a + b)

    debug(a, b, a + b)
  }
}
