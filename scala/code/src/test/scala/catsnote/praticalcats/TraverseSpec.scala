package catsnote.praticalcats

import java.time.{Instant, LocalDateTime}

import cats.effect.{ContextShift, Effect, IO}
import monix.eval.Task
import org.scalatest.{FunSuite, Matchers}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

/**
  * Created by ikhoon on 08/04/2018.
  */
class TraverseExample extends FunSuite with Matchers {

  def withTS(block: => Unit) = {
    println(s"# Start  - ${LocalDateTime.now()}")
    block
    println(s"# Finish - ${LocalDateTime.now()}")
  }
  import cats.implicits._

  case class Item(id: Int, name: String)

  import java.util.concurrent.Executors

  implicit val ec = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(20))
  val itemIds: List[Int] = (1 to 100).toList
//  val items: Future[List[Item]] = itemIds.traverse(findItemById)

  def findItemById(itemId: Int): Future[Item] = Future {
    Thread.sleep(1000)
    println(Instant.now)
    Item(itemId, "$")
  }

  def findItemByIdEffect[F[_]](itemId: Int)(implicit F: Effect[F]): F[Item] = F.delay {
    Thread.sleep(1000)
    println("Find Item by ID 2 : " + Instant.now)
    Item(itemId, "$")
  }

  def findItemByIdTask(itemId: Int): Task[Item] = Task {
    Thread.sleep(1000)
    println("Find Item by ID 2 : " + Instant.now)
    Item(itemId, "$")
  }
//  import cats.implicits._
//  withTS {
//    val items =
//      itemIds
//        .grouped(10)
//        .toList
//        .foldLeft(List.empty[Item].pure[Future]) { (acc, group) =>
//          acc.flatMap(xs => group.traverse(findItemById).map(xs ::: _))
//        }
//
//    val xs = Await.result(items, Duration.Inf)
//    println(xs)
//  }

  test("io + traverse") {
    withTS {
      val res = itemIds.traverse(findItemByIdEffect[IO])
      val xs = res.unsafeRunSync()
      println(xs)
    }
  }

  import cats.effect.implicits._
  implicit val ct: ContextShift[IO] = IO.contextShift(scala.concurrent.ExecutionContext.global)
  test("io + parTraverse") {
    withTS {
      val res = itemIds.parTraverse(findItemByIdEffect[IO](_))
      val xs = res.unsafeRunSync()
      println(xs)
    }
  }

  test("io + shift + parTraverse") {
    withTS {
      val res = itemIds.parTraverse(IO.shift *> findItemByIdEffect[IO](_))
      val xs = res.unsafeRunSync()
      println(xs)
    }
  }

  test("io + shift + traverse") {
    withTS {
      val res = itemIds.traverse(IO.shift *> findItemByIdEffect[IO](_))
      val xs = res.unsafeRunSync()
      println(xs)
    }
  }
  test("task + parTraverse") {
    import monix.eval.instances._
    import monix.execution.Scheduler.Implicits.global
    withTS {
      val res = itemIds.parTraverse(findItemByIdEffect[Task])
      val xs = res.runToFuture
      println(Await.result(xs, Duration.Inf))
    }
  }

  test("task + parTraverse 2") {
    import monix.eval.instances._
    import monix.execution.Scheduler.Implicits.global
    withTS {
      val res = itemIds.parTraverse(findItemByIdTask)
      val xs = res.runToFuture
      println(Await.result(xs, Duration.Inf))
    }
  }

  test("task + traverse") {
    import monix.execution.Scheduler.Implicits.global
    withTS {
      val res = itemIds.traverse(findItemByIdEffect[Task])
      val xs = res.runToFuture
      println(Await.result(xs, Duration.Inf))
    }
  }

  test("task + traverse 2") {
    import monix.execution.Scheduler.Implicits.global
    withTS {
      val res = itemIds.traverse(findItemByIdTask)
      val xs = res.runToFuture
      println(Await.result(xs, Duration.Inf))
    }
  }

//  {
//    val start = System.nanoTime()
//    val res = itemIds.traverse(findItemById)
//    Await.result(res, Duration.Inf)
//    val interval = (System.nanoTime() - start) / 1000000.0
//    println(s"interval: $interval ms")
//  }

//  {
//    val start = System.nanoTime()
//    val eventualList = itemIds.grouped(10).toList.traverse[Future, List[Option[Item]]](group => {
//      println(group)
//      group.traverse(findItemById)
//    })
//    val xs = Await.result(eventualList, Duration.Inf)
//    val interval = (System.nanoTime() - start) / 1000000.0
//    println(s"2 interval: $interval ms")
//    println(xs)
//  }
}
