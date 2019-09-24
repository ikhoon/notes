println("hello")

import shapeless._

// "hello", 20, true

// 1 번 퀴즈
// HList를 만드는데 string, int, boolean
// 이중에서 두번째 int의 값을 가져와 봅니다.
val hlist = "hello" :: 20 :: true :: HNil
hlist.tail.head

// 2 번 퀴즈 tuple로도 만들어 봅니다.
val tuple: (String, Int, Boolean) = ("hello", 20, true)
tuple._2


// 3 번 퀴즈 list로도 만들어 봅니다.
val list = List("hello", 20, true)
list.tail.head

case class Employee(name: String, number: Int, manager: Boolean)
case class IceCream(name: String, numCherries: Int, inCone: Boolean)

def employeeCsv(e: Employee): List[String] =
  List(e.name, e.number.toString, if(e.manager) "yes" else "no")

def iceCreamCsv(c: IceCream): List[String] =
  List(c.name, c.numCherries.toString, if(c.inCone) "yes" else "not")


val e = Employee("edina", 1, true)

val i = IceCream("vera", 0, false)

employeeCsv(e)
iceCreamCsv(i)

// case class to generic(hlist)
// 각각의 도메인
val edinaGeneric = Generic[Employee].to(e)
val creamGeneric = Generic[IceCream].to(i)

// core
def toCsv(sib: String:: Int:: Boolean :: HNil): List[String] =
  List(sib.head, sib.tail.head.toString, if(sib.tail.tail.head) "yes" else "no")

toCsv(edinaGeneric)
toCsv(creamGeneric)


// 관심사의 분리

// 각각의 도메인 있고 공통된 도메인(core)
