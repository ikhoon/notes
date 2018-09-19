package stdnote

import scala.collection.generic.CanBuildFrom
import scala.collection.immutable

/**
  * Created by ikhoon on 03/12/2017.
  */
object CanBuildFroms {

  /**
    * generic한 collection을 반환하고 싶다.
    * 입력값이 List이면 반환값도 List이고 싶다.
    * http://www.michaelpollmeier.com/create-generic-scala-collections-with-canbuildfrom
    * 여기에 나와있는 내용을 구현해봄
    */

  def map[A, B, CC[X] <: Iterable[X]](collection: CC[A])(f: A => B)(
    implicit cbf: CanBuildFrom[CC[B], B, CC[B]]): CC[B] = {

    val builder = cbf()
    builder.sizeHint(collection.size)
    collection.foreach { x =>
      builder += f(x)
      ()
    }
    builder.result()
  }

  def main(args: Array[String]): Unit = {
    val xs: List[Int] = map(List(1, 2, 3)) { _ + 10 }
    println(xs)
    val ys: Seq[Int] = map(Seq(1, 2, 3)) { _ + 10 }
    println(ys)
  }

}
