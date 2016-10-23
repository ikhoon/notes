package shapeguide


import shapeguide.deriving.CsvEncoder
import shapeless.{:+:, ::, CNil, Coproduct, Generic, HList, HNil, Inl, Inr, Lazy}
import shapeless.Generic.Aux

// Reference : https://github.com/davegurnell/shapeless-guide

object generic {

  // 1.1 무엇이 제너릭 프로그래밍인가?

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
}

// 아래 3줄의 코드는 특정 object안에 속해있으면 nested하면 shapeless에서 컴파일을 하지 못한다. 매크로 때문인거 같다.
// 고로 top level에 sealed trait이 위치 해주어야 한다!!
sealed trait Shape3
final case class Rectangle3(width: Double, height: Double) extends Shape3
final case class Circle3(radius: Double) extends Shape3
object coproduct {
  val gen = Generic[Shape3]
  // gen: shapeless.Generic[Shape3]{type Repr = shapeless.:+:[Rectangle,shapeless.:+:[Circle,shapeless.CNil]]} = anon$macro$1$1@3b97a8f1
  // trait으로 Generic을 만들면 Coproduct가 된다. awesome


  /// 망햇다... ㅠㅠ 날라갔다.
}


object deriving {
  // 3. Automatically deriving type class instances

  // 3.1 복습: type class
  // 인스턴스의 유도에 대해서 깊게 들어가기전에 짧게 type class의 중요성에 대해서 알아본다.
  // Type class는 Haskell에서부터 가져온 프로그래밍 패턴이다.
  // ('class'란 단어는 object oriented programming의 class와는 아무런 관계가 없다. 오해말기를..)
  // 우리는 type class를 scala에서 trait과 implicit 파라메터를 통해서 구현할수 있다.
  // type class는 다양한 타입에 대해서 적용하려고 범용적 함수를 표현하고자 하는 generic trait이다.

  trait CsvEncoder[A] {
    def encode(value: A): List[String]
  }

  // 관심이 있는 타입에 대하여 인스턴스와 함께 type class 구현하면 된다.

  // 생성자에 대한 보조함수이다.
  def createEncoder[A](f: A => List[String]): CsvEncoder[A] =
    new CsvEncoder[A] {
      def encode(value: A): List[String] = f(value)
    }

  // 커스텀 타입
  case class Employee(name: String, number: Int, manager: Boolean)
  // 커스텀 타입에 대한 CsvEncoder의 인스턴스
  implicit val employeeEncoder = createEncoder[Employee](e =>
    List(e.name, e.number.toString, if(e.manager) "yes" else "no"))

  // 각각의 인스턴스에 대해서는 implicit 키워드로 표시한다.
  // 그리고 위 타입에 대응하는 implicit 파라메터를 받아들이는 제너릭 엔트리 포인트 함수(시작하는 함수)를 구현한다.

  def writeCsv[A](values: List[A])(implicit encoder: CsvEncoder[A]): String =
    values.map(value => encoder.encode(value).mkString(",")).mkString("\n")

  // 시작함수를 `호출`때 컴파일러는 해당 값의 타입 파라메터를 계산하고 타엡에 해당하는 implicit CsvWriter를 찾는다.
  val employees : List[Employee] = List(
    Employee("Bill", 1, true),
    Employee("Peter", 2, false),
    Employee("Milton", 3, false)
  )

  // 컴파일러가 CsvEncoder[Employee]를 파라메터로 주입한다.
  writeCsv(employees)

  // 우리는 implicit CsvEncoder를 정의하고 scope 에 놓아 둠으로서 어떤 타입에 대해서도 wrtieCsv를 사용할수 있다.
  case class IceCream(name: String, numCherries: Int, inCorn: Boolean)
  implicit val iceCreamEncoder: CsvEncoder[IceCream] =
    createEncoder[IceCream](i => List(i.name, i.numCherries.toString, if(i.inCorn) "yes" else "no"))

  val iceCreams: List[IceCream] = List(
    IceCream("Sundae", 1, false),
    IceCream("Cornetto", 0, true),
    IceCream("Banana Split", 0, false)
  )
  // 여기서는 컴파일러가 CsvEncoder[IceCream]을 파라메터로 주입한다.
  writeCsv(iceCreams)


  // 3.1.1 인스턴스 유도하기
  // 타입 클래스는 매우 유연하지만 고려해야 하는 모든 타입에 대해서 인스턴스를 선언하는것이 필요하다.
  // 다행이도 스칼라 컴파일러는 주어진 규칙을 통해서 인스턴스를 생성하는 몇가지 트릭이 있다.
  // 예를 들어 주어진 A, B의 CsvEncoder들을 활용하여 (A, B) 쌍의 인코더를 만들수 있다.
  // 기존에 만들어진 코드를 재활용하였다.
  implicit def pairEncoder[A, B](
    implicit aEncoder: CsvEncoder[A], bEncoder: CsvEncoder[B]
  ): CsvEncoder[(A, B)] =
    createEncoder[(A, B)]{
      case (a, b) => aEncoder.encode(a) ++ bEncoder.encode(b)
    }

  // implicit def안의 모든 파라메터가 implicit으로 선언되어 있을때
  // 컴파일러는 그것을 다른 인스턴스로 부터 인스턴스를 생성하는 유도하는 규칙으로 이용한다.
  // 예를 들어 만약 writeCsv를 호출하고 List[(Employee, IceCream)]을 넘길때
  // 컴파일러는
  // * pairEncoder
  // * iceCreamEncoder
  // * employeeEncoder
  // 를 조합하여 필요로하는 CsvEncoder[(Employee, IceCream)] 을 만들수 있다.

  writeCsv(employees zip iceCreams)

  // `implicit val`과 `implicit def`로 인코딩되어 있는 주어진 규칙들로 부터
  // 컴파일러는 조합된 대상이 필요로 하는 인스턴스를 찾는 능력을 가지고 있다.
  // "implicit derivation"으로 알려진 이 행위는 type class 패턴을 아주 강력하게 만든다.

  // 이것은 통상적으로 ADT에는 제한 되어 있다.
  // 컴파일러는 case class와 sealed trait의 별개 타입의 대해서는 선택하지 못한다.
  // 그래서 개발자는 ADT의 instance를 항상 손으로 한땀 한딴 정의해 왔다.
  // Shapeless의 제너릭 표현은 이 모든것을 바꾼다!!!!
  // 어떤 ADT라도 인스턴스를 공짜로 유도할수 있게 해준다. wow! 대박이다잉

  // 3.2 Deriving instances for products

  // 이번 섹션은 products(ie. case class)의 type class 인스턴스를 유도하기 위해서 shapeless를 사용할것 이다.
  // 2가지 직관(진실?, 사실?)을 이용할것이다.
  // 1. HList의 head의 tail의 type class instance가 있다면 우리는 전체 HList의 instance를 유도할수 있다.
  // 2. case class A, Generic[A], 제너릭의 Repr의 type class 인스턴스가 있다면 A의 인스턴스를 생성하기 위해 이를 조합할수 있다.

  // CsvEncoder와 IceCream을 예로 들어
  // * IceCream은 String :: Int :: Boolean :: HNil의 제너릭 Repr을 가지고 있다.
  // * Repr은 String, Int, Boolean과 HNil로 구성되어 있다. 이들 타입에 대해서 CsvEncoder가 있다면 전체에 대한 encoder를 만들수 있다.
  // * Repr에 대해서 CsvEncoder를 유도할수 있다면 IceCream에 대해서도 할수가 있다.


  // 3.2.1 Instance for HList
  // String, Int, Boolean 에 대해서 CsvEncoder를 작성하는것 부터 시작하자.

  implicit val stringEncoder: CsvEncoder[String] = createEncoder(s => List(s))

  implicit val intEncoder: CsvEncoder[Int] = createEncoder(i => List(i.toString))

  implicit val booleanEncoder: CsvEncoder[Boolean] = createEncoder(b => List(if(b) "yes" else "no"))

  implicit val doubleEncoder: CsvEncoder[Double] = createEncoder(d => List(d.toString))

  // HList를 위한 encoder를 만들기 위해 이 블록들을 합칠수 있다.
  // 두가지 규칙을 사용할것이다. 하나는 `HNil` 위한 것이고 또다른 하나는 `::` 위한 것이다.
  implicit val hnilEncoder: CsvEncoder[HNil] = createEncoder[HNil](hnil => Nil)

  // head와 tail을 각각 encode하고 합친다.
  // 합치는 규칙만 여기서 정하면 된다.
  implicit def hlistEncoder[H, T <: HList](
    implicit hEncoder: CsvEncoder[H], tEncoder: CsvEncoder[T]
  ): CsvEncoder[H :: T] = createEncoder[H :: T] {
    case h :: t => hEncoder.encode(h) ++ tEncoder.encode(t)
  }

  // 이 5가지의 규칙은 String, Int, Boolean을 포함하고 있는 어떤 HList라도 CsvEncoder를 만들어 낼수 있다.

  val reprEncoder: CsvEncoder[String :: Int :: Boolean :: HNil] = implicitly

  reprEncoder.encode("abc" :: 123 :: true :: HNil)
  // List(abc, 123, yes)


  case class IceCream2(name: String, numCherries: Int, inCorn: Boolean)
  val iceCream2s: List[IceCream2] = List(
    IceCream2("Sundae", 1, false),
    IceCream2("Cornetto", 0, true),
    IceCream2("Banana Split", 0, false)
  )

  // 3.2.2 Instances for concrete products
  // IceCream을 위한 CsvEncoder를 만들기 위해서 HList의 유도 규칙과 Generic 인스턴스를 합칠수 있다.
  // IceCream을 받아서 List[String]을 반환하는 encoder를 만든다.
  implicit val iceCreamEncoder2: CsvEncoder[IceCream2] = {
    val gen = Generic[IceCream2]
    val enc = implicitly[CsvEncoder[gen.Repr]]
    createEncoder[IceCream2] {
      iceCream => enc.encode(gen.to(iceCream))
    }
  }
  writeCsv(iceCream2s)

  // 이방법은 IceCream에만 한정되어 있다.
  // 이상적으로 Generic과 매칭되는 CsvEncoder를 가지고 있는 모든 case class를 하나의 규칙으로 다루는 것을 가지고 싶다.
  // 이 유도는 한단계 한단계 시작해보자.

  // 첫번째로는
  /**
  implicit def genericEncoder[A](
    implicit
      gen: Generic[A],
      enc: CsvEncoder[???]
   ): CsvEncoder[A] =
    createEncoder(a => enc.encode(gen.to(a)))
  **/
  // 첫번째 문제는 ??? 자리에 위치한 곳에 타입을 선택해서 넣는것이다.
  // gen과 관련된 Repr타입을 작성하고 싶지만 이것은 할수 없다.

  /**
  implicit def genericEncoder2[A](
    implicit
      gen: Generic[A],
      enc: CsvEncoder[gen.Repr]
  ): CsvEncoder[A] =
    createEncoder[A](a => enc.encode(gen.to(a)))
    **/
  // <console>:26: error: illegal dependent method type: parameter may only be referenced in a subsequent parameter section
  //          gen: Generic[A],
  //          ^

  // 문제는 범위 이슈이다.
  // 같은 블록에서는 하나의 파라메터의 타입 멤버는 다른 파라메터를 참조할수 없다.
  // 여기서 자세한것을 다루지는 않는다.
  // 이런 종류의 문제를 풀기위한 트릭은
  // * 메소드에 타입 파라메터를 추가하고
  // * 이를 각각의 관련된 타입이 참조를 하는 것이다.
  // 같은 타입에 대한 encoder가 두개 있어도 안된다. 일반적인 경우에 에러는 implicit이 모호하다고 뜨지만 이경우에는 그냥 없다고만 뜬다ㅠ.ㅠ
  // 그래서 디버깅하는게 조금은 힘들다.
  /*implicit*/ def genericEncoder3[A, R](
    implicit
      gen: Generic[A] { type Repr = R },
      enc: CsvEncoder[R]
  ): CsvEncoder[A] =
    createEncoder(a => enc.encode(gen.to(a)))

  // 이 제너릭 스타일은 다음 장에서 보다 자세히 다룰것이다.
  // 이 정의는 컴파일 되고 예상된데로 수행하며 예상되는 모든 case class는 이것을 사용할 수 있는것으로 충분하다고 말할수 있다.

  // 직관적으로 이 정의에 대해 이야기하면 :
  // 주어진 A와 HList R 타입에 대하여 A를 R로 바꾸는 implicit Generic과 R를 위한 CsvEncoder를 통해서 A를 위한 CsvEncoder를 만든다.

  // 컴파일의 호출은 아래와 같이 확장된다.
  writeCsv(iceCream2s)
  // 유도의 규칙들을 사용하여
  writeCsv(iceCream2s)(
    genericEncoder3(
      Generic[IceCream2],
      hlistEncoder(stringEncoder,
        hlistEncoder(intEncoder,
          hlistEncoder(booleanEncoder, hnilEncoder)))))

  // Aux type alias
  // Generic[A] { type Repr = R } 와 같은 type refinement 는 장황하고 가독성이 떨어진다.
  // > Refinement Types = Types + Logical Predicates 이라고 한다.
  // > 스칼라는 타입 멤버로 타입에 대한 제약을 주는 경우에 사용한다.

  // 그래서 shapeless는 type member를 type parameter로 재구성한 type alias인 `Generic.Aux`를 제공한다.
  trait Generic2[T] {
    type Repr
  }
  object Generic2 {
    type Aux[A, R] = Generic2[A] { type Repr = R }
  }

  // 이 alias를 이용하면 훨씬 보다 가독성 좋은 정의를 얻을수 있다.
  implicit def genericEncoder4[A, R](
    implicit
      gen: Generic.Aux[A, R],
      enc: CsvEncoder[R]
  ): CsvEncoder[A] =
    createEncoder[A](a => enc.encode(gen.to(a)))

  // 3.2.3 안좋은 것 무엇인가?
  // 앞에서 보았던것들은 꽤 마법처럼 보인다, allow us to provide one significant dose of reality.
  // 만약 무엇인가 잘못 되었으면 왜 그런지 컴파일러는 제대로 말해주지 못한다.

  // 코드가 컴파일 되지 않는 큰이유가 2가지 있다.
  // 첫번째는 타입에 대한 implicit Generic을 찾지 못하는 경우 이다.


  // not a case class
  class Foo(bar: String, baz: Int)

//  writeCsv(List(new Foo("abc", 123)))     // 컴파일안됨요.

  // <console>:30: error: could not find implicit value for parameter encoder: CsvEncoder[Foo]
  //        writeCsv(List(new Foo("abc", 123)))

  // 이 에러메시지는 상대적으로 이해하기 쉽다.
  // 만약 Generic을 계산해내지 못한다면 그 타입은 ADT가 아닌것이다.
  // algebra안에 어딘가 case class나 sealed abstract 타입이 *아닌것이* 있는것이다.

  // 다른 가능성 있는 실패는 컴파일러가 HList에 대해서 CsvEncoder를 계산해 내지 못하는 것이다.
  // 일반적으로 ADT의 필드중 하나의 encoder가 없는 경우에 일어난다.

  import java.util.Date
  case class Booking(room: String, date: Date)
//  writeCsv(List(Booking("Lecture Hall", new Date)))   // 컴파일 안됨요.

  // <console>:32: error: could not find implicit value for parameter encoder: CsvEncoder[Booking]
  //        writeCsv(List(Booking("Lecture hall", new Date())))

  // 이 에러 메시지는 전혀 도움이 되지 않는다.
  // 컴파일러가 아는것은 많은 implicit 찾기 위한 규칙을 시도했지만 그것을 하지 못했다는 것이 전부이다.
  // 어떤 조합이 바람직한 결과에 가장 밀접한지 알수 없기 때문에, 어디에 실패한 소스가 있는지 말하지 못한다.

  // implicit 찾기에 대한 디버깅은 다음 챕터에서 다룰것이다.
  // 지금의 좋은 소식은 implicit 찾기의 실패는 항상 컴파일 타임에 이루어 진다는 것이다.
  // 코드가 실행도중에 실패를 하는것은 거의 없다.

  // 3.3 Deriving instances for coproducts

  // 지난 섹션에서는 어떤 product 타입에 대해서도 CsvEncoder를 자동으로 유도하는 규칙들을 만들었다.
  // 이번 섹션에는 같은 패턴을 coproducts에 적용시킨다.
  // shape ADT 예제로 돌아가서

  /* TOP level에 선언해줘야 함
  sealed trait Shape
  final case class Rectangle(width: Double, height: Double) extends Shape
  final case class Circle(radius: Double) extends Shape
  */

  // Shape에 대한 제너릭 표현은 Rectangle :+: Cirle :+: CNil 이다
  // 우리는 HList에서 사용했던 것과 같은 원리로 :+: 와 CNil 에 대한 제너릭 CsvEncoder를 만들수 있다.
  // 현재 존재하는 encoder들이 Retangle과 Circle을 처리해줄것이다.

  import shapeless.{Coproduct, :+:, CNil, Inl, Inr}

  implicit val cnilEncoder: CsvEncoder[CNil] =
    createEncoder[CNil](cnil => throw new Exception("Universe exploded! Abort!"))

  implicit def coproductEncoder[H, T <: Coproduct](
    implicit
      hEncoder: CsvEncoder[H],
      tEncoder: CsvEncoder[T]
  ): CsvEncoder[H :+: T] =
    createEncoder[H :+: T] {
      case Inl(h) => hEncoder.encode(h)
      case Inr(t) => tEncoder.encode(t)
    }

  // 두가지 중요한 포인트가 있다.

  // 1. 놀랍게도 CNil에 대한 encoder는 오히려 예외를 발생시킨다.
  // 그래도 당황하지 않기!!, 실제로 CNil의 타입의 어떤 값도 만들수 없다는 것을 상기하자.
  // 단지 컴파일러는 위한 표시일뿐이다.
  // 이부분에 도달할수 없기 때문에 갑자기 실패하는것이 맞는것이다.

  // http://www.mathgoodies.com/lessons/vol9/disjunction.html 두개의 명제는 OR로 연결

  // 2. Coproducts는 타입의 논리합(disjunction)이다.
  // :+: 를 위한 encoder는 왼쪽값을 encode할것인지 오른쪽 값을 할것인지 결정해야 한다.
  // :+: 의 하위타입 왼쯕은 Inl, 오른쪽은 Inr 대해서 패턴 매칭을 할것이다.

  // 이정의와 product type에 대한 정의로 shape의 리스트에 대해서 직렬화 할수 있다.
  val shapes: List[Shape3] = List(
    Rectangle3(3.0, 4.0),
    Circle3(1.0)
  )

//  writeCsv(shapes)    // 컴파일 안됨요.

  // <console>:33: error: could not find implicit value for parameter encoder: CsvEncoder[Shape]
  //        writeCsv(shapes)

  // 이런... 컴파일 안됨! 에러메시지는 앞에서 이야기 했던것 처럼 도움이 되지 않는다.
  // 컴파일 오류가 나는 이유는 Double에 대한 CsvEncoder가 없기 때문이다.


  writeCsv(shapes)

}

object exercise extends App {
  // 3.3.1 Aligning columns in CSV Output

  // 아마 rectangle과 circle를 위한 데이터를 두개의 컬럼 셋으로 구분하는게 더 좋을것이다.
  // CsvEncoder에 width 필드를 넣음으로서 이를 해결하수 있다.

  trait CsvEncoder[A] {
    def width: Int
    def encode(value: A): List[String]
  }

  // 모든 정의를 다 따르면 각각의 필드를 다른 컬럼에 놓게 할수 있다.
  // width, height, radius 요런 느낌으로

  def createEncoder[A](w: Int)(f: A => List[String]): CsvEncoder[A] =
    new CsvEncoder[A] {
      def width: Int = w
      def encode(value: A) = f(value)
    }


  implicit val doubleEncoder: CsvEncoder[Double] = createEncoder[Double](1)(d => List(d.toString))
  implicit val hnilEncoder: CsvEncoder[HNil] = createEncoder[HNil](0)(hnil => Nil)
  implicit def hlistEncoder[H, T <: HList](
    implicit
      hEncoder: CsvEncoder[H],
      tEncoder: CsvEncoder[T]
  ): CsvEncoder[H :: T] = createEncoder[H :: T](hEncoder.width + tEncoder.width) {
    case h :: t => hEncoder.encode(h) ++ tEncoder.encode(t)
  }

  implicit val cnilEncoder: CsvEncoder[CNil] = createEncoder[CNil](0)(cnil => throw new Exception("Never happen!"))
  implicit def cporudctEncoder[H, T <: Coproduct](
    implicit
      hEncoder: CsvEncoder[H],
      tEncoder: CsvEncoder[T]
  ): CsvEncoder[H :+: T] = createEncoder[H :+: T](hEncoder.width + tEncoder.width) {
    case Inl(h) => hEncoder.encode(h) ++ List.fill(tEncoder.width)("")
    case Inr(t) => List.fill(hEncoder.width)("") ++ tEncoder.encode(t)
  }

  implicit def genericEncoder[A, R](
    implicit
      gen: Generic.Aux[A, R],
      enc: CsvEncoder[R]
   ): CsvEncoder[A] = createEncoder(enc.width) {
    a => enc.encode(gen.to(a))
  }

  def writeCsv[A](values: List[A])(implicit encoder: CsvEncoder[A]): String =
    values.map(value => encoder.encode(value).mkString(",")).mkString("\n")

  val shapes: List[Shape3] = List(
    Rectangle3(3.0, 4.0),
    Circle3(1.0)
  )
  println(writeCsv(shapes))
}

object recursive {
  // 3.4 Deriving instances for recursive types
  // 보다 모호한 경우에 대해서 시도를 해보자 - 이진 트리
  sealed trait Tree[A]
  final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]
  final case class Leaf[A](value: A) extends Tree[A]

  // 이론적으로는 위의 정의를 위한 CsvWriter를 도출해 해내기 위한것들을 이미 다 정의하였다.

//  implicitly[CsvEncoder[Tree[Int]]]   // 컴파일 안됨요.

  // <console>:24: error: could not find implicit value for parameter e: CsvEncoder[Tree[Int]]
  //        implicitly[CsvEncoder[Tree[Int]]]

  // 문제는 컴파일러가 implicit을 위해서 무한히 찾는것을 막고 있다는데 있다.
  // 만약 어떤 브랜치 검색에서 같은 type 생성자는 두번 본다면 implicit를 찾는것이 끝나지 않는것으로 보고 중지한다.
  // Branch가 재귀적이면 CsvEncoder[Branch] 규칙을 위해 이런 행위가 발생한다.

  // 사실 상황은 이것보다 훨씬더 나쁘다.
  // 만약 컴파일러가 같은 타입 생성자 두번를 만나고 타입 파라메터의 복잡도가 증가한다면 이 또한 중지할것이다.
  // ::[H, T]과 :+:[H, T]이 다른 제너릭 표현들에서 나타나고 이는 컴파일러가 조기에 포기하게 하게 해서 shapeless에서 문제가 된다.


  // 3.4.1 Lazy
  // 다행히 shapeless는 차선책으로 Lazy라 불리는 타입을 제공한다.
  // Lazy는 두가지를 한다.
  // 1. 이는 implicit 찾기를 엄격히 필요할때 까지 늦춘고 자기참조 implicit 유도를 하게 한다.
  // 2. 앞에서 언급한 지나친 경험적(heuristic) 방어로 부터 보호한다.

  // 경험의 법칙(rule of thumbs)으로
  // 어떤 HList나 Coproduct규칙의 "head" 파라메터와
  // 어떤 제너릭 규칙의 "repr" 파라메터는 Lazy로 감싸는것이 항상 좋다.
  implicit def hlistEncoder[H, T <: HList](
    implicit
      hEncoder: Lazy[CsvEncoder[H]],    // lazy로 감쌈
      tEncoder: CsvEncoder[T]
  ): CsvEncoder[H :: T] = new CsvEncoder[H :: T] {
    def encode(value: H :: T): List[String] = value match {
      case h :: t =>
        hEncoder.value.encode(h) ++ tEncoder.encode(t)
    }
  }

  implicit def cproductEncoder[H, T <: Coproduct](
    implicit
      hEncoder: Lazy[CsvEncoder[H]],  // lazy로 감쌈
      tEncoder: CsvEncoder[T]
  ): CsvEncoder[H :+: T] = new CsvEncoder[H :+: T] {
    def encode(value: H :+: T): List[String] = value match {
      case Inl(h) => hEncoder.value.encode(h)
      case Inr(t) => tEncoder.encode(t)
    }
  }

  implicit def genericEncoder[A, R](
    implicit
      gen: Generic.Aux[A, R],
      enc: Lazy[CsvEncoder[R]]   // lazy로 감쌈
  ): CsvEncoder[A] = new CsvEncoder[A] {
    def encode(value: A): List[String] =
      enc.value.encode(gen.to(value))
  }

  // 이것은 컴파일러가 일찍 중지하는것을 막아주고 Tree와 같은 복잡하고 재귀적인 타입에 대해서 작동하는것이 가능하다.
  implicitly[CsvEncoder[Tree[Int]]]

  // 3.5 요약
  // 이번장에서는 case class 인스턴스를 자동으로 유도하기 위해 Generic, HList와 Coproducts를 어떻게 사용하는지에 대해서 알아보았다.
  // 또한 복잡하거나 재귀적인 타입을 다루는 수단으로서 Lazy 타입을 다루었다.

  // 이것들을 하나로 모아, 우리는 type class 인스턴스를 유도하기 위한 공통된 부분을 작성할수 있다.

  import shapeless.{HList, ::, HNil, Coproduct, :+:, CNil, Generic, Lazy}

  // 1번째 : type class 정의
  trait MyTC[A] {
    // etc...
  }

  // 2번째 : 기본 인스턴스 정의
  implicit def intInstance: MyTC[Int] = ???

  // 3번째 : HList와 Coproduct를 위한 인스턴스 정의
  implicit def hnilInstance: MyTC[HNil] = ???

  implicit def hlistInsstance[H, T <: HList](
    implicit
      hInstance: Lazy[MyTC[H]],  // lazy로 감쌈
      tInstance: MyTC[T]
  ): MyTC[H :: T] = ???

  implicit def cnilInstance: MyTC[CNil] = ???

  implicit def coproductInstance[H, T <: Coproduct](
    implicit
      hInstance: Lazy[MyTC[H]],  // lazy로 감쌈
      tInstance: MyTC[T]
  ): MyTC[H :+: T] = ???

  implicit def genericInstance[A, R](
    implicit
    gen: Generic.Aux[A, R],
    enc: Lazy[MyTC[R]]    // lazy로 감쌈
  ): MyTC[A] = ???

  // 다음장에서는 이런 스타일로 코드를 작성하기 위한 몇가지 유용한 이론, 프로그래밍 패턴 그리고 디버깅 기술들에 대해서 다룰것이다.
  // 그다음장에서는 ADT에서 필드와 타입이름을 조사하는 것을 가능하게 해주는 다양한 Generic을 이용한 type class 유도에 대해서 다시 알아볼것이다.

}

object typeAndImplicits extends App {

  // 4. Working with types and implicits

  // 지난장에서는 shapeless의 가장 매력적인 사용방법중에 하나를 보았다. - 자동으로 type class 인스턴스를 유도하는것.
  // 더 많은 강력한 예가 이후에 있다.
  // 그러나 진행하기 전에 타입과 implicit-heavy 패턴 코드를 작성과 디버깅에 대하여 보고 작성한것에 대한
  // 이론에 대해서 이야기 하는 시간을 가져 보자.

  // 4.1 Theory: dependent types
  // 지난장에 ADT를 제너릭 표현으로 맵핑하는 type class인 Generic을 사용한는 것에 많은 시간을 보냈다.
  // 그러나 Generic을 포함한 많은 shapeless를 떠 받치는 이론에 대해서 이야기 하지 않았다 - dependent types

  // 이를 잘 설명하기 위해 Generic에 대해 보다 자세히 들여다 보자.
  // 정의에 대한 간단화한 버전이 있다.

  trait SimpleGeneric[A] {
    type Repr

    def to(value: A): Repr

    def from(value: Repr): A
  }

  // Generic의 인스턴스는 두개의 다른 타입을 참조한다.
  // type parameter A와 type member Repr이다.
  // 아래처럼 getRepr을 구현한다고 해보자. 무슨 타입을 반환받게 될까?

  def getRepr[A](value: A)(implicit gen: Generic[A]) =
    gen.to(value)

  // 답은 우리가 얻게되는 gen의 instance에 달려있다.

  // 호출을 getRepr로 확장할때 컴파일러는 Generic[A]를 위해서 찾고 결과 타입은 그 인스턴스에 정의된 Repr이다.

  case class Vec(x: Int, y: Int)
  case class Rec(origin: Vec, size: Vec)
  getRepr(Vec(1, 2))
  // res1: shapeless.::[Int,shapeless.::[Int,shapeless.HNil]] = 1 :: 2 :: HNil

  getRepr(Rec(Vec(0, 0), Vec(5, 5)))
  // res2: shapeless.::[Vec,shapeless.::[Vec,shapeless.HNil]] = Vec(0,0) :: Vec(5,5) :: HNil

  // 여기에 보여진것이 dependent type이다.
  // getRepr의 반환 타입은 파라메터의 타입 멤버에 의존한다.

  // Generic의 Repr를 타입 멤버 대신 타입 파라메터로 한정해보자.
  trait Generic2[A, Repr]

  def getRepr2[A, R](value: A)(implicit gen: Generic2[A, R]): R = ???

  // 우리는 원하는 Repr을 getRepr에 타입 파라메터로 전달하게 된다.
  // 사실상 getRepr을 쓸모없게 만든다.

  // 직관적으로 type parameter는 입력으로서 유용하고 type member는 결과로서 유용하다는것을 끄집어 낼수 있다.

  // 4.2 Dependently typed function
  // shapeless는 모든곳(Generic, Witness 그리고 HList가 동작하는 그외의 implicit값이 사용되는 곳)에 dependent type을 사용한다.

  // 예를 들어 shapeless는 HList의 마지막 요소를 반환하는 Last라 불리는 type class를 제공한다.
  import shapeless.ops.hlist.Last
  val last = implicitly[Last[String :: Int :: HNil]]
  // last: shapeless.ops.hlist.Last[shapeless.::[String,shapeless.::[Int, shapeless.HNil]]] = shapeless.ops.hlist$Last$$anon$34@14b67e4b
  println(last("a" :: 10 :: HNil))    // 10

  // 각각의 경우 주목할 점은 Out type은 우리가 시작한 HList의 type에 의존한다.
  // 또한 인스턴스는 입력 HList가 적어도 하나의 요소가 있을때만 생성된다는 점에 주목하자.

//  implicitly[Last[HNil]]    // 컴파일 안됨요.
    // <console>:15: error: Implicit not found: shapeless.Ops.Last[shapeless.HNil]. shapeless.HNil is empty, so there is no last element.
    //        implicitly[Last[HNil]]


  // 추가 예제로 HList에서 두번째 요소를 반환하는 `Second`이란 이름의 type class를 직접 구현하여 보자.
  trait Second[H <: HList] {
    type Out
    def apply(value: H): Out
  }

  implicit def hlistSecond[A, B, Rest <: HList]: Second[A :: B :: Rest] =
    new Second[A :: B :: Rest]{
      type Out = B
      def apply(value: A :: B :: Rest): Out = value.tail.head
    }

  val second = implicitly[Second[String :: Boolean :: Int :: HNil]]
  second("Woo!" :: true :: 321 :: HNil)
  // res3: second.Out = true

  // 4.2.1 Chaining dependent functions
  // 우리는 하나의 타입에서 다른 타입을 계산하는 방법으로 dependently typed function를 보았다.
  // - case clase의 Repr을 계산하기 위해 Generic을 사용하였다. 그외 더있음요.

  // 계산이 하나의 단계보다 많을때는 어떠한가?
  // 예를 들어 HList의 *마지막* 아이템을 찾고 싶다 해보자.
  // Generic과 Last를 조합할 필요가 있다.
  // 작성해보자.

  /*  컴파일 안됨요
  def lastField[A](input: A)(
    implicit
      gen: Generic[A],
      last: Last[gen.Repr]
  ): last.Out = last.apply(gen.to(input))
  */

  // <console>:20: error: illegal dependent method type: parameter may only be referenced in a subsequent parameter section
  //          gen: Generic[A],

  // 안타깝게도 이것은 컴파일이 되지 않는다.
  // 이는 지난장에 HList쌍을 위한 CsvWriter를 만들때 동일한 문제를 겪었다.
  // 일반적인 규칙으로 모든 출력 변수 타입을 type parameter로 lifting하고
  // 컴파일러가 적합한 타입으로 합치게 한다.

  def lastField[A, Repr <: HList](input: A)(
    implicit
      gen: Generic.Aux[A, Repr],
      last: Last[Repr]
  ): last.Out = last.apply(gen.to(input))

  lastField(Rec(Vec(1, 2), Vec(3, 4)))
  // res4: Vec = Vec(3,4)

  // 이는 보다 미묘한 제약에서도 잘동작한다.
  // 예를 들어 정확히 하나의 필드만 있는 case class의 내용을 원한다고 해보자.
  // 아래와 같이 시도할수 있다.

  // 여담 : 값이 하나만 있으니 WrappedValue이다.  이는 Value Object 개념을 알면 도움이 된다.
  // http://docs.scala-lang.org/overviews/core/value-classes.html
  def getWrappedValue[A, Head](input: A)(
    implicit
      gen: Generic.Aux[A, Head :: HNil]
  ): Head = gen.to(input).head

  // 결과는 더 않좋다.
  // 함수의 정의는 컴파일 되지만 호출하려고 하는 곳에서는 컴파일을 위한 implicit을 절대 찾을수 없다.
  case class Wrapper(value: Int)

//  getWrappedValue(Wrapper(12))   // 컴파일 안됨요.
  // <console>:21: error: could not find implicit value for parameter gen: shapeless.Generic.Aux[Wrapper,shapeless.::[Head,shapeless.HNil]]
  //        getWrappedValue(Wrapper(42))


  // 문제에 대한 에러메시지의 힌트는 아래와 같다.

  // error: could not find implicit value for parameter gen:
  // Generic.Aux[Wrapper, Head :: HNil]

  // 단서는 type Head의 출현에 있다.
  // 이것은 함수의 타입 파라메터의 이름이다.
  // 이는 컴파일러가 합치려는(unify)하려는 타입에는 나타나서는 안된다.
  // 문제는 gen paramter가 제약이 과했다.(over-constraint)
  // 컴파일러는 Repr을 찾고 그것이 동시에 하나의 필드가 있는 HList 보장하는 능력을 가지고 있지 않다.

  // 다른 유형의 타입 - Nothing을 포함하는 타입을 컴파일러가 공변(covariant)타입으로 통합하는걸 실패할때 종종 나타난다.

  // 해결책은 문제를 몇가지 단계로 나누는것이다.
  // 1. A에 대한 적합한 Repr과 함께 Gerneic을 찾는것이다.
  // 2. Repr을 Head 타입으로 제공한다.

  // `=:=`를 사용한 버전으로 수정해보자.
  // `=:=`는 Generalized type contstraints 라 불리며 옆 링크에 설명이 잘되어 있다. http://stackoverflow.com/questions/3427345/what-do-and-mean-in-scala-2-8-and-where-are-they-documented

  /* 컴파일 안됨요.

  def getWrappedValue2[A, Repr <: HList, Head, Tail <: HList](input: A)(
    implicit
      gen: Generic.Aux[A, Repr],
      ev: (Head :: Tail) =:= Repr
  ): Head = gen.to(input).head

  */

  // <console>:21: error: could not find implicit value for parameter c: shapeless.ops.hlist.IsHCons[gen.Repr]
  //        ): Head = gen.to(input).head

  // 함수의 body에 있는 head 메소드는 implicit parameter로 IsHCons를 필요로 하기때문에 컴파일이 실패한다.
  // 이 에러는 아까보다 훨씬 고치기 쉽다. 단지 shapeless의 toolbox에서 tool을 배우는게 필요할뿐이다.
  // IsHCons는 shapeless에서 HList를 Head와 Tail로 나누는 type class이다.
  // 우리는 `=:=` 대신에 IsHCons를 써야만 한다.

  import shapeless.ops.hlist.IsHCons

  def getWrappedValue3[A, Repr <: HList, Head, Tail <: HList](input : A)(
    implicit
      gen: Generic.Aux[A, Repr],
      isHCons: IsHCons.Aux[Repr, Head, Tail]
  ): Head = gen.to(input).head

  // 드뎌 컴파일 된다.
  // 이번건은 버그를 수정하고 위 함수가 implicit을 예상한데로 찾게 해준다.

  getWrappedValue3(Wrapper(42))

  // 최종적으로 가져가려는 점은 IsHCons가 아니다.
  // shapeless는 이와 같은 툴을 많이 제공하고 있고, 필요한 툴이 없다면 우리는 직접 작성할수 있다.
  // 중요한점은 코드를 컴파일 되게 작성하는 과정과 적합한 해답을 찾아내는 능력이다.
  // 우리가 알아온 것들을 단계별로 정리하면서 이 섹션을 마무리 할것이다.

  // 4.3 Summary

}
