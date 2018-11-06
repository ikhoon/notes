package patternnote
import patternnote.OptimizationPrint.semantics

import scala.concurrent.Future

/**
 * Created by ikhoon on 2018-10-28.
 */

trait Regions {
  // Ordinary type synonyms
  type Vector = (Double, Double)

  // Abstract domain types
  type Region

  // Abstract domain operations
  def univ: Region
  def empty: Region
  def circle: Region
  def scale(v: Vector, x: Region): Region
  def union(x: Region, y: Region): Region

}

trait Evaluation extends Regions {
  type Region = Vector => Boolean

  override def univ: Vector => Boolean = p => true
  override def empty: Vector => Boolean = p => false
  override def circle: Vector => Boolean =
    p => p._1 * p._1 + p._2 * p._2 < 1
  override def scale(v: Vector, x: Region): Region =
    p => x(p._1 / v._1, p._2 / v._2)
  override def union(x: Region, y: Region): Region =
    p => x(p) || y(p)
}

object Eval extends Evaluation

trait Printing extends Regions {
  type Region = String

  override def univ: Region = "univ"
  override def empty: Region = "empty"
  override def circle: Region = "circle"
  override def scale(v: Vector, x: Region): Region =
    s"scale($v, $x)"
  override def union(x: Region, y: Region): Region =
    s"union($x, $y)"
}

object Print extends Printing

trait Optimization extends Regions {
  val semantics: Regions

  type Region = (semantics.Region, Boolean)

  override def univ: Region = (semantics.univ, true)
  override def empty: Region = (semantics.empty, false)
  override def circle: Region = (semantics.circle, false)

  override def scale(v: Vector, x: Region): Region =
    if(x._2) (semantics.univ, true)
    else (semantics.scale(v, x._1), false)

  override def union(x: Region, y: Region): Region =
    if(x._2 || y._2) (semantics.univ, true)
    else (semantics.union(x._1, y._1), false)
}

object OptimizationPrint extends Optimization {
  val semantics: Regions = Print
}

import scala.concurrent.ExecutionContext.Implicits.global
trait Functions {
  // Abstract domain types
  type Rep[X]

  // Abstract domain operations
  def fun[S, T](f: Rep[S] => Rep[T]): Rep[S => T]
  def app[S, T](f: Rep[S => T], v: Rep[S]): Rep[T]
}

trait FunEval extends Functions {
  type Rep[T] = T
  def fun[S, T](f: S => T): S => T = f
  def app[S, T](f: S => T, v: S): T = f(v)
}

trait FunPrinting extends Functions {
  type Rep[T] = String
  def fun[S, T](f: String => String): String => String = {
    v => s"fun($v => ${f(v)})"
  r
}

object Run {

  def main(args: Array[String]): Unit = {
    println(program(Print))
    println(program(Eval)((1, 2)))
    println(program(OptimizationPrint))
  }
  def program(semantics: Regions): semantics.Region = {
    import semantics._
    val ellipse24 = scale((2, 4), circle)
    union(univ, ellipse24) // The returned expression
  }


}
