package stdnote

/**
  * Created by Liam.M on 2018. 09. 03..
  */


/**
  * 결합, 교환
  * 어디에 사용되는가?
  * minus, 빼기
  * 10 - 20
  * 20 - 10
  * 될수가 없다.
  * foldLeft, foldRight
  * reduceLeft, reduceRight
  * @tparam A
  */
trait Monoid[A] {
  def empty: A
  def combind(x: A, y: A): A
}

object MonoidExample {
  val xs = List(1, 2, 3)

  implicit val plusIntMonoid = new Monoid[Int] {
    def empty: Int = 0
    def combind(x: Int, y: Int): Int = x + y
  }

  implicit val plusStrMonoid = new Monoid[String] {
    def empty: String = ""
    def combind(x: String, y: String): String = x + y
  }

  def sum[A](xs: List[A])(implicit monoid: Monoid[A]): A = {
    xs.foldLeft(monoid.empty) {
      case (acc, elem) => monoid.combind(acc, elem)
    }
  }
  sum(List(1, 2, 3))
  sum(List("1", "2", "3"))
}