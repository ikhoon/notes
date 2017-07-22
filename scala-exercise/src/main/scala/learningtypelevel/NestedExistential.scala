package learningtypelevel

/**
  * Created by ikhoon on 2016. 9. 4..
  * http://typelevel.org/blog/2015/07/27/nested-existentials.html
  * This is the fifth of a series of articles on “Type Parameters and Type Members”
  * # Nested existentials
  */
object NestedExistential {

  import MList._
  val estrs: MList = MCons("hi", MCons("bye", MNil())): MList.Aux[String]
  val eints: MList = MCons(1, MCons(2, MNil())): MList.Aux[Int]
  val ebools: MList = MCons(true, MCons(false, MNil())): MList.Aux[Boolean]
  val elists: PList[MList] = PCons(estrs, PCons(eints, PCons(ebools, PNil())))


//  def plenLength(xss: PList[PList[_]]): Int = plenLengthTP(xss)
/**
Error:(13, 47) no type parameters for method plenLengthTP: (xss: PList[PList[T]])Int
exist so that it can be applied to arguments (PList[PList[_]])
 --- because ---
argument expression's type is not compatible with formal parameter type;
 found   : PList[PList[_]]
 required: PList[PList[?T]]
Note: PList[_] >: PList[?T], but class PList is invariant in type T.
You may wish to define T as -T instead. (SLS 4.5)

*/

  import Existential._
  def plenLengthTP[T](xss: PList[PList[T]]): Int =
    xss match {
      case PNil() => 0
      case PCons(h, t) => plengthT(h) + plenLengthTP(t)
    }


  // 스칼라에만 있는 `forSome` existential 구분자를 사용해보자.

  def plenLengthE(xss: PList[PList[E]] forSome { type E }): Int =
    plenLengthTP(xss)

  // 컴파일 된다.
  // PList[PList[_]]는 안되는데 PList[PList[E]] forSome { type E } 는 된다.
  // 뭔 차이인가?

  // plenLength가 plenLengthTP 를 호출할 수 없는건 이글에서 다루기에는 복잡한 이유란다.


  // 이함수의 대한 테스트는 `NestedExistentialSpec`에서 해봄
  def badLength: (PList[E] => Int) forSome { type E } = plengthE


  /////
  // 다시 타입 멥버로 돌아가서

  def mlenLengthTP[T](xss: PList[MList.Aux[T]]): Int =
    xss match {
      case PNil() => 0
      case PCons(h, t) => mlength(h) + mlenLengthTP(t)
    }

  // 컴파일 되지 않는다.
//  def mlenLength(xss: PList[MList]): Int = mlenLengthTP(xss)
  def mlenLength(xss: PList[MList]): Int = ???

//  Error:(59, 57) type mismatch;
//  found   : PList[MList]
//  required: PList[MList.Aux[this.T]]
//  (which expands to)  PList[MList{type T = this.T}]
//    Note: MList >: MList.Aux[this.T], but class PList is invariant in type T.
//    You may wish to define T as -T instead. (SLS 4.5)

  // MList와 MList { type T = E } forSome { type E } 는 아래 코드에 의해서 같은걸 증명할수 있다
  implicitly[MList =:= MList { type T = E } forSome { type E }]

  // 이걸 이용해서 위의 코드를 수정해서 다시 컴파일할수 있다.
  def mlenLengthE(xss: PList[MList.Aux[E]] forSome { type E }) =
    mlenLengthTP(xss)


  // 우리는 `mlenLengthE`, `mlenLengthTP`가 있지만 둘다 `mlenLength`에 사용할수 없다.
  // 어느쪽도 다른것보다 더 일반화되지 못했다.
  // 정말 원하는건 위의 3함수도다 더 일반화된 함수이다.

  // 아래 두가지의 다른 형태의 함수가 있다.

  // 하나는 반만 type paramterized 된것이다.
  def mlenLengthTP2[T <: MList](xss: PList[T]): Int =
    xss match {
      case PNil() => 0
      case PCons(h, t) => mlength(h) + mlenLengthTP2(t)
    }

  // 또다른 하나는 전체가 existential 한것이다.
  def mlenLengthE2(xss: PList[_ <: MList]): Int =
    xss match {
      case PNil() => 0
      case PCons(h, t) => mlength(h) + mlenLengthE2(t)
    }

  // full existential for mlenLengthE2의 `PList[_ <: MList]`는 아래와 같이 표현된다.
  type PListE2 = PList[E] forSome {
    type E <: MList {
      type T = E2
    } forSome { type E2 }
  }


}
