package scalaznote

import scalaz.concurrent.Future


/**
  * Created by ikhoon on 25/03/2018.
  */
object PlusExample extends App {


  import scalaz._, Scalaz._

  val result = 1.some <+> none[Int]
  println(result)

  val res2 = OptionT(1.some.pure[Future]) <+> OptionT(none[Int].pure[Future])
  println(res2.run.unsafePerformSync)

}
