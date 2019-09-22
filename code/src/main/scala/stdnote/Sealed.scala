package stdnote

/**
  * Created by Liam.M on 2017. 12. 06..
  */

sealed trait A
case class B() extends A
case class C() extends A

object TestSealed {

  def getA: A = B()

  def main(args: Array[String]): Unit = {
    val a = getA

    a match {
      case b: B => println("bb")
      case c: C => println("cc")
    }

  }

}
