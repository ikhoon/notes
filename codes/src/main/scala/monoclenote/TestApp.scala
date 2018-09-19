package monoclenote

import monocle.{Iso, PIso, PPrism, Prism}

/**
  * Created by Liam.M on 2018. 02. 22..
  */
object TestApp extends App {

  // Iso : Int => String
  val iso = Iso[Int, String](_.toString)(_.toInt)
  val str: String = iso.get(10)
  iso.reverseGet("10")

  // String => Double
  val iso2 = Iso[String, Double](_.toDouble)(_.toString)

  val iso3: Iso[Int, Double] = iso composeIso iso2

  import monocle.std.string.stringToLong

  val iso4: Prism[Int, Long] = iso composePrism stringToLong
  val y: Option[Long] = iso4.getOption(10)

}
