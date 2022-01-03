package fs2note

import cats.Eval
import cats.effect.IO
import cats.effect.unsafe.implicits.global
import fs2._
import cats.implicits._
import scala.concurrent.duration._

/**
  * Created by ikhoon on 06/08/2018.
  */
object BufferTumbling {

  def main(args: Array[String]): Unit = {
    val chunked =
      Stream
        .emits(1 to 99)
        .covary[IO]
        .chunkN(10)
        .map { xs =>
          println(xs)
          xs
        }
    val list = chunked.compile.toList.unsafeRunSync()
    println(list)
  }

}
