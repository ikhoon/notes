package macronote

import scala.reflect.macros.whitebox

/**
  * Created by ikhoon on 06/08/2017.
  */

//@macrocompat.bundle
class NatMacros(val c: whitebox.Context) {
  import c.universe._
  def materialize(i: Tree): Tree = {
    def loop(n: Int, acc: Tree): Tree = {
      if(n <= 0) acc
      else loop(n - 1, q"new shapenote.Succ[$acc]")
    }

    i match {
      case Literal(Constant(n : Int)) if n >= 0 =>
        loop(n, q"shapenote.Nat.Zero")
      case _ =>
        c.abort(c.enclosingPosition, s"$i not a natural number")
    }
  }
}
