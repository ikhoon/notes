import scala.collection.mutable
val a = mutable.ArrayBuffer[Int]()

a.+=(1)


case class Foo(a: Int, b: Int)
Foo(1, 2)

class Bar(val a: Int, val b: Int) {
  override def toString = s"Bar($a, $b)"
}
new Bar(1, 2)

// ADT
// algebraic data types

def aaa(): Boolean = {
  if(false) true
  else throw new Exception
}


