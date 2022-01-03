package stdnote

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

/**
  * Created by ikhoon on 2016. 7. 28..
  */
class ClassSpec extends AnyFunSuite with Matchers {

  /**
    * 1. `val`의 의미는 무엇인가?
    * 2. `class`의 생성자의 파라메터에 val이 있을때와 없을때의 차이는?
    */
  test("class with val") {
    class ClassWithVal(val name: String)
    val classWithVal = new ClassWithVal("술디나")
    classWithVal.name shouldBe "술디나"
  }

  /**
    * `var`는 쓰지 말자 필요없다.
    */
  test("class with var") {
    class ClassWithVal(val name: String)
    val classWithVal = new ClassWithVal("술디나")
//    classWithVal.name = "김디나"
    classWithVal.name shouldBe "김디나"
  }
}
