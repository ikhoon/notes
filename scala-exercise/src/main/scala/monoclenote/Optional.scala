package monoclenote

import monocle.{Lens, Optional}

/**
  * Created by ikhoon on 06/03/2018.
  */
class OptionalExample {

  // `Optional`은 Product를 들여다 볼수 있다. case class, Tuple, HList or even Map
  // Product를 들여다 볼수 있는건 기존에 Lens가 있었다.
  // `Optional`은 값이 없을수 있다는 것에 조금더 초점을 맞추었다.

  // 일반적인 case class에는 크게 의미가 없는 구조라 생각이 든다.
  case class Foo(a: Int, b: String, c: Boolean)

  // 값이 무조건 Some이 될것이기 때문이다.
  Optional[Foo, Int](s => Some(s.a))(a => f => f.copy(a = a))
  // 물론 case class의 값의 자료구조형이 Option[A] 타입일경우 에는 조금 달라질것이다.

  // Lense로 Optional을 표현할수 있다.
  def toOptional[S, A](lens: Lens[S, A]): Optional[S, A] =
    Optional[S, A](s => Some(lens.get(s)))(a => s => lens.set(a)(s))


  // 진정 유용함은 List와 같은 자료 구조형을 표현할때인것 같다.
  val head = Optional[List[Int], Int] {
    case Nil => None
    case x :: xs => Some(x)
  }{ a => {
    case Nil => Nil
    case x :: xs => a :: xs
  }
  }
  head.nonEmpty(List(1, 2))
}
