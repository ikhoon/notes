package patternnote

/**
 * Created by Liam.M on 2018. 11. 30..
 */

object MagnetPattern extends App {

  // magnet ? pattern?

  // override =>
  // overload =>

  def foo(int: Int) : String = int.toString
  def foo(str: String): String = str
  def foo(str: String)(str2: String): String = str



  trait MyMagnet {
    type Input
    def getInput(): Input
  }

  def bar(magnet: MyMagnet): magnet.Input =
    magnet.getInput()

  // int => magnet

  implicit def intToMagnet(int: Int): MyMagnet = {
    new MyMagnet {
      override type Input = Int
      override def getInput(): Input = int
    }
  }

  implicit def strToMagnet(str : String): MyMagnet = {
    new MyMagnet {
      override type Input = String
      override def getInput(): Input =str
    }
  }
  implicit def tupleToMagnet(tuple: (String, String)): MyMagnet = {
    new MyMagnet {
      override type Input = (String, String)
      override def getInput(): Input = tuple
    }
  }
  // Open & Close principle

  bar(10)
  bar("hello")


}
