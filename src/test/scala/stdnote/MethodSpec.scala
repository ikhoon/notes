package stdnote

import org.scalatest.{FunSuite, Matchers}

import scala.collection.immutable.Seq

/**
  * Created by ikhoon on 2017. 3. 3..
  */
class MethodSpec extends FunSuite with Matchers{

  test("max") {
    // 함수를 만들어보자
    // max(a, b) => ? a or b

    // method, function
    // 1. 함수를 만드는 키워드: def
    // 2. 타입의 정의: (a: Int), (b: Any)
    // 3. 반환 타입:
    // 4. 괄호:
    def foo(input: Int): String = {
      input.toString
    }

    def max(x:Int, y:Int) : Int = {
      if (x>y) x else y
    }

    def max1(x:Int, y:Int) : Int =
      if (x>y)
        x
      else
        y

    def getValueOfPi1: Double = 3.14159
    def getValueOfPi2(): Double = 3.14159

    val b: Double = getValueOfPi1
    val c: Double = getValueOfPi2

    def printHelloMsg = println("Hello there !!!!")

    // return
    // yes?
    // no?

    // 함수가 public 일때
    // 타입이 애매모호할때
    // 땡
    // 가독성 있어야 할때
    // 재귀함수
    // return 키워드를 쓸때는
    // 로컬 타입추론
    def factorial(n: Int): Int =
      if(n <= 0) 1
      else n * factorial(n - 1)

    def whichIsGreater (a : Int , b: Int) {
      if(a>b) a else b
    }

    def whichIsGreater1 (a : Int , b: Int) = {
      if(a>b) a else b
    }
    val bb: Int = whichIsGreater1(1, 20)

    def someMethod(): Unit = {
      return 200
    }
    def someMethod1(): Int = {
      return 200
    }

    val cc = someMethod()
    println(cc)

    val dd = someMethod1()
    println(dd)

    val a = 10
//    var b = 20
    def some(a: Int) = {
      var b = a
      b = b - 10
    }



    def whichIsGreaterA (a : Int , b: Int): Int = if (a>b) a else b

    // 타샤 : Int
    // 에디나 : Unit

    val list: Seq[Any] = List(1, "10", 2L)

    def whichIsGreaterB (a : Int , b: Int): Any = {

      if (a>b) a else "a is lesser"

    }


    // 이거 eager
    //
    def multiply(x : Int, y: Int) : Int ={
      println("xyxyxyxyxyxyxy")
      x * y
    }
    def multiply1(x: => Int, y: => Int) : Int ={
      println("xyxyxyxyxyxyxy")
      x * y
    }
    def multiply2(x: => Int, y: Int) : Int ={
      println("xyxyxyxyxyxyxy")
      x * y
    }


    def x: Int = {
      println("xxxxx")
      10
    }
    def y: Int = {
      println("yyyy")
      20
    }

    multiply(x, y) // x, y, xy // 김디나 : xy => x => y, 김계옥님 : xy => x => y
    // x 평가 y 평가, xy 출력
    println
    multiply1(x, y) // 샐리 x, y, xy,
    println
    multiply2(x, y) // 전: xx, xyxy, yy

  }

  def isAllowedURl(url: String = "default"): String = {
    if(url.equals("default"))
      "No URL provided"
    else
      "Access allowed"
  }

  isAllowedURl()
  isAllowedURl("aAAAAAAAA")

  def asaaa = ???


}
