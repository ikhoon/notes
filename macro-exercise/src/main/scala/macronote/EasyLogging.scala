package macronote

import scala.reflect.macros.blackbox

/**
  * Created by Liam.M on 2018. 07. 31..
  */
object EasyLogging {
  def log(message: String): Unit = macro EasyLoggingImpl.logImpl
  def trace[V](message: V): Unit = macro EasyLoggingImpl.logWithMethodName[V]
}

class EasyLoggingImpl(val c: blackbox.Context) {
  import c.universe._
  def logImpl(message: c.Expr[String]): c.Tree = {
    val msg = message
    q"""
        println("some prefix " + $msg)
     """
  }

  def logWithMethodName[V: c.WeakTypeTag](message: c.Tree): c.Tree = {
    val p = c.macroApplication.pos
    val file = p.source.file.name
    val line = p.line
    val methodname = getMethodSymbol().asMethod.name

    val variableName = message
    val logMessage = s"$file:$line [$methodname] $variableName = "
    q"println($logMessage + $message)"
  }


  private def getMethodSymbol(): c.Symbol = {

    def getMethodSymbolRecursively(sym: Symbol): Symbol = {
      if (sym == null || sym == NoSymbol || sym.owner == sym)
        c.abort(
          c.enclosingPosition,
          "This memoize block does not appear to be inside a method. " +
            "Memoize blocks must be placed inside methods, so that a cache key can be generated."
        )
      else if (sym.isMethod)
        sym
      else
        getMethodSymbolRecursively(sym.owner)
    }

    getMethodSymbolRecursively(c.internal.enclosingOwner)
  }
}
