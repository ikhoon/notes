package refiendnote

import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric._
import shapeless._

/**
  * Created by Liam.M on 2017. 08. 24..
  * 24/08/2017
  */
object normal {



  def factorial(n: Int): BigInt = {
    if(n < 0) throw new IllegalArgumentException("n은 0보다 큰 양수이어야 합니다")

    if(n == 0) 1
    else n * factorial(n - 1)
  }
  factorial(0)
  factorial(-10)
}

object refined {

  type Positive = Int Refined Greater[_0]

  def factorial(n: Positive): Int = {
    if(n < 0) throw new IllegalArgumentException("n은 0보다 큰 양수이어야 합니다")

    if(n.value == 0) 1
    else n * factorial(n.value - 1)
  }

  factorial(10)
  factorial(-2)

}
