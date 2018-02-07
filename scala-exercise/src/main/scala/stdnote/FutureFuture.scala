package stdnote

import scala.collection.immutable
import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.util.{Failure, Success, Try}
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
      nettyHttpGet(onComplete: () => {
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





  }







}
