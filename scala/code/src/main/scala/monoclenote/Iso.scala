package monoclenote

import monocle.{Lens, PLens, PTraversal, Traversal}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

/**
  * Created by Liam.M on 2018. 02. 07..
  */


case class Person(name: String, age: Int)

// Iso는 S와 A타입 사이에 정보의 손실없이 서로 변환될수 있다.
// Person =:= tuple (String, Int)
// tuple (String, Int) =:= Person
// 그렇기 Person과 (String, Int) 사이에는 Iso를 생성할수 있다.

object IsoExample extends App {
  import monocle.Iso
  val personToTuple = Iso[Person, (String, Int)](p => (p.name, p.age)){case (name, age) => Person(name, age)}

  // Iso는 다음 두 함수를 가지고 있다.
  // get: Person => (String, Int)
  // reverseGet (aka apply): (String, Int) => Person

  personToTuple.get(Person("Zoe", 25))
  personToTuple.reverseGet(("Zoe", 25))
  // reverseGet은 apply함수를 통해서도 호출할수 있다.
  personToTuple(("Zoe", 25))

  personToTuple.mapping


  // 아직은 그닥 쓸모없어 보인다.
  // 김은미가 만든 Converter와 별만 차이가 없다. 단방향인거 빼고는

  // 조금더 응용을 해보면 좋겠다.

  // higher kind type에도 적용이 가능하다.

  def listToVector[A] = Iso[List[A], Vector[A]](_.toVector)(_.toList)
  listToVector.get(List(1, 2, 3))

  // 그리고 기존에 있는걸 활용해서 뒤집을수 있다.
  // 요건 조금 좋아 보인다. 한번 정의 해놓은 변환을 계속 활용할수 있으니
  def vectorToList[A] = listToVector[A].reverse
  vectorToList.get(Vector(1, 2, 3))


  // 다른 예를 보자.
  val stringToList = Iso[String, List[Char]](_.toList)(_.mkString(""))
  // 이건 조금더 멋지다.
  // S => A 타입으로 변경해서 문자을 수정하고 다시 A => S 타입으로 변경해낸다.
  stringToList.modify(_.tail)
  // 스고이 하다.
  // 코드 자체는 아직 익숙하지 않지만 타입간의 변화와 조작을 한번에 넣은 추상화이다.
  // 나쁘지 않은듯





  // 이번엔 traverse
  import cats.implicits._
  val eachL = Traversal.fromTraverse[List, Person]
  val values = eachL.composeIso(personToTuple)
  val values2 = values.getAll(List(Person("김은미 주식 잘될것 같냐?", -20), Person("박민철 너도 주식 잘될것 같냐?", -30)))
  println(values2)


  // Iso generation api에 대해서 한번 보자.
  // case class와 tuple사이의 변환을 macro로 지원해준다.

  case class MyString(s: String)
  case class Foo()
  case object Bar

  import monocle.macros.GenIso

  val a = GenIso[MyString, String].get(MyString("hello"))
  println(a)

  // GenIso.unit은 필드가 없는 object나 case class에서 사용될수 있다.
  val fooUnit = GenIso.unit[Foo]
  val foo: Foo = fooUnit.reverseGet(())
  // 이게 언제 써먹을수 있을라나? 싶긴하다 아직

  val tuples: (String, Int) = GenIso.fields[Person].get(Person("John", 42))
  println(tuples)



}






