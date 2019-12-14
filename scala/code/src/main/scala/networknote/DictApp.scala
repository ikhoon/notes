package networknote

import java.io.{BufferedReader, InputStreamReader, OutputStream, OutputStreamWriter}
import java.net.Socket

import cats.effect.{ExitCode, IO, IOApp, Resource}

import scala.util.control.NonFatal

object DictApp extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    val socket = new Socket("dict.org", 2628)
    socket.setSoTimeout(15000)
    withResource(socket)
      .use(
        s =>
          IO {
            val out: OutputStream = s.getOutputStream
            val writer: OutputStreamWriter = new OutputStreamWriter(out, "UTF-8")
            writer.write("DEFINE eng-lat gold\r\n")
            writer.flush()
            val in = s.getInputStream
            val reader = new BufferedReader(new InputStreamReader(in, "UTF-8"))
            var line = reader.readLine()
            while (line != ".") {
              println(line)
              line = reader.readLine()
            }
            writer.write("quit\r\n")
            writer.flush()
            ExitCode.Success
        }
      )
  }

  def withResource(socket: Socket) = {
    Resource.make(IO(socket)) { s =>
      IO(s.close()).handleErrorWith {
        case NonFatal(ex) =>
          ex.printStackTrace()
          IO.unit
      }
    }
  }
}
