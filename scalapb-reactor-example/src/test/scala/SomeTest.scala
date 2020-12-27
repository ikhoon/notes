import scalapb.reactor.test_service.Request

object SomeTest {

  def main(args: Array[String]): Unit = {
    println(Request(in = 10))
  }
}
