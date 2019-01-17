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

  trait Fruit
  trait Apple extends Fruit
  trait Orange extends Fruit

  val ys: List[Fruit] = List(new Apple {}, new Orange {})
  val zs: List[Apple] = List(new Apple {}, new Apple {})

  def bar[A <: Fruit: ClassTag](xs: List[Fruit]): List[Fruit] = {
    xs.filter {
      case _: A => true
      case _    => false
    }
  }

  bar[Apple](ys)
  def quz[A](xs: List[_]): String = {
    xs match {
      case _: List[Apple]  => "apple"
      case _: List[Orange] => "orange"
      case _ => "unknown"
    }
  }
  def qux[A](xs: List[_]): String = {
    xs match {
      case _: List[A]  => "matched"
      case _ => "unknown"
    }
  }

}
