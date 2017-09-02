package exp

/**
  * Created by ikhoon on 24/08/2017.
  */
class Tpyeclass {

  trait Adder[A] {
    def zero: A
    def add(x: A, y: A): A
  }

  def sum[A](xs: List[A])(adder: Adder[A]): A =
    xs.foldLeft(adder.zero)(adder.add)

  val intAdder = new Adder[Int] {
    def zero: Int = 0
    def add(x: Int, y: Int) = x + y
  }

  val doubleAdder = new Adder[Double] {
    def zero: Double = 0.0
    def add(x: Double, y: Double) = x + y
  }

}
