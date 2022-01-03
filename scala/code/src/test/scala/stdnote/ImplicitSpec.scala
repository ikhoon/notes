package stdnote

import java.util.concurrent.Callable

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by Liam.M(엄익훈) on 9/28/16.
  */
class ImplicitSpec extends AnyFunSuite with Matchers {

  // implicit : 은연중의 <-> 대놓고, 명시적 : explicit

  // lambda, for comprehension, flatMap, trait,
  // implicit 기존에 만들어진 라이브러리/프레임워크도 잘이해하고 갖다쓸수 있다.

  // 함수의 파라메터

  test("parameter") {
    def foo(a: Int): String = {
      a + " " + a
    }
    val bar: Int = 10
    // 명시적
    println(foo(10))
    // 암묵적으로 함수의 파라메터를 바인딩하고 싶다.
    def foo2(implicit a: Int): String = {
      a + " " + a
    }

    // 찾는 조건
    // 1. 같은 타입
    // 2. 현재스코프 implicit
    // 3. import 된 스코프 implicit
    // 4. companion object implicit

  }

  object barInt {
    implicit val bar: Int = 10
  }

  test("example") {
    // 1. 함수의 인자를 implicit 으로 받는 함수를 만든다.
    // 2. implicit 변수도 만든다.
    // 3. 실행을 해보면 된다.
    // 4. 다양한 scope에 넣어서 실행해보면 좋을것 같아요.
    // 5. 현재 찾을수 있는 scope에 같은 타입의 implicit이 2개 이상이면 어떻게 되나?
  }

  test("함수의 implicit") {
    // 형변환
    // Int => String
    def foo(a: Int): String = a.toString + " " + a.toString

    val foo1: String = foo(10)

    case class Alcol(name: String)
    case class Edina(want: String)

    implicit def to(edina: Edina): Alcol =
      Alcol(edina.want)

    val edina = Edina("와인")
    println(to(edina))

    // 현재의 타입에서 바뀔 타입으로 구성된 implicit 함수를 찾습니다.
    val alcol: Alcol = edina
    val alcol2: Alcol = to(edina)

  }

  test("explicit1") {

    def foo(bar: String): String = bar + " " + bar

    implicit val str: String = "hello"
    val str2: String = "world"

    println(foo(str)) // => 명시적인거입니다.

    def foo1(implicit bar1: String): String = bar1 + " " + bar1

    println(foo1(str)) // => 명시적인거입니다.
    println(foo1) // => 암묵적으로 쓰고 싶습니다.
    // 1. 로컬 스코프
    // 2. 컴패니언 오브젝트
    // 3. import 된애들을 찾고 .... ,
    // 찾는 기준 type

    // Quiz1. 만약에 str도 앞에 implicit 있으면 어떻게 될까요?

    // 1. 함수의 파라메터에 implicit
    // 2. 변수에 implicit

    // 1. implicit val a                               변수 앞
    // 2. def foo(implicit bar: String): String = ???  함수의 인자 앞
    // 3. implicit def foo(a: String): Int             함수 앞
    // 4. implicit class Foo(a: String) {              class 앞
    //     def bar: String = a + " " + a
    //  }

    case class Status(status: Int)
    implicit def foo2(bar: Int): Status = Status(bar)
    implicit def foo3(bar: Int): String = bar.toString

    // 암묵적인 타입 케스팅

    val a1: Status = 123 //
    val a3: String = 123 //
    val a2: Status = foo2(123) //
    println(a1)
    println(a2)
    println(a3)

    // 문법요소
    implicit class Foo(b: String) {
      def barbarbar: String = b + b + b
    }
    // 몽키 패칭 => 위험

    println(a3.barbarbar)
    println(new Foo(a3).barbarbar)

    // 자바의 단점..... interface를 바꿀수 없다.
    // 오래된 List => 의 interface가 아직도 그대로다.

    // 람다 = 익명함수, arrow, first class function
    // implicit part 2
    /*
      implicit 4가지 :
      - 위치할수 있는곳
      - 함수의 파라메터
      - 변수의 앞에
      - class 앞에
      - 함수의 앞에
      - 클래스의 인자에
     */
//    class MyFuture(implicit executionContext: ExecutionContext) {
//
//    }
//    new MyFuture
  }

  test("실전") {
    // 1. 암묵적인 type casting

    // Edina(sul: String) ==> Suldia(edina: String)
    case class Edina(sul: String)
    case class Suldina(sul: String)

//    val suldina : Suldina = Edina("isool")

    // 암묵적으로 되는건 명시적으로도 된다.
    // f: Edina => Suldina
    // f(edina) = suldina
    // impliclit f: Edina => Suldina
    // suldian = edina

    implicit def toSuldina(e: Edina): Suldina = Suldina(e.sul)
    val e = Edina("isool")
    val s = toSuldina(e)
    val s1: Suldina = e

    // 2. 기존에 있는 class에 문법 추가하기
    // Edina에 name이라는 함수를 추가해봅시다.
    val e1 = Edina("막걸리")
    e1.name == "에디나"

    implicit class xxxxxxxxx(e: Edina) {
      def name = "에디나"
    }

    // 3. 암묵적인 변수가 있구요.
    //    암묵적인 변수를 특정 클래스에 주입.
    val a = Future[Int](2)(global)
    val list = List[Int](1 + 10)
    val future = Future[Int](1 + 10)
    val set = Set[Int](2)

  }

}
