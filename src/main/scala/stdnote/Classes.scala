package stdnote

/**
  * Created by ikhoon on 2016. 7. 28..
  */

// 함수 모습이 비슷하다.
class Point(x: Int, y: Int) {
  override def toString: String = s"($x, $y)"
}
object Classes1 extends App {

  val pt = new Point(1, 2)
  // pt.x 안됨
  // pt.y 도 안됨
  println(pt)

}

object Classes2 {
  def main(args: Array[String]): Unit = {
    val pt = new Point(10, 20)
    println(pt)

  }
}
