package monoclenote

import scala.concurrent.Future

/**
  * Created by ikhoon on 13/02/2018.
  */
object Test {}
object LensExample extends App  {

  // Lens는 product type 즉 case class나 tuple, HList, Map 같은걸 들여다 볼수 있는 타입이다.
  case class Address1(streetNumber: Int, streetName: String)

  // lens streetName







  //  get: Address1 => Int
  //  set: Int => Address1 => Address1

  // Address1에서 streetNumber는 뽑아오는 방법과
  // streetNumber를 받았을때 그것을 어떻게 반영(modify)할것지에 대한것을 Lens생성시에 선언하고 있다.
  import monocle.Lens
//  val streetNumber = Lens[Address1, Int](_.streetNumber)(abc => addr => addr.copy(streetNumber = abc))

  // 역시나 메크로를 통해서 간단히 수행할수 있다.
  import monocle.macros.GenLens
  val streetNumberGen = GenLens[Address1](_.streetNumber)
  val streetNumber = streetNumberGen

  val address = Address1(10, "High Street")

  streetNumber.get(address)

  streetNumber.set(5)(address)

  // 그리고 get과 set을 동시에 수행할수 있는 modify를 호출할수 있다.
  streetNumber.modify(_ + 1)(address)

  def neighbors(n: Int): List[Int] =
    if(n > 0) List(n - 1, n + 1) else List(n + 1)

  // modifyF를 활용하면 Functor를 통한 update가 가능한다.
  import cats.implicits._
  val addresses: List[Address1] = streetNumber.modifyF(neighbors)(address)
  println(addresses)



  import scala.concurrent.ExecutionContext.Implicits.global
  // Future랑도 엮을수 있다.
  def updateNumber(n: Int): Future[Int] = Future.successful(n + 1)
  val eventualAddress: Future[Address1] = streetNumber.modifyF(updateNumber)(address)

  // 무엇보다 중요한건 Lens는 서로 답학 역을수 있다는 것이다.
  case class Person1(name: String, age: Int, address: Address1)
  val john = Person1("John", 20, address)

  val addressGen = GenLens[Person1](_.address)

  // 두개의 lens를 엮어 가져오고
  (addressGen composeLens streetNumber).get(john)

  // 두개의 lens를 엮어서 수정할수 있다.
  (addressGen composeLens streetNumber).set(2)(john)

  // Lens 만들기
  // 역시나 메크로
  import monocle.macros.GenLens
  val age = GenLens[Person1](_.age)

  // deep한 데이터 자료 구저를 수정할때도 가능하다.
  GenLens[Person1](_.address.streetName).set("Iffley Road")(john)


  // 그리고 어노테이션
  // case class의 모든 필드에 대해서 lense를 생성한다.
  import monocle.macros.Lenses
  @Lenses case class Point(x: Int, y: Int)
  val p = Point(5, 3)
  Point.x.get(p)
  Point.y.set(0)(p)



  // 그리고 만들어지는 lense에 대해서 prefix를 사용할수 있다.
  @Lenses("_") case class Point2(x: Int, y: Int)
  val p2 = Point2(5, 3)
  Point2._x.get(p2)




}
