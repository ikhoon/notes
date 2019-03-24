package patternnote

import com.twitter.util.Future


trait Component[A, B] {
  def operation(a: A): B
}

abstract class Decorator[A, B](
                                component: Component[A, B]
                              ) extends Component[A, B] {
  override def operation(a: A): B = component.operation(a)
}


class DoubleStringComponent extends Component[Int, String] {
  override def operation(a: Int): String = {
    println(s"concreate component $a")
    (a * 2).toString
  }
}

class LoggingDecorator[A, B](
                              component: Component[A, B]
                            ) extends Decorator[A, B](component) {

  override def operation(a: A): B = {
    println(s"before operation ${a}")
    val result = super.operation(a)
    println(s"after operation ${result}")
    result
  }
}

class TimerDecorator[A, B](
                            component: Component[A, B]
                          ) extends Decorator[A, B](component) {
  override def operation(a: A): B = {
    val now = System.nanoTime()
    val result = super.operation(a)
    val diff = System.nanoTime() - now
    println(s"elapsed = ${diff}")
    result
  }
}

object DecoratorMain {
  def main(args: Array[String]): Unit = {
    val decorator =
      new TimerDecorator(
        new LoggingDecorator(
          new DoubleStringComponent
        )
      )
    val str = decorator.operation(10)
    println(s"str = ${str}")
  }
}

