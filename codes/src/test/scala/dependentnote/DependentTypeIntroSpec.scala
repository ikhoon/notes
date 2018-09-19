package dependentnote

import org.scalatest.FunSuite

import scala.concurrent.Future
import scala.reflect.ClassTag


trait ABC {
  type DEF
}

object ABC {
  type Aux[DEF0] = ABC { type DEF = DEF0 }
}
/**
  * Created by ikhoon on 2017. 4. 18..
  */
class DependentTypeIntroSpec extends FunSuite {

  test("java vs scala") {
    // nested class
    trait A {
      trait B
    }
    // context bound
    def createArray[T: ClassTag] = {
      // implicit instance 갖다 쓰고 싶다.
      implicitly[ClassTag[T]].newArray(10)
      new Array[T](10)
    }
    def createArray2[A](implicit A: ClassTag[A]) = {
      A.newArray(10)
    }

    // a객체를 두개 만들었쥬
    val a1 = new A {}
    val a2 = new A {}

    // 의존 타입을 왜쓸까유?
    // 값, 타입, A 타입이고 a1은 값, 값을 타입처럼 쓰고싶다

    val b1 : a1.B = new a1.B {}
    val b2 : a2.B = new a2.B {}

    val b4 : A#B = new a1.B {}
    val b5 : A#B = new a2.B {}
    val b6 : A#B = new a2.B {}
//
    // implicit 찾는 기준?
    // Int, 10, type level programming
    trait C {
      trait D
      val d = new D {}
    }


    // c
    // d
    // BOOLEAN
    // true
    // false

  }

}
