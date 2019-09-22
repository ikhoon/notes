package macronote

import org.scalatest.FunSuite

/**
  * Created by Liam.M on 2018. 07. 31..
  */
class EasyLoggingTest extends FunSuite {

  test("log with prepend") {
    EasyLogging.log("good")
    EasyLogging.log("world")
    EasyLogging.log("a yo")
  }

  def foo(x: Int) = {
    EasyLogging.trace(x)
  }

  test("trace logging") {
    foo(10)
    foo(20)
    foo(30)
  }
}
