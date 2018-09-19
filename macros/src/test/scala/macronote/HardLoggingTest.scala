package macronote

import org.scalatest.FunSuite

/**
  * Created by Liam.M on 2018. 07. 31..
  */
class HardLoggingTest extends FunSuite {

  import HardLogging._
  def foo(): Unit = {
    log2("hello world")
  }

  test("print with prefix") {
    foo()
  }
}
