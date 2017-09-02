package exp

/**
  * Created by ikhoon on 26/08/2017.
  */

object hmap {
  trait Key { type Value }
  trait HMap {
    def get(key: Key): Option[key.Value]
    def add(key: Key)(value: key.Value): HMap
  }

}
trait Key { type Value }

trait HMap { self =>
  val underlying: Map[Any, Any]
  def get(key: Key): Option[key.Value] =
    underlying.get(key).map(_.asInstanceOf[key.Value])

  def add(key: Key)(value: key.Value): HMap =
    new HMap {
      val underlying = self.underlying + (key -> value)
    }

}

object HMap {
  val empty = new HMap {
    override val underlying: Map[Any, Any] = Map.empty
  }
}
object RunMap {
  def main(args: Array[String]): Unit = {
    val map1: Map[String, Int] = Map("width" -> 120)
    val map2: Map[String, Any] = map1 + ("sort" -> "time")
    val width: Option[Any] = map2.get("width")
    val sort: Option[Any] = map2.get("sort")
  }
}
object RunHMap {
  def main(args: Array[String]): Unit = {
    val sort = new Key { type Value = String }
    val width = new Key { type Value = Int }
    val hmap: HMap = HMap.empty
      .add(width)(120)
      .add(sort)("time")
    val maybeInt: Option[Int] = hmap.get(width)
    val maybeString: Option[String] = hmap.get(sort)

    println(maybeInt)
    println(maybeString)
  }
}
