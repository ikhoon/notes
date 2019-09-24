import scala.reflect.ClassTag
trait FruitA
trait AppleA extends FruitA {
  override def toString = "apple"
}
trait OrangeA extends FruitA {
  override def toString = "orange"
}

val ys: List[FruitA] = List(new AppleA {}, new OrangeA {})
val zs: List[AppleA] = List(new AppleA {}, new AppleA {})
val oranges: List[OrangeA] = List(new OrangeA {}, new OrangeA {})

def bar[A <: FruitA: ClassTag](xs: List[FruitA]): List[FruitA] = {
  xs.filter {
    case _: A => true
    case _    => false
  }
}

bar[AppleA](ys)





import scala.reflect.runtime.universe._
def quz[A <: List[FruitA]](xs: A)
  (implicit tag: TypeTag[A]): String = {
  xs match {
    case _ if tag.tpe =:= typeOf[List[AppleA]] => "apple"
    case _ if tag.tpe =:= typeOf[List[OrangeA]] => "orange"
    case _ => "unknown"
  }
}

quz(oranges)


def qux[B](xs: B)
  (implicit tag: WeakTypeTag[B]): String = {
    xs match {
      case _ if tag.tpe =:= weakTypeOf[List[AppleA]] => "apple"
      case _ if tag.tpe =:= weakTypeOf[List[OrangeA]] => "orange"
      case _ => "unknown"
  }
}

def bar[A] = qux()

