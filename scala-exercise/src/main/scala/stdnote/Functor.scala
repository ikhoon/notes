package stdnote

/**
  * Created by Liam.M on 2018. 09. 10..
  */



// higher kind
// List => Int => List[Int]
// * -> *
// List[Int] : 완전한 타입
// Either[A, B]
// Either[String, Int]
// Either => String => Int => Either[String, Int]
// * -> * -> *
class Foo(a: Int) // a: Int => class constructor

// 내가 변하는게 아니라 내가 가지고 있는걸 변한다.
// List(1, 2, 3).map(_.toString) => List("1", "2", "3")
// F[A] : List[Int]
// B : String
// f : _.toString


// def a
// def b
// def ab =>
// 추상화 vs 일반화



// ex
// F map : 안에 있는걸 바꾼다.
// List map  : 모든 원소를 바꾼다.
// Option map : 값이 null이 아닐때만 바꾼다.
// Try map : 값이 성공일때만 바꾼다.
// Either map : 오른쪽 값만 바꾼다.
// Future map : 비동기의 값이 완료되었을때만 바꾼다.




// typeclass
// 행위를 정의 한다.
// convert, mapping






object FunctorApp {

}
