package monoclenote

import monocle.PPrism
import monocle.macros.{GenIso, GenPrism}

import scala.util.Try

/**
  * Created by ikhoon on 22/02/2018.
  */
object PrismExample extends App {

  // Prism은 Sum 타입을 위한 optic이다.
  // Prism은 S, A 두대의 타입 파라메터를 받고 있다.
  // S는 Sum타입을 표현하고, A는 Sum타입의 일부이다.

  sealed trait Json
  case object JNull extends Json
  case class JStr(v: String) extends Json
  case class JNum(v: Double) extends Json
  case class JObj(v: Map[String, Json]) extends Json

  // Prism의 생성자는 두개의 함수를 인자로 받는다.
  // getOption: Json => Option[String]
  // reverseGet: String => Json

  import monocle.Prism
  val jStr: Prism[Json, String] = Prism[Json, String] {
    case JStr(v) => Some(v)
    case _ => None
  }(JStr)
  println(jStr.getOption(JStr("hello")))
  println(jStr.getOption(JNum(10)))

  val jStr2 = Prism.partial[Json, String] { case JStr(v) => v }(JStr)
  println(jStr2.getOption(JStr("hello")))
  println(jStr2.getOption(JNum(10)))

  // jStr는 pattern matching에 바로 쓸수 있다.
  def isLongString(json: Json): Boolean = json match {
    case jStr(v) => v.length > 100
    case _ => false
  }


  // 그리고 Json이 오직 JStr일때만 set과 modify를 적용해서 update를 할수 있다.
  val json: Json = jStr.set("world")(JStr("hello"))
  println(s"json = ${json}")
  val json2: Json = jStr.modify(_.reverse)(JStr("hello"))
  println(s"json2 = ${json2}")

  val json3: Json = jStr.modify(_.reverse)(JNum(10))
  println(s"json3 = ${json3}")

  // 성공과 실패에 신경을 써야한다면
  val json4: Option[Json] = jStr.setOption("world")(JStr("hello"))
  println(s"json4 = ${json4}")

  val json5: Option[Json] = jStr.modifyOption(_.reverse)(JNum(10))
  println(s"json5 = ${json5}")


  // 아래와 같이 Json 타입과 JStr(S), 그리고 String(A)의 3개의 타입 사이의 관계만 설정해 놓으면 나머지는
  Prism.partial[Json, String] { case JStr(v) => v }(JStr)
  // 다양한 경우에 필요한 api들은 사용할수 있다.

  // 그리고 역시나  monocle에서 compose가 가장 중요한 핵심이다.
  val strToInt = Prism[String, Int](str => Try { str.toInt }.toOption)(_.toString)
  val jInt: Prism[Json, Int] = jStr composePrism strToInt
  val json6: Json = jInt.set(20)(JStr("10"))
  println(s"json6 = ${json6}")

  println(s"jInt(5) = ${jInt(5)}")

  println(jInt.getOption(JNum(5.0)))

  println(jInt.getOption(JNum(5.2)))

  println(jInt.getOption(JStr("Hello")))


  // 그리고 내장함수도 많다.
  import monocle.std.int.intToChar
  val jChar: PPrism[Json, Json, Char, Char] = jInt composePrism intToChar
  val json7: Json = jChar('A')
  println(s"json7 = ${json7}")


  // Prism을 만드는것도 역시나 macro가 존재한다.
  val rawJNum: Prism[Json, JNum] = GenPrism[Json, JNum]

  val json8 : Option[JNum] = rawJNum.getOption(JNum(10))
  println(s"json8 = ${json8}")
  val json9: Option[JNum] = rawJNum.getOption(JStr("good"))
  println(s"json9 = ${json9}")

  // Prism[Json, JNum] 대신 Prism[Json, Double]을 원한다면
  // 지난번에 GenIso는 하나의 필드가 있는 case class에 대해서 Iso를 만들어 주었다.
  // 이를 활용하면 된다.
  // compose it!

  val rawDouble: Prism[Json, Double] = GenPrism[Json, JNum] composeIso GenIso[JNum, Double]
  val d1: Option[Double] = rawDouble.getOption(JNum(10))
  println(s"d1 = ${d1}")
  val json10 = rawDouble(10)
  println(s"json10 = ${json10}")


}
