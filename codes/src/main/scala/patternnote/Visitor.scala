package patternnote

/**
  * Created by ikhoon on 2018-10-15.
  *
  * data 와 logic의 분리
  * data는 element로 로직은 visitor로 표현한다.
  */
trait Element {
  def accept(visitor: Visitor): Unit
}

class Tire extends Element {
  override def accept(visitor: Visitor): Unit =
    visitor.visit(this)
}

class Body extends Element {
  override def accept(visitor: Visitor): Unit =
    visitor.visit(this)
}

class SideMirror extends Element {
  override def accept(visitor: Visitor): Unit =
    visitor.visit(this)
}
class Window(
  val left: Element,
  val right: Element
) extends Element {
  override def accept(visitor: Visitor): Unit = {
    left.accept(visitor)
    right.accept(visitor)
    visitor.visit(this)
  }
}

class Car(
  elements: Vector[Element]
) extends Element {
  override def accept(visitor: Visitor): Unit = {
    elements.foreach(element => element.accept(visitor))
    visitor.visit(this)
  }
}
trait Visitor {
  def visit(tire: Tire)
  def visit(body: Body)
  def visit(window: Window)
  def visit(car: Car)
  def visit(sideMirror: SideMirror)
}

class PrintVisitor extends Visitor {
  override def visit(tire: Tire): Unit =
    println("visit tire")
  override def visit(body: Body): Unit =
    println("visit body")
  override def visit(window: Window): Unit =
    println(s"visit window")
  override def visit(car: Car): Unit =
    println(s"visit car")
  override def visit(sideMirror: SideMirror): Unit =
    println(s"visit side mirror")
}

object Main {
  def main(args: Array[String]): Unit = {
    val car = new Car(
      Vector(
        new Tire,
        new Window(
          new SideMirror,
          new SideMirror
        ),
        new Body
      )
    )

    val visitor = new PrintVisitor
    car.accept(visitor)
  }
}

