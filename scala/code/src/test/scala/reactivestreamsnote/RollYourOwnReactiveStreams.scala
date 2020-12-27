package reactivestreamsnote

import org.reactivestreams.{Publisher, Subscriber, Subscription}
import org.reactivestreams.tck.{PublisherVerification, TestEnvironment}
import org.scalatestplus.testng.TestNGSuiteLike
import org.scalatest.{FunSuite, Matchers}

class ListPublisherTest(env: TestEnvironment, publisherShutdownTimout: Long)
    extends PublisherVerification[Long](env, publisherShutdownTimout)
    with TestNGSuiteLike {

  def this() {
    this(new TestEnvironment(500), 1000)
  }

  override def createPublisher(elements: Long): Publisher[Long] = {
    println(s"## createPublisher $elements")
    new ListPublisher[Long](Stream.range(0, elements))
  }

  override def createFailedPublisher(): Publisher[Long] = {
    new ListPublisher(throw new Exception("Fail to create publisher"))
  }
}

class RollYourOwnReactiveStreamsTest extends FunSuite with Matchers {

  test("subscription") {
    val publisher = new ListPublisher[Int]((1 to 3).toStream)
    publisher.subscribe(new Subscriber[Int] {
      var subscription: Subscription = _
      override def onSubscribe(s: Subscription): Unit = {
        subscription = s
        println("onSubscribe = " + s)
        subscription.request(1)
      }

      override def onNext(t: Int): Unit = {
        println("onNext: t = " + t)
        subscription.request(1)
      }

      override def onError(t: Throwable): Unit = {
        println("onError t = " + t)
      }

      override def onComplete(): Unit = {
        println("onComplete")
      }
    })
  }

  test("example") {}
}
