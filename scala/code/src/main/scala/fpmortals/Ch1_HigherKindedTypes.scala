package fpmortals

/**
  * Created by ikhoon on 15/11/2017.
  */
object Ch1_HigherKindedTypes {

  // Higher Kinded Types는 타입 파라메터에 타입 생성자를 사용하는것을 허락한다.
  // C[_] 같이 생겼다

  trait Foo[C[_]] {
    def create(i: Int): C[Int]
  }

  // List는 타입 생성자이다

  // List -> Int -> List[Int]

  object FooList extends Foo[List] {
    def create(i: Int): List[Int] = List(i)
  }

  // 그리고 타입 생성자가 여러개 있는 경우 type parameter hole을 생성한다.




  // Either[String, _]

  type EitherString[T] = Either[String, T]

  // 타입 alias는 새로운 타입을 만들지 않고 치환만 한다.
  // 컴파일 타입에 EitherString[T] 는 Either[String, T]로 다 바뀐다.

  object FooEitherString extends Foo[EitherString] {
    def create(i: Int): Either[String, Int] = Right(i)
  }

  // 일일이 타입 alias를 지정하기 귀찮다.
  // 이때 이름을 짖는것도 일이다.
  // 역시나 이릉을 짖지 않는건 lambda가 짱이다.
  // type에도 lambda가 있다.
  // kind projector 를 이용해보자.

  object FooEitherString2 extends Foo[Either[String, ?]] {
    def create(i: Int): Either[String, Int] = Right(i)
  }

  // 타입 생성자를 무시하고 싶을때가 있다.
  // 즉 List[_] 대신 Int를 넣고 싶을수가 있다.

  type Id[T] = T

  // Id[Int]는 Int 와 똑같은것이다.
  object FooId extends Foo[Id] {
    def create(i: Int): Id[Int] = i
  }


}
