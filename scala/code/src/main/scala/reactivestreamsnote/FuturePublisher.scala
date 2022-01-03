package reactivestreamsnote

import java.util.concurrent.atomic.AtomicBoolean
import org.reactivestreams.{Publisher, Subscriber, Subscription}
import scala.concurrent.Future

class FuturePublisher[A](future: Future[A]) extends Publisher[A] {
  override def subscribe(s: Subscriber[_ >: A]): Unit = {
    val subscription = new FutureSubscription(s)
    s.onSubscribe(subscription)
  }

  class FutureSubscription[B >: A](subscriber: Subscriber[B]) extends Subscription {
    private val finished = new AtomicBoolean

    override def request(n: Long): Unit = ???

    override def cancel(): Unit = ???
  }

}
