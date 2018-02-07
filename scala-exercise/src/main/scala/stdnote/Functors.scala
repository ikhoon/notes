package stdnote

import cats.Functor

/**
  * Created by Liam.M on 2017. 12. 08..
  */
object Functors {


  def main(args: Array[String]): Unit = {

    implicit val iterableInstance = new Functor[Iterable] {
      override def map[A, B](fa: Iterable[A])(f: A => B): Iterable[B] = fa.map(f)
    }
    val xs: List[Int] = List(1, 2, 3)

    val ys: Iterable[Int] = Functor[Iterable].map(xs)(_ + 1)
    println(ys)


  }
}
