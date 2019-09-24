package algonote

import scala.math.pow

/**
  * Created by ikhoon on 30/06/2018.
  */
object Algo_1_1_KaratsubaMultiplication {

  def main(args: Array[String]): Unit = {

  }

  def karatsuba(x: BigInt, y: BigInt): BigInt = {
    if(x < 100 || y < 100) x * y
    else {
      val Karatsuba(m, a, b, c, d) = divide(x, y)
      val res = BigInt(10).pow(m * 2) * karatsuba(a, c) +
        BigInt(10).pow(m) * (karatsuba(a, d) + karatsuba(b, c)) +
        karatsuba(b, d)
      res
    }
  }


  case class Karatsuba(mid: Int, a: BigInt, b: BigInt, c: BigInt, d: BigInt)

  def divide(x: BigInt, y: BigInt): Karatsuba = {

    if(x < 10 || y < 10) Karatsuba(1, 0, x, 0, y)
    else {
      val xstr: String = x.toString
      val n: Int = xstr.length
      val mid: Int = n / 2
      val (a, b) = split(x, mid)
      val (c, d) = split(y, mid)
      Karatsuba(mid, a, b, c, d)
    }
  }

  def split(x: BigInt, n: Int): (BigInt, BigInt)= {
    val xstr: String = x.toString
    val a = BigInt(xstr.dropRight(n))
    val b = BigInt(xstr.takeRight(n))
    (a, b)
  }

}
