package shapeguide


import shapeless.Generic.Aux

// 1.1 무엇이 제너릭 프로그래밍인가?

object generic {

  // 스칼라 프로그래머로서 우리는 타입을 사용한다. 타입은 특별하기 때문에 유용하다.
  // 우리가 다른 코드의 조각을 어떻게 사용하는지 알려주고 버그가 생기는것을 막아준다.
  // 그리고 코딩할때 문제 해결을 위한 가이드를 해준다.
  // 아래와 같은 타입이 있다.
  case class Employee(name: String, number: Int, manager: Boolean)
  case class IceCream(name: String, numCherries: Int, inCone: Boolean)

  // 두개의 case class는 다른 종류의 데이터이지만 명확히 비슷한점을 가지고 있다.
  // 두개 모두 같은 타입의 필드를 가지고 있다.
  // csv file로 serialize하는 기능을 만들고 싶다면 두개의 타입의 유사점에도 불구하고 두개의 다른 serialization 함수를 만들어야 한다.

  def employeeCsv(e: Employee): List[String] =
    List(e.name, e.number.toString, e.manager.toString)

  def iceCreamCsv(c: IceCream): List[String] =
    List(c.name, c.numCherries.toString, c.inCone.toString)

  // 제너릭 프로그래밍은 위와 같은 경우를 해결하수 있게 한다.
  // Shapeless는 특정타입을 제너릭하게 변경시킴으로서 공통의 코드로 이를 조작할수 있게한다.
  // 예를 들어 Employee와 IceCream의 값을 같은 타입으로 변경시키는 코드를 사용할수 있다.

  import shapeless._

  val genericEmployee = Generic[Employee].to(Employee("ikhoon", 12, false))

  val genericIceCream = Generic[IceCream].to(IceCream("barbambar", 0, false))

  def genericCsv(gen: String :: Int :: Boolean :: HNil): List[String] =
    List[String](gen(0), gen(1).toString, gen(2).toString)



}

object AlgebraicDataType {

  // 제너릭 프로그램의 주된 아이디어는 작은 제너릭 코드로 넓고 다양한 타입 문제를 푸는것이다.

  // shapeless는 다음 두가지의 툴을 제공한다.
  // 1. 제너릭 타입의 셋은 타입 레벨에서 검사(inspect)하고 이동(traverse)하고 조작(manipulate) 할수 있다.
  // 2. Algebraic Data Type(스칼라에서는 case class와 sealed trait으로 인코딩된다)과 제너릭 표현 사이의 자동적인 맵핑

  // 이 챕터에서는 ADT에 무엇인지 알아보고 이것이 왜 스칼라 개발자에게 익숙한지 알아보는걸로 시작한다.
  // 그리고 shapeless에서 제너릭 표현을 위해서 사용하는 데이터 타입을 보고 그것들이 어떻게 완전한 ADT로 mapping되는지 알아본다.
  // 마지막으로 ADT와 제너릭 사이를 자동으로 서로 mapping을 제공하는 Generic을 소개할것이다.
  // 제너릭을 이용하여 특정타입에서 다른 타입으로 변경하는 간단한 예제를 통해서 마무리 할것이다.

  // 2.1 Recap : Algebraic Data type
  // Algebraic Data Type은 팬시한 이름을 가진 함수형 프로그래밍의 컨샙이지만 단순한 뜻이다.
  // "and"와 "or'를 이용해 데이터를 표현하는 관용적인 방법이다. 예를 들어
  // * 도형은 사각형 `or` 원 이다.
  // * 사각형은 가로 `and` 높이를 가지고 있다
  // * 원은 반지름을 가지고 있다.

  // ADT 용어에서 `and types`는 products라 불리는 rectangle과 circle이다.
  // `or types`는 coproducts라 불리는 shape이다.

  sealed trait Shape
  final case class Rectangle(width: Double, height: Double) extends Shape
  final case class Circle(radius: Double) extends Shape

  val rectangle = Rectangle(3.0, 4.0)
  val circle = Circle(1.0)

  // ADT의 아름다움은 완전히 type safe하다는 것이다.
  // 컴파일러는 우리가 정의한 algebra들은 완전히 알고 있다.
  // 이것은 우리의 타입과 관련된 타입 관련 함수를 완벽하고 올바르게 작성할수 있도록 도와준다.

  def area(shape: Shape): Double =
    shape match {
      case Rectangle(w, h) => w * h
      case Circle(r) => math.Pi * r * r
    }

  // 2.1.1 대안적인 인코딩
  // sealed trait과 case class는 의심할 여지가 없이 스칼라에서 가장 편리하게 ADT를 인코딩한다.
  // 그러나 유일한 encoding은 아니다.
  // 스칼라에서는 generic product는 Tuple의 형태로 제공을 하고 generic coproduct는 Either의 형태로 제공한다.
  // 우리는 Shape를 encoding하기 위해서 이것을 선택할수 있다.

  type Rectangle2 = (Double, Double)
  type Circle2 = Double
  type Shape2 = Either[Rectangle2, Circle2]

  val rect2: Shape2 = Left((3.0, 4.0))
  val circle2: Shape2 = Right(1.0)

  // 이 encoding은 case class보다 가독성이 떨어지지만 몇가지 매력적인 특징은 그대로 가지고 있다.
  // 우리는 여전히 Shape2와 관련되어 완전히 type safe한 연산을 구현할수 있다.

  def area2(shape: Shape2): Double =
    shape match {
      case Left((w, h)) => w * h
      case Right(r) => math.Pi * r * r
    }

  // 중요한것은 `Shape2`가 `Shape`보다 general한 encoding이다.
  // 어떤 두개의 Double쌍은 Rectangle2에 대해서 동작하고 반대로도 마찬가지 이다.
  // 스칼라 개발자로서 상호 연산은 나쁘것이라 보는 경향이 있다.
  // 이런 자유로움이 우리의 코드베이스에 의도치 않게 발생하는 어떤 재앙이 될것인가?
  // 그러나 어떤 경우에는 이것은 유용하다.
  // 예를 들어 data를 disk로 직렬화 한다면, 우리는 Rectangle2와 Double의 쌍을 차이점을 고려할 필요가 없다.
  // 단지 두개의 숫자를 쓰고 읽기만 하면 끝난다.

  // shapeless는 두개의 세상에 모두에 대해서 최선의 선택을 준다.
  // sealed trait과 case class를 기본으로 사용하고 상호운용이 필요할때는 제너릭 표현으로 변환하면 된다.
  // 그러나 Tuple과 Either를 사용하는 대신에 shapeless는 제너릭 products와 coproducts를 표현하기 위해서 자체 데이터 타입을 사용한다.


  // 2.2 Generic product encoding
  // 앞에서는 제너릭 products 표현을 위해서 tuple들을 사용하였다.
  // 불행히도 스칼라의 내장된 tuple들은 몇가지 단점을 가지고 있고 그것이 shapeless의 목적에 적합하지 않게 한다.

  // * 각각의 사이즈의 tuple(Tuple1 ~ Tuple22)은 다르고 관련이 없는 타입이다. 이것은 사이를 넘어서는 추상화를 할때 코드를 작성하기 힘들게 한다.
  // *  0-length, 1-length tuple은 사이즈가 없다. 이것은 0개의 필드와 1개의 필드의 products를 표현하는데 중요하다.

  // 이런 이유 때문에 shapeless는 heterogeneous list 혹은 HList라 불리는 다른 제너릭 encoding을 사용하고 있다.
  // HList는 각각의 element의 타입이 전체 리스트의 타입에 관리되어 지는것을 제외하고는 일반적인 list와 유사하다.

  import shapeless.{HList, ::, HNil}
  val product: String :: Int :: Boolean :: HNil = "Sunday" :: 1 :: false :: HNil

  // HList의 type과 value는 서로 반영한다. 둘다 String, Int, Boolean 멤버를 표현한다.
  // 이타입들의 head와 tail을 탐색할수 있고 element의 타입은 보존이 된다.

  val first: String = product.head
  val second: Int = product.tail.head
  val rest: Boolean :: HNil = product.tail.tail

  // 컴파일러는 각각의 HList의 정확한 길이를 알고 있다. 그렇기 때문에 empty list의 head나 tail은 컴파일 에러를 유발할것이다.

//  product.tail.tail.tail.head  ==> 컴파일 에러 난다잉

  // 조사하고 탐색하는것 이외에 HList를 조작하고 변형할수 있다.
  // 예를 들어 앞에 `::` 함수와 함께 element를 추가할수 있다.
  // 다시 결과 타입은 elememts의 숫자와 타입들의 어떻게 반영하는지 알아보자

  val newProduct: Long :: String :: Int :: Boolean :: HNil = 42L :: product

  // 또한 shapeless는 mapping, filtering, concatenating list 등과 같은 보다 복잡한 연상을 할수 있는 툴을 제공한다.


  // 2.2.1 표현을 `Generic`을 사용하는걸로 변경
  // shapeless는 Generic이라 불리는 type class를 제공한다. 이것은 완벽한 ADT과 제너릭 표현사이에 서로 바뀌는것을 할수 있게 한다.
  // 여기에서는 매크로의 마법이 우리로 하여금 제너릭 인스턴스를 추가코드 없이 얻는것을 가능하게 한다.

  import shapeless.Generic

  case class IceCream(name: String, numCherries: Int, inCorn: Boolean)
  val iceCreamGen = Generic[IceCream]
  // iceCreamGen: shapeless.Generic[IceCream]{type Repr = shapeless.::[String,shapeless.::[Int,shapeless.::[Boolean,shapeless.HNil]]]} = anon$macro$4$1@29ed4101

  // 제너릭 인스턴스의 타입 멤버 Repr은 타입의 제너릭 표현을 포함하고 있는것에 주목하라.
  // 이경우 iceCreamGen.Repr은 String :: Int :: Boolean :: HNil
  // 제너릭의 인스턴스는 두가지의 함수를 가지고 있다. 하나는 Repr로(to) 변경하는것과 이로부터 복구하는것 이다.

  val iceCream = IceCream("Sundae", 1, false)

  val repr: iceCreamGen.Repr = iceCreamGen.to(iceCream)

  val iceCream2: IceCream = iceCreamGen.from(repr)

  // 만약 두개의 ADT가 같은 Repr을 가지고 있다면 그것들의 제너릭을 이용하여 두개 사이를 서로 변환할수 있다.

  case class Employee(name: String, number: Int, manager: Boolean)
  // ice cream으로 부터 employee를 생성하였다.
  val strangeEmployee = Generic[Employee].from(repr)


  // 2.3 제너릭 coproducts
  // 이제 우리는 shapeless가 product 타입들을 어떻게 하는지 알았다. coproducts는 어떠한가??
  // 앞에서 Either를 보았지만 그것이 tuple과 유사한 단점들을 겪게 된다.
  // 두개의 타입보다 작은 것에 대해서 disjunction을 표현하는 방법이 없다.
  // 이런 연유 때문에 shapeless는 HList와 유사한 것을 제공한다

  // Coproduct 타입은 disjuction에 있는 가능한 모든 타입을 인코딩한다.
  // 하지만 각각의 구체적인 인스턴스는 가능성있는것중에 하나의 값만 포함하고 있다.

  case class Red()
  case class Amber()
  case class Green()

  import shapeless.{:+:, CNil}
  type Light = Red :+: Amber :+: Green :+: CNil

  // 제너릭 coproduct 타입은 A :+: B :+: C :+: CNil 의 타입을 취한다. 이것의 의미는 A or B or C 이다.
  // :+: 은 Either로 느슨하게 해석되어 질수 있고, 하위 타입인 Inl과 Inr은 Left와 Right에 느슨하게 대응한다.
  // CNil은 Nothing과 유사하게 값을 가지고 있지 않는 빈 타입이다. 이로인해 빈 coproduct는 인스턴스화 할수는 없다.
  // 이와 유사하게 순수하게 Inr의 인스턴스로만 coproduct를 생성할수는 없다.
  // 항상 하나의 값에는 정확히 하나의 Inl이 있다.

  import shapeless.{Inl, Inr}
  val red: Light = Inl(Red())
  val green: Light = Inr(Inr(Inl(Green())))


  // 2.3.1 제너릭을 이용한 인코딩으로의 변환
  // Coproduct의 타입은 첫인상에 파싱하기 어렵다.
  // 그러나 그것들이 제너릭 인코딩의 큰그림에 어떻게 맞추어 가는지 보면 상대적으로 쉽다.
  // case class와 case object를 이해하기 위해서 추가적으로 shapeless의 Generic type class는 sealed trait과 abstract class를 이해한다.
  // 어떻게?? macro인가봉가?

  object generic {
    sealed trait Shape
    final case class Rectangle(width: Double, height: Double) extends Shape
    final case class Circle(radius: Double) extends Shape
    val gen = Generic[Shape]
    // gen: shapeless.Generic[Shape]{type Repr = shapeless.:+:[Rectangle,shapeless.:+:[Circle,shapeless.CNil]]} = anon$macro$1$1@3b97a8f1
    // trait으로 Generic을 만들면 Coproduct가 된다. awesome


  }









}
