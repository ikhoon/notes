package twitternote

import org.scalatest.{Matchers, WordSpec}
import com.twitter.util.{Await, Future}
import com.twitter.concurrent.AsyncStream
/**
  * Created by ikhoon on 16/07/2017.
  * 참조 : https://finagle.github.io/blog/2016/02/15/asyncstream/
  */
class AsyncStreamSpec extends WordSpec with Matchers {

  "twitter async stream" should {
    "from future to stream" in {
      val futureList: Future[Seq[Int]] = Future.value(Seq(1, 2, 3, 4))
      val value1: Future[AsyncStream[Int]] = futureList.map((a: Seq[Int]) => AsyncStream.fromSeq(a))
      val flatten: AsyncStream[Int] = AsyncStream.fromFuture(value1).flatten
      Await.result(flatten.toSeq()) shouldBe Seq(1, 2, 3, 4)
    }

    def toAsyncStream[A](f: Future[Seq[A]]): AsyncStream[A] =
      AsyncStream.fromFuture(f.map(AsyncStream.fromSeq)).flatten

    // syntax
    implicit class AsyncStreamOps[A](f: Future[Seq[A]]) {
      def toAsyncStream: AsyncStream[A] =
        AsyncStream.fromFuture(f.map(AsyncStream.fromSeq)).flatten

    }

    "from future to stream with" in {
      val futureList: Future[Seq[Int]] = Future.value(Seq(1, 2, 3, 4))
      val value1 = toAsyncStream(futureList).take(2).toSeq()
      Await.result(value1) shouldBe Seq(1, 2)
    }

    val PAGE_SIZE = 10
    def getList(page: Int): Future[Seq[Int]] = {
      println(s"##### fetch page $page.")
      Future.value((page * PAGE_SIZE) until ((page + 1) * PAGE_SIZE))
    }

    def getStream(initialPage: Int): AsyncStream[Int] = {
      getList(initialPage).toAsyncStream ++ getStream(initialPage + 1)
    }

    "no paging yes stream" in {
      val ten = getStream(0).take(10).toSeq()
      Await.result(ten) shouldBe (0 until 10)
      val hundrend = getStream(0).take(100).toSeq()
      Await.result(hundrend) shouldBe (0 until 100)
    }

    "stream foreach" in {
      getStream(0)
        .take(40)
        .foreach(println)
    }

    "no paging yes stream with filter" in {
      val evenHundrend = getStream(0)
       // .filter(_ % 2 == 0)
        .take(100).toSeq()
      Await.result(evenHundrend) shouldBe Range(0, 100, 1)
    }


  }

}
