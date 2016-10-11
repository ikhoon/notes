package shapenote

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
    List(gen(0), gen(1).toString, gen(2).toString)



}
