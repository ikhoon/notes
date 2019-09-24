
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

def getUser(userId: Int): Future[Long] = Future {
  Thread.sleep(1000)
  println("get user!!")
  userId.toLong
}

def getOrder(user: Long): Future[String] = Future {
  Thread.sleep(1000)
  println("get order!!")
  user.toString
}

def getOrderItem(order: String): Future[Int] = Future {
  Thread.sleep(1000)
  println("get order item!!")
  order.toInt
}

val userId = 10
// map
val future: Future[Future[Future[Int]]] =
  getUser(userId).map(user =>
    getOrder(user).map(order =>
      getOrderItem(order)))
future.map(x => x.map(y => y.map(z => println("xxxx " + z))))


// flatMap
val futureA: Future[Int] =
  getUser(userId).flatMap(user =>
    getOrder(user).flatMap(order =>
      getOrderItem(order)
    )
  )
println(Await.result(futureA, Duration.Inf))
// for comprehension
val orderItem: Future[Int] = for {
  user <- getUser(userId)
  order <- getOrder(user)
  orderItem <- getOrderItem(order)
} yield orderItem

println(Await.result(orderItem, Duration.Inf))

// Action.async
// Action { ... }

// Future { }

Future.successful(10)
