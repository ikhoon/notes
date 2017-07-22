package learningtypelevel

/**
  * Created by ikhoon on 2016. 8. 28..
  * http://typelevel.org/blog/2015/07/13/type-members-parameters.html
  * This is the first of a series of articles on “Type Parameters and Type Members”.
  * Type members are [almost] type parameters
  */


// type member
class Blar {
  type Member
}

// type parameter
class Blar2[Param]

// 위에 두개는 다른점보다는 유사한 점이 더 많다.

// type parameter가 대부분의 경우에 더 편하다. 타입을 existentially하게 쓰는 경우에는 아마 type member가 더 좋을것이다.

// 두개의 버전을 List를 구현을 통해서 비교해본다.
// 1. type parameter 버전
sealed abstract class PList[T]
final case class PNil[T]() extends PList[T]
final case class PCons[T](head: T, tail: PList[T]) extends PList[T]

// 2. type member 버전
sealed abstract class MList { self =>
  type T
  def uncons : Option[MCons { type T = self.T }]
}

sealed abstract class MNil extends MList {
  override def uncons = None
}

sealed abstract class MCons extends MList { self =>
  val head : T
  val tail : MList { type T = self.T }
  override def uncons = Some(self: MCons { type T = self.T })
}

// type parameter를 만들때 보다 훨 복잡하다. 그리고 아직 instance를 생성할수 있는 코드도 없다.

// 생성자 만들기, 함수를 통해 들어온 type parameter를 type member로 전달한다.
object MList {
  type Aux[T0] = MList { type T = T0 } // part 3에서 추가됨.

  def MNil[T0](): MNil { type T = T0 } =
    new MNil {
      type T = T0
    }

  def MCons[T0](hd: T0, tl: MList { type T = T0 }): MCons { type T = T0 } =
    new MCons {
      type T = T0
      val head: T0 = hd
      val tail: MList { type T = T0 } = tl
    }
}

// { type T = ... } 를 항상 달고 다녀야 하나?

// MCons의 tail에다가 정보를 type정보를 빼보자.
sealed abstract class MList2 { self =>
  type T
  def uncons : Option[MCons2 { type T = self.T }]
}

sealed abstract class MNil2 extends MList2 {
  override def uncons = None
}

sealed abstract class MCons2 extends MList2 { self =>
  val head : T
  val tail : MList2  // 여기에 type member 정보가 없앴다.
  override def uncons = Some(self: MCons2 { type T = self.T })
}

object MList2 {
  def MNil2[T0](): MNil2 {type T = T0} =
    new MNil2 {
      type T = T0
    }

  def MCons2[T0](hd: T0, tl: MList2 { type T = T0 }): MCons2 { type T = T0 } =
    new MCons2 {
      type T = T0
      val head: T0 = hd
      val tail: MList2 { type T = T0 } = tl
    }
}

// 그럼 테스트 해보자.
// 테스트는 TypeMemberVsTypeParameterSpec.scala에서 해봄.

// MCons2는 특정 동작에 대해서 컴파일 오류를 발생한다.
// MList라고 표현하는건 type parameter에서 PList[_]로 표현하는것과 같다.
// 함수형 언어에서는 이걸 existential이라 말한다. 용어자체가 잘 와 닿지는 않지만 자바의 List<?>의 와일드 카드와 같은 개념이라 생각하면 면된다.
// 트위터 스칼라 스쿨에도 이를 List[_], List[T forSome { type T }] 같은 표현을 와일드 카드라 표현하였다.
// 참조 : https://twitter.github.io/scala_school/ko/type-basics.html#quantification
// 한국어로 적절한 표현이 무엇일까? 고민....

// 지금은 용어 그래도 써보기로 한다. 고유명사처럼..

// 언제 existential이 OK? 괜찮은걸까?
// existential 버전으로 유용한 함수를 만들수 있다.

object Existential {
  // type member existential version
  def mlength(xs: MList): Int =
    xs.uncons match {
      case None => 0
      case Some(c) => 1 + mlength(c.tail)
    }

  def mlength(xs: MList2): Int =
    xs.uncons match {
      case None => 0
      case Some(c) => 1 + mlength(c.tail)
    }

  // type parameter version
  def plengthT[T](xs: PList[T]): Int =
    xs match {
      case PNil() => 0
      case PCons(_, t) => 1 + plengthT(t)
    }

  // type parameter existential version
  def plengthE(xs: PList[_]): Int =
    xs match {
      case PNil() => 0
      case PCons(_, t) => 1 + plengthE(t)
    }

  // existential 방식? 을 쓸 수 있는건 간단한 rule을 만족하면 된다.
  // 1. type parameter가 argument에 한번만 나타날때
  // 2. 그리고 result type에는 없을때.

  // mlength, plengthT, plenghtE의 테스트는 TypeMemberVsTypeParameterSpec.scala에서 또 해봄.

}

