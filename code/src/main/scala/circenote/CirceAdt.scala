package circenote

import io.circe.generic.JsonCodec
import io.circe.generic.extras.Configuration

/**
  * Created by Liam.M on 2018. 08. 16..
  */
object CirceAdt {

  sealed trait Foo[A] {
    val a: A
  }

  case class Bar() extends Foo[String] {
    val a = "bar"
  }


  case class Quz() extends Foo[Int] {
    val a = 10
  }

  case class Wrapper[A](a: Foo[A])

  def main(args: Array[String]): Unit = {
    import io.circe.syntax._
    import io.circe.generic.extras.auto._
    implicit val genDevConfig: Configuration =
      Configuration.default.withDiscriminator("type")
    val bar: Wrapper[String] = Wrapper(Bar())
    val quz = Wrapper(Quz())

    println(bar.asJson.noSpaces)
    println(quz.asJson.noSpaces)
  }
}
