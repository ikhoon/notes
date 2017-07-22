package learningtypelevel

import scala.collection.mutable.ArrayBuffer

/**
  * Created by ikhoon on 2016. 8. 28..
  * http://typelevel.org/blog/2015/07/16/method-equiv.html
  * This is the second of a series of articles on “Type Parameters and Type Members”.
  * # When are two methods alike?
  */
object MethodEquivalence {

  // #1
  def copyToZero(xs: ArrayBuffer[_]) : Unit = {
//    xs += xs(0)   // 컴파일 안된다...
  }
  // Error:(8, 13) type mismatch;
  // found   : (some other)_$1(in value xs)
  // required: _$1(in value xs)
  // xs += xs(0)

  // 에러메시지가 당체 뭘 말하는지는 잘 모르겠다.

  // #3
  // 다행히도? Java와 Scala에는 existential을 method type parameter으로 lifting할수 있는 `equivalent` method type이 있다.
  def copyToZeroE(xs: ArrayBuffer[_]): Unit =
    copyToZeroP(xs)

  def copyToZeroP[T](xs: ArrayBuffer[T]): Unit = {
    val zv: T = xs(0)
    xs += zv
  }

  // 위의 코드는 컴파일이 된다. 실행도 해봐야겠다.
  // 실행도 잘된다. 결과도 예상대로 나온다.


  // #5
  // 다시 컴파일 오류나던 copyToZero를 로컬 변수를 선언해서 다시 만들어보자.
  def copyToZeroWithLV(xs: ArrayBuffer[_]): Unit = {
    val zv = xs(0)
//    xs += zv   // 역시나 컴파일 되지 않는다.
  }

  // Error:(37, 11) type mismatch;
  // found   : zv.type (with underlying type Any)
  // required: _$3
  // xs += zv

  // 변수에 대한 타입 추론이 전혀 도움이 되지 않는다.
  // scala는 xs를 existential type으로 됨으로 간주하고 zv와 xs의 관계를 끊음으로서
  // xs에서 독립적으로 zv의 정의를 만든다. 이로 인해 zv의 타입은 없어지고 Any로 추론된다.


  // existential variant를 구현하기 위해서 type-parameterized variant를 호출했다.
  // 이는 단지 equivalent method type를 이용하여 컴파일러에게 도움을 주었을뿐이다.

  // 앞의 단순한 케이스에서 보았듯이, `scalac`과 `javac` 모두 추론을 관리하기 위해서 type `T`는 existential 이어야한다?
  // 직접 쓸 수 없는 함수를 method equivalance와 generality는 아를 작성하고, 안전하게 만들어주는것이 가능하다.



  // #6 뭔가 어려운 말이 계속 나옴

  // wildcard는 잘못된 표현이다..

  def pdropFirst[T](xs: PList[T]): PList[T] =
    xs match {
      case PNil() => PNil()
      case PCons(_, t) => t
    }

  def mdropFirstT[T0](xs: MList { type T = T0 }): MList { type T = T0} = {
    import MList._
    xs.uncons match {
      case None => MNil()
      case Some(c) => c.tail
    }
  }

  // refinement를 잘라내보자. 컴파일이 되는듯 하다.
  def mdropFirstE(xs: MList): MList = {
    import MList._
    xs.uncons match {
      case None => MNil()
      case Some(c) => c.tail
    }
  }

  // 확실히 더 깔끔하고 좋아보인다.
  // 그런데 `mdropFirstE`는 `xs.T`를 type paramter로 `mdropFirstT`에 전달함으로서 이함수를 이용해 구현할수 있다.
  def mdropFirstEUsingP(xs: MList): MList = {
    mdropFirstT[xs.T](xs)
  }
  // 그런데 반대로는 되지 않는다.
  // `mdropFirstT` 는 <m `mdropFirstE` 혹은 mdropFirstT는 엄격히 더 일반적이다.


  // 아래 `mdropFirstE1`의 경우 type parameter 인자값 `T0`와 반환된는값 `Int`사이의 올바른 관계를 만드는것이 실패했다.
  def mdropFirstE1[T0](xs: MList): MList = {
    import MList._
    MCons[Int](42, MNil())
  }

  // 강한 type 제약이 있는 `mdropFirstT`의 경우 이런 행위들을 금지하게 한다.

  // 중간에 뭐라뭐라 어려운 말 나옴... ㅡㅡ;;

  // #7
  def goshWhatIsThis[T](t: T): T = t
//    = null  // 컴파일 되지 않음
  // Error:(105, 36) type mismatch;
  // found   : Null(null)
  // required: T

  // 그러나 자바버전은 된다.

  // 그리고 자바버전을 holdOnNow를 이용해서 goshWhatIsThis을 구현할수 있다.

  def goshWhatIsThis1[T](t: T): T = MethodEquivalenceJava.holdOnNow(t)

  // 그리고 반대로?도 된다 그러면 그들은 equivalent 이다. 그러나 타입이 null은 반환을 할수 없다한다.

  // 자바의 generic type은 class type만 젹용될수 있기 때문에 이문제로 자바는 암묵적으로 upper bound를 넣는다.
  // scala에서는 `[T <: AnyRef]`와 같은 의미가 된다.
  // 만약 이 제약을 scala에 넣는다면 우리는 에러를 볼수 있다.

  def holdOnNow[T <: AnyRef](t: T): T = MethodEquivalenceJava.holdOnNow(t)
//  def goshWhatIsThis2[T](t: T): T = holdOnNow(t)   // 컴파일 안됨
  // Error:(124, 37) inferred type arguments [T] do not conform
  // to method holdOnNow's type parameter bounds [T <: AnyRef]


  // 아 어렵다... 나중에 다시 읽고 정리해봐야겠다.





}
