package stdnote

import scala.collection.generic.CanBuildFrom

/**
  * Created by Liam.M on 2017. 12. 08..
  */
object GenericCollection {

  // map
  // xs:  Seq[A]
  // f: A => B
  // Seq[B]

  /**

  val x: List = map(List(1, 2, 3), x => x + 1)
  val y : Seq = map(Seq(1, 2, 3), x => x + 1)
  val z : Vector = map(Vector(1, 2, 3), x => x + 1)

  */


//  def map1[A, B](xs: Seq[A], f: A => B): Seq[B] = xs.map(f)


  //  val xs: Seq[Int] = map1(List(1, 2, 3), (x: Int) => x + 1)
  //  val ys: List[Int] = map1(List(1, 2, 3), (x: Int) => x + 1)

  // mapF : , Future[CC[A]] => Future[CC[B]]

  // mapF : , Future[List[A]] => Future[List[A]]
  // mapF(Future(List(1, 2, 3)), (x: Int) => x.toString)
  // Future(List("1", "2", "3"))





}
