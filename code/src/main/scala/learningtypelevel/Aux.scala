package learningtypelevel

/**
  * Created by ikhoon on 2016. 8. 29..
  * http://typelevel.org/blog/2015/07/19/forget-refinement-aux.html
  * This is the third of a series of articles on “Type Parameters and Type Members”
  * # What happens when I forget a refinement?
  * 글 읽고 따라해봄
  */
object Aux {
  // refinement를 뺐을때 무엇이 일어날까?

  // 앞에서 `MList`를 가지고 에러를 내면서 했다면 이번엔 `PList`를 가지고 해보자.

//  def pdropFirst(xs: PList) = ??? // 컴파일 안됨
  def pdropFirst[T](xs: PList[T]) = ???
  // Error:(9, 22) class PList takes type parameters

  // PList에 type parameter가 없기 때문에 컴파일 오류가 발생한다.

  // 또다른 실수를 발생시켜보자.
  // 앞에서 type parameter, member를 T로 선언했다.
  // 이는 자바에서는 일반적인 컨벤션이지만 스칼라에서는 일반적이지 않다.
  // 스칼라에서는 관용적으로 A를 이용해서 type parameter나 member를 표현한다. F[A], F[A, B] 등등등..

  def mdropFirstE2[T0](xs: MList { type A = T0 }) = {
    import MList._
    xs.uncons match {
      case None => MNil()
      case Some(c) => c.tail
    }
  }

  // 위의 코드는 컴파일은 된다. 그러나 실행을 하면...

  // mdropFirstE2(MNil[Int]())
  // Error:(12, 29) type mismatch;
  // found   : MNil{type T = Int}
  // required: MList{type A = ?}

  // 타입이 맞지 않다는 에러가 발생한다.

  // MList는 type member A가 없다.
  // `MList {type A = T0}`에서 type member A은 다른 trait에서 MList로 mixin되거나 inner subclass 으로 부터 올수 있다.
  // 어떤것은 sealed 속성이나 final 가진 것들은 instant화 될수 없고 아니면 그외의 것은 부적절한? 것이 된다.

  // 값(value)가 없는 타입들은 자바와 스칼라 양쪽 모두 의미가 있고 유용하다.

  // # 왜 T0? Aux가 뭔가?

  // `MList`의 몇몇 함수는 `T`대신 `T0`를 이용하여서 type parameter를 취해왔다.
  // 이것은 기억, 메모의 방법으로 `scalac`가 나에게 허락하면 나는 여기서 `T`를 사용하겠다고 미리 서술하는 것이다.
  // 이것은 scalaz.Unapply에서 사용한 방식을 차용했다.
  // https://github.com/scalaz/scalaz/blob/v7.1.3/core/src/main/scala/scalaz/Unapply.scala#L217

  // `T0`대신 `T`를 사용해보자.
  //  T에 대한 순한 참조가 일어나면서 컴파일 되지 않는다.
//  def MNil3[T]() : MNil { type T = T} =
//    new MNil {
//      type T = T
//    }
  //  Error:(51, 36) illegal cyclic reference involving type T

  // 이것은 scoping 문제이다. refinement type이 member `T`가 method type parameter `T`를 가리게 만든다.
  // 이문제를 `MCons#uncons`와 `MCons#tail`에서 다루었다. 이 케이스에는 외부 `T`를 `self.T`로 대체하였다.



  // type를 member와 같이 정의 할때 member를 type parameter로 바꾸기 위한 `Aux` type을 companion에 정의 해야한다.

  // `Aux`의 이름은 shapeless의 conversion에서 빌려왔다.

  // `object MList` 이것들을 추가해보자.
  // 이제 `MList { type T = Int }` 대신에 `MList.Aux[Int]`로 쓸수 있다. 깔끔하다.
  // mdropFirstT는 새로운 스타일로 함수를 다시 쓸수 있다.
  def mdropFirstT2[T](xs: MList.Aux[T]) : MList.Aux[T] = ???

  // 익숙하진 않지만 훨씬 더 세련되었다.
  // 또한 member `T`는 Aux의 type parameter의 위치에 대한 범위가 아니므로
  // 에러없이 type parameter의 이름은 `T`로 명명하고 `MList.Aux[T]`라고 쓸쑤 있다.
  // 이전에 것을 보면 이것은 type parameter의 장점이고, `PList`는 처음부터 이런 문제가 없었다.

  // `Aux`를 사용함으로서 type member를 명시하는것을 빼먹거나 오타로 인한 오류 발생을 피하는것을 도와줄것이다.

}
