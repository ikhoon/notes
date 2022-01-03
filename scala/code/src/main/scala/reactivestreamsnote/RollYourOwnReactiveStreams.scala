package reactivestreamsnote

import java.util.concurrent.atomic.{AtomicBoolean, AtomicLong}
import org.reactivestreams.{Publisher, Subscriber, Subscription}
import scala.util.control.NonFatal

class ListPublisher[A](xs: => Stream[A]) extends Publisher[A] {

  private var subscriber: Subscriber[_ >: A] = _

  override def subscribe(subscriber: Subscriber[_ >: A]): Unit = {
    val subscription = new ListSubscription(subscriber)
    subscriber.onSubscribe(subscription)
    subscription.doOnSubscribed()
  }

  private class ListSubscription[B >: A](subscriber: Subscriber[B]) extends Subscription {

    private var error: Option[Throwable] = None
    private val iterator: Iterator[A] =
      try {
        xs.iterator
      } catch {
        case NonFatal(ex) =>
          error = Some(ex)
          Iterator.empty
      }

    private val terminationFlag: AtomicBoolean = new AtomicBoolean(false)
    private val demand: AtomicLong = new AtomicLong(0)

    def doOnSubscribed(): Unit = {
      error.foreach(ex => if (!terminate()) subscriber.onError(ex))
    }

    override def request(n: Long): Unit = {
      if (n <= 0 && !terminate()) {
        subscriber.onError(new IllegalArgumentException(s"non-positive request signals are illegal: $n"))
      } else {
        if (demand.getAndAdd(n) == 0) {
          while (demand.get > 0 && iterator.hasNext && !isTerminated) {
            try {
              subscriber.onNext(iterator.next())
              demand.getAndDecrement()
            } catch {
              case NonFatal(ex) => if (!terminate()) subscriber.onError(ex)
            }
          }
          if (iterator.isEmpty && !terminate()) {
            subscriber.onComplete()
          }
        }
      }
    }

    override def cancel(): Unit = terminate()

    @inline private def isTerminated: Boolean = terminationFlag.get

    private def terminate(): Boolean = terminationFlag.getAndSet(true)

  }
}
