package catsnote

import cats.data.OptionT
import org.scalatest.{AsyncFunSuite, FunSuite, Matchers}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.Try
import scala.concurrent.ExecutionContext.Implicits.global
import cats._
import cats.implicits._
import cats.syntax._

import scala.collection.immutable


/**
  * Created by Liam.M on 2017. 08. 07..
  * 07/08/2017
  */

/**
  * 여기있는 공식만 외우면 여러분은 마틴오더스키가 될것입니다.
  *
  */
class MonadTransformerSaveUs extends FunSuite with Matchers {

  case class UserDto(id: Long)
  case class User(id: Long)
  case class Address()
  case class Order(id: Long, userId: Long, itemId: Long)
  case class OrderItem(id: Long, orderId: Long)
  case class ItemOption(id: Long, itemId: Long)
  case class Item(id: Long)
  type Device = String

  def getId: Long = math.random().toLong

  // 다양한 API가 존재한다.
  /** 1 : 1, CPU 연산만 변형하는 연산들 */
  def toUserDto(a: User): UserDto = UserDto(a.id)

  /** 1 : 0..1, 파싱하는 로직같은 경우 */
  def toUserId(userId: String): Option[Long] = Try { userId.toLong }.toOption

  /** 1 : N, 데이터를 N개 변환한다, 뭐가 있을려나? ids 파싱같은 경우 */
  def toIds (ids: String): List[Long] = ids.split(",").map(_.toLong)(collection.breakOut)

  /** 1 : 0..1(with Future) DB에서 select 하는 연산이 있다, 값이 있을수도 없을수도 있다. */
  def findUser(id: Long): Future[Option[User]] = Future.successful(Some(User(id)))
  def findAddress(id: Long): Future[Option[Address]] = Future.successful(Some(Address()))
  def findDevice(userId: Long): Future[Device] = Future.successful("ios")

  /** 1 : 1(with Future) 꼭 있어야 하는 데이터의 경우에는 가능하다. */
  def findOption(itemId: Long): Future[ItemOption] = Future.successful(ItemOption(getId, itemId))
  def findItem(itemId: Long): Future[Item] = Future.successful(Item(itemId))
  def findOrder(userId: Long): Future[Option[Order]] = Future.successful(Some(Order(getId, userId, getId)))

  /** 1 : N(with Future) 외부에서 key를 가지고 여러건의 데이터를 select할 경우가 있다 */
  def findOrders(userId: Long): Future[List[Order]] = Future.successful(List(Order(getId, userId, getId), Order(getId, userId, getId)))
  def findOrderItems(orderId: Long): Future[List[OrderItem]] = Future.successful(List(OrderItem(getId, orderId), OrderItem(getId, orderId)))


  // 다양하다 해도 이게 다일것 같다.
  // 더 생각나는 사람있으면 이야기 해도 좋다

  // 공식 - 다 너에게 맞추겠어 - 함수 반환값의 타입 모양에 내부에서 호출하는 함수의 타입을 맞추어라!!
  // 그렇기 때문에 구현을 할때 내가 무엇을 갖고 싶은지(반환하고 싶은지)를 먼저 정하고 시작해야한다.
  // 이거 하나면 된것 같다.


  /** Future[ItemOption] 갖고 싶다면 Future[Option[A]]로 반환하는 함수가 있으면 안된다. */
  def onlyFuture: Future[ItemOption] =
   for {
     item <- findItem(10)   // Future[Item]
     option <- findOption(item.id) // Future[ItemOption]
   } yield option


  /** Future[Option[Address]]를 갖고 싶다면 OptionT로 감싸라. */
  // 힘들것이다. 사용하기 어렵다. 짜증난다. 이게 뭐다냐. 쪼사뿔까.
  def optionFuture1: Future[Option[Address]] = {
    for {
      optionUser <- findUser(10) // Future[Option[User]]
      optionAddress <- optionUser match {
        case Some(user) => findAddress(user.id)  // Future[Option[Address]]
        case None => Future.successful(None)
      }
    } yield optionAddress
  }

  // QUIZ 이걸 OptionT를 이용해서 구현해보라.





  // QUIZ 이번엔 아래함수를 합성해보자. 아직은 동작하지 않는다
  /*
  def optionFuture2: Future[Option[Device]] = {
    val userId = toUserId("10") // Option[Long]
    val user = findUser(userId)  // Future[Option[User]]
    val userDto = toUserDto(user)  // UserDto
    val device = findOrder(userDto.id)  // Future[Option[Order]]
    device
  }
  */




  /** Future[Option[A]]를 반환한다면 무조건 OptionT를 고려해야한다.
    * 중간에 A => Future[B] 이런게 들어있다면? 무조건 OptionT 이다!
    * 중간에 A => Option[C] 이런게 들어있다면? 무조건 OptionT.fromOption 이다.
    * 중간에 A => D         이런게 들어있다면? 무조건 OptionT.pure 이다!
    * 중간에 A => Future[E] 이런게 들어왔다면? 무조건 OptionT.liftF 이다.
    * 어떤함수를 쓰던지 여기에 맞추어라 무조건!!!!
    */
  def findDeviceByUserId(userId: String): Future[Option[Device]] =
    (for {
      userId <- OptionT.fromOption[Future](toUserId(userId)) // Option[Long] => Future[Option[Long]]
      user <- OptionT(findUser(userId))  // Future[Option[User]]
      userDto <- OptionT.pure[Future, UserDto](toUserDto(user))  // UserDto => Future[Option[UserDto]]
      device <- OptionT.liftF(findDevice(userDto.id)) // Future[Device] => Future[Option[Device]]
    } yield device).value


  // 이것만 알았다면 이제 Future[Option[A]]은 두렵지 않다.

  // 그러면 Future[List[A]]은 어떻게 해야하나?
  // 마티니라면 이렇게 했겠지?
  def findAllOrderItemsByUserId(userId: Long): Future[List[OrderItem]] = {
    val orders: Future[List[Order]] = findOrders(userId)
    // 아놔 내가 뭘 잘못했다고 이런 시련을 주시나이까! 살려주세요.
    val value1: Future[List[Future[List[OrderItem]]]] = orders.map(_.map(order => findOrderItems(order.id)))
    // Future와 Future를 합성할때는 절때 map을 쓰면 아니아니된다.
    // 우선 안에 있는 Future를 밖으로 끄내자
    val value2: Future[Future[List[List[OrderItem]]]] = value1.map(Future.sequence(_))
    // 오호라 이번엔 flatten이 있으니 압축해보자.
    val value3: Future[List[List[OrderItem]]] = value2.flatten
    // 오예! 이번엔 안에 있는 List를 합치자.
    val value4: Future[List[OrderItem]] = value3.map(_.flatten)
    // 앗싸 이제 반환하자.
    value4


    // value1: Future[List[Future[List[OrderItem]]]] 이건 왜 생길까요?
    // Future[List[A]]를 본다면?
    // FutureList[FutureList[OrderItem]]이 된다. 이걸 그냥 flatten하면 모든게 행복해 진다.
  }

  // 여기도 위의 OptionT와 같은 원칙이 성립한다.
  // 이번엔 ListT를 사용하면 된다.
  def findAllOrderItemsByUserId2(userId: Long): Future[List[OrderItem]] = {
    import scalaz._
    import Scalaz._
    (for {
      order <- ListT.listT(findOrders(userId)) // Future[List[Order]]
      item <- ListT.listT(findOrderItems(order.id)) // Future[List[OrderItems]]
    } yield item).run
  }

  def findAllOrderItemsByUserId3(userId: Long): Future[List[Item]] = {
    import scalaz._
    import Scalaz._
    (for {
      order <- ListT.listT(findOrders(userId)) // Future[List[Order]]
      item <- findItem(order.itemId).liftM[ListT] // Future[Item]] => Future[List[Item]]
    } yield item).run
  }



  // 친구가 구매한 상품을 이용해서 유저의 대한 추천을 하고 싶다고 하자.
  // 그러면 List[UserId]를 가지고 그들의 주문내역을 가지고 오고 싶다면?
  // 앞에 있는 findAllOrderItemsByUserId를 활용해보자.
  def findAllOrderItemsByUserId4(friends: List[Long]): Future[List[OrderItem]] = {
    // 일단 우리는 먼저 `friends.map` 부터 생각한다.
    val value1: List[Future[List[OrderItem]]] = friends.map(findAllOrderItemsByUserId)
    val value2: Future[List[List[OrderItem]]] = Future.sequence(value1)
    val value3: Future[List[OrderItem]] = value2.map(_.flatten)
    value3
  }


  // 위에것이 틀린것은 아니다
  // 하지만 저렇게 할필요가 없다.
  // 우리는 map을하고 sequence를 하였다.
  // 이건 traverse로 하면 된다.
  // map + sequence = traverse
  def findAllOrderItemdByUserId5(friends: List[Long]): Future[List[OrderItem]] = {
    val value1: Future[List[List[OrderItem]]] = friends.traverse(findAllOrderItemsByUserId)
    val value2: Future[List[OrderItem]] = value1.map(_.flatten) // 헐 flatten이라니!
    value2
  }

  // map + sequence + flatten = flatTraverse
  def findAllOrderItemdByUserId6(friends: List[Long]): Future[List[OrderItem]] = {
    val value1: Future[List[OrderItem]] = friends.flatTraverse(findAllOrderItemsByUserId)
    value1
  }




}
