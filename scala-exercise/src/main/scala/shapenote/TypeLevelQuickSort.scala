package shapenote

/**
  * Created by ikhoon on 12/07/2017.
  * shapeless 집중 공부 시간을 가져보자.
  * http://jto.github.io/articles/typelevel_quicksort/
  * 여기 글에 나오는 quick sort를 typelevel로 구현해보자.
  */

object TypeLevelQuickSort {
  sealed trait Nat
  // 0은 이미 존재하니까 피해서 _0으로 0 타입을 표현하자
  final class _0 extends Nat

  // 모든 자연수는 n은 다른수 n의 successor 이다.
  final class Succ[P <: Nat]() extends Nat

  // 위의 선언으로 모든 수를 표현할수 있다.
  type _1 = Succ[_0]
  type _2 = Succ[_1]
  type _3 = Succ[_2]
  type _4 = Succ[_3]
  type _5 = Succ[_4]

  // 기본 계산(위키피디아 참조했다함
  // a + 0 = a
  // a + succ(b) = succ(a + b)

  // 두개의 자연수를 받아서 다른 자연수 Out를 반환한다.
  // dependent type을 사용하였다.
  // Out은 A와 B에 의존한다.
  // 다른 말로 하면 스칼라 컴파일러에게 A와 B를 주면 Out이 무엇인지 알아낸다.
  // A + B = Out을 표현한다.
  trait Sum[A <: Nat, B <: Nat] { type Out <: Nat }

  object Sum {
    def apply[A <: Nat, B <: Nat](implicit sum: Sum[A, B]): Aux[A, B, sum.Out] = sum

    type Aux[A <: Nat, B <: Nat, O <: Nat] = Sum[A, B] {type Out = O}

    // 이건 정의의 base case 이다. 어떤 자연수에 대해서도 0 + b = b 이다.
    implicit def sum1[B <: Nat]: Aux[_0, B, B] = new Sum[_0, B] {
      type Out = B
    }


    // 이것이 말하는 바는 자연수 A, B에 대해서
    // Succ(A) + B = A + Succ(B)
    // 이것은 위키피디아에서 이야기 한것과 정확하게 일치하지 않는다.
    // A + Succ(B) = Succ(A + B)
    // 유사하게, Succ(A) + B = Succ(A + B)
    // 그러므로 A + Succ(B) = Succ(A + B) = Succ(A) + B
    implicit def sum2[A <: Nat, B <: Nat]
      (implicit sum: Sum[A, Succ[B]]): Aux[Succ[A], B, sum.Out] =
      new Sum[Succ[A], B] { type Out = sum.Out }


    // 그러면 3 + 1은 어떻게 동작하겠는가?
    // Sum[_3, _1]을 호출하면
    // 1. apply함수의 implicit sum: Sum[_3, _1]을 찾게 된다.
    // 그러면 sum2에서 Aux[Succ[Succ[Succ[_0]]], Succ[_0], sum.Out]으로 반환되는 타입을 찾는다.
    // 그타입은 sum2 함수 의 implicit sum: Sum[_2, Succ[_1]]의 형태의 타입을 찾는다.
    // 그러면 다시 sum2를 호출하게 되고 재귀적으로 Sum[_0, _4]가 될때까지 찾는다.
    // Sum[_0, _4]가 되면 sum1 함수를 호출하고 재귀 호출은 끝이나게 된다.

    // 기본적인 산술 연산은 타입 시스템으로 할수 있다.
  }

  // quick sort를 구현하기 위해서는 자연수를 비교할수 있어야 한다.
  trait LT[A <: Nat, B <: Nat]

  object LT {
    def apply[A <: Nat, B <: Nat](implicit lt: A < B): LT[A, B] = lt

    type <[A <: Nat, B <: Nat] = LT[A, B]

    implicit def lt1[B <: Nat]: _0 < Succ[B] = new (_0 < Succ[B]) {}

    // 이함수를 읨의의 값 A가 B보다 작을때만 컴파일이 된다.
    // 최종적으로 A = _0, B = _1 혹은 더 큰수 일때 implicit lt를 찾울수 있기 때문이다.
    implicit def lt2[A <: Nat, B <: Nat]
      (implicit lt: A < B): Succ[A] < Succ[B] = new (Succ[A] < Succ[B]) {}

  }

  // quick sort를 구현하기 위해서는 ≥ 연산자도 필요하다.
  // 하지만 shapeless는 이것을 제공해주지 않는다. ≤ 만 제공해준다.
  // 대신 저걸 쓰면 된다.
  trait LTEq[A <: Nat, B <: Nat]

  object LTEq {
    def apply[A <: Nat, B <: Nat](implicit lteq: A <= B): LTEq[A, B] = lteq

    type <=[A <: Nat, B <: Nat] = LTEq[A, B]

    implicit def lteq1[B <: Nat] : _0 <= B = new (_0 <= B) {}

    implicit def lteq2: _0 <= _0 = new (_0 <= _0) {}

    implicit def lteq3[A <: Nat, B <: Nat](implicit lteq: A <= B): Succ[A] <= Succ[B] =
      new (Succ[A] <= Succ[B]) {}
  }

  // 스칼라의 List는
  object list {
    sealed abstract class List[+A]
    case object Nil extends List[Nothing]
    // head와 tail의 내부 타입이 같아야 한다.
    final case class ::[B](head: B, tail: List[B]) extends List[B]
  }

  // type level의 list는 HList
  object hlist {
    sealed trait HList
    final class ::[+H, +T <: HList] extends HList
    final class HNil extends HList
    // type level의 자연수의 list는
    // 여기서는 hlist를 쓸수 밖에 없다.
    // 왜냐면 그냥 list를 사용하면 타입 정보가 다 날라갈것이다.
    type NS = _1 :: _0 :: _3 :: _2 :: HNil
  }

}
