package armerianote

import com.linecorp.armeria.scalapb.testing.messages.{SimpleRequest, SimpleResponse}
import com.linecorp.armeria.server.{Server, ServerListener}
import com.linecorp.armeria.server.annotation.{ConsumesJson, Post}
import com.linecorp.armeria.server.docs.DocService

object ScalapbExample {
  def main(args: Array[String]): Unit = {
    var serverUri = ""
    val server = Server
      .builder()
      .serverListener(
        ServerListener
          .builder()
          .whenStarted((server: Server) => {
            serverUri = s"http://127.0.0.1:${server.activeLocalPort()}"
          })
          .build()
      )
      .http(8080)
      .annotatedService(new Object() {
        @ConsumesJson
        @Post("/")
        def foo(req: SimpleRequest): SimpleResponse = {
          SimpleResponse(req.payload)
        }
      }, Array.emptyObjectArray: _*)
      .serviceUnder("/docs", new DocService())
      .build()

    server.start().join()
  }
}
