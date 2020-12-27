package macronote

import scala.quoted.{Const, Expr, QuoteContext, Varargs}

/**
  * 스칼라 3에서 사용되는 macro 예제
  * https://blog.softwaremill.com/starting-with-scala-3-macros-a-short-tutorial-88e9d2b2584c
  */
object Debug {
  inline def hello(): Unit = println("Hello, World!")

  inline def debugHello(inline expr: Any): Unit = ${debugSingleImpl('expr)}

  def debugSingleImpl(expr: Expr[Any])(using QuoteContext): Expr[Unit] =
    '{println("Value of " + ${Expr(expr.show)} + " is " + $expr)}

  inline def debug(inline exprs: Any*): Unit = ${debugImpl('exprs)}

  def debugImpl(exprs: Expr[Seq[Any]])(using QuoteContext): Expr[Unit] = {
    def showWithValue(expr: Expr[_]): Expr[String] = '{${Expr(expr.show)} + " is " + $expr}

    val stringExprs: Seq[Expr[String]] = exprs match {
      case Varargs(es) =>
        es.map {
          case Const(s: String) => Expr(s)
          case e => showWithValue(e)
        }
      case e => List(showWithValue(e))
    }

    val concatenatedExpr: Expr[String] = stringExprs
      .reduceOption((a, b) => '{$a + ", " + $b})
      .getOrElse('{""})

    '{
      if (Main.debugEnabled) {
        println($concatenatedExpr)
      } else {
        println("info level")
      }
    }
  }
}
