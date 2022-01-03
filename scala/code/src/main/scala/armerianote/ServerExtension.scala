package armerianote

import com.linecorp.armeria.common.{HttpRequest, HttpResponse}
import com.linecorp.armeria.server._

object ServerExtension {

  implicit class ServerBuilderOps(sb: ServerBuilder) {}

  def main(args: Array[String]): Unit = {
    val server = Server
      .builder()
      .service("/", new HttpService {
        override def serve(ctx: ServiceRequestContext, req: HttpRequest): HttpResponse = {
          HttpResponse.of("hello")
        }
      })
      .serverListener(
        ServerListener
          .builder()
          .whenStarted(server => println(server))
          .build()
      )
      .build()

    server.start()
  }
}
