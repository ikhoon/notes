package stdnote

/**
 * Created by ikhoon on 2019-02-15.
 */

/**
  * toString
  * equals
  * getter & setter
  * apply, unapply
  * Product
  * hashCode
  * Serializable
  *
  */
case class CaseClass(
  a: Int,
  b: String
)

trait BCD {
  def bcd(): String
}

// 외부에서 상속을 못하게 한다.

sealed trait CDE
object CDE {
  final case class A() extends CDE
  final case class B() extends CDE
}


