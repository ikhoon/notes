package networknote

import java.io.InputStreamReader
import java.net.Socket

import cats.effect.{ExitCode, IO, IOApp, Resource}

import scala.util.control.NonFatal

object TimeApp extends IOApp {
  def run(args: List[String]): IO[ExitCode] = {
    val socket: Resource[IO, Socket] = mkSocket("time.nist.gov", 13)
    socket.use(
      s =>
        IO {
          val in = s.getInputStream
          val reader = new InputStreamReader(in, "ASCII")
          var c: Int = reader.read()
          val builder = new StringBuilder
          while (c != -1) {
            builder.append(c.toChar)
            c = reader.read()
          }
          println(builder.mkString)
          ExitCode.Success
      }
    )

  }

  def mkSocket(host: String, port: Int): Resource[IO, Socket] = {
    val acquire: IO[Socket] = IO.pure(new Socket(host, port))

    def release(s: Socket): IO[Unit] =
      IO(s.close())
        .handleErrorWith {
          case NonFatal(ex) =>
            ex.printStackTrace()
            IO.unit
        }

    Resource.make(acquire)(release)
  }
}
