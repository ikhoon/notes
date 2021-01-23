package stdnote

import java.util.concurrent.{CompletableFuture, CompletionStage}
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.jdk.javaapi

//import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by Liam.M on 2018. 01. 09..
  */
object FutureFuture {

  def main(args: Array[String]): Unit = {
    // error handling
    // 합성, for comprehension
    // 다양한 Future에 대한 조합
    /*
    def newFuture(implicit ex: ExecutionContext) = {
      Future { 10 }(ex)
    }

    def foo(): Future[Int] = {
      val promise = Promise[Int]()
      nettyHttpGet(() => {
        promise.success(10)
      })

    }

    def foo(): Future[Int] = {
      val future = Future ?
      nettyHttpGet(onComplete: () => {
        future = Future { 10 }
      })

    }

     */

    import scala.jdk.FutureConverters._

    val completableFuture: CompletableFuture[Void] = new CompletableFuture[Void]
    val future: Future[Unit] = completableFuture.asScala
    println(future)

    val completableFuture2: CompletableFuture[Int] = new CompletableFuture[Int]
    val future2: Future[Int] = completableFuture2.asScala
    println(future)
    completableFuture.complete(null)
    completableFuture2.complete(10)
    val value = Await.result(future, Duration.Inf)
    println(value)
    val value2 = Await.result(future2, Duration.Inf)
    println(value2)
  }

  implicit class CompletionStageOps2(private val cs: CompletionStage[Void]) extends AnyVal {
    def asScala: Future[Unit] = javaapi.FutureConverters.asScala(cs).asInstanceOf[Future[Unit]]
  }

  implicit class NestedCompletionStageOps[A <: CompletionStage[_]](private val cs: CompletionStage[A]) extends AnyVal {
    def asScala: Future[Unit] = javaapi.FutureConverters.asScala(cs).asInstanceOf[Future[Unit]]
  }

}
