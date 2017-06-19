package macronote

import scala.reflect.runtime.universe._
/**
  * Created by ikhoon on 07/05/2017.
  */
class Macros {
  def createMacro[A: WeakTypeTag]: Tree = {
    val targetType = weakTypeOf[A]
//    val applyMethod = find
    ???
  }
}
