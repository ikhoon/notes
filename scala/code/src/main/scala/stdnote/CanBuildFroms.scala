package stdnote

import scala.collection.BuildFrom

/**
  * Created by ikhoon on 03/12/2017.
  */
object CanBuildFroms {

  def main(args: Array[String]): Unit = {
    val xs: List[Int] = map(List(1, 2, 3)) { x =>
      x + 10
    }
    println(xs)
    val ys: Seq[Int] = map(Seq(1, 2, 3)) {
      _ + 10
    }
    println(ys)
  }

  /**
    * generic한 collection을 반환하고 싶다.
    * 입력값이 List이면 반환값도 List이고 싶다.
    * http://www.michaelpollmeier.com/create-generic-scala-collections-with-canbuildfrom
    * 여기에 나와있는 내용을 구현해봄
    * scala 2.13 버전에서는 BuildFrom을 사용함
    * https://docs.scala-lang.org/overviews/core/custom-collection-operations.html#transforming-any-collection
    */
  def map[A, B, CC[X] <: Iterable[X]](collection: CC[A])(f: A => B)(implicit cbf: BuildFrom[CC[A], B, CC[B]]): CC[B] = {
    val builder = cbf.newBuilder(collection)
    builder.sizeHint(collection.size)
    collection.foreach { x =>
      builder += f(x)
      ()
    }
    builder.result()
  }
}
