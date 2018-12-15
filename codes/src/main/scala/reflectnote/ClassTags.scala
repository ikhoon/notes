package reflectnote

/**
  * Created by Liam.M on 2018. 11. 05..
  */
trait AA
trait BB
object ClassTags {

  import scala.reflect._

  val aa = classTag[AA]
  val bb = classTag[BB]
  def foo[A](implicit tag: ClassTag[A]): Unit = {
    tag match {
      case x if x == aa => println("aaa")
      case y if y == bb => println("bbb")
    }
  }

  def main(args: Array[String]): Unit = {
    foo[AA]
    foo[BB]

  }
}
