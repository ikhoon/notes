package armerianote

import com.linecorp.armeria.scalapb.testing.messages.{SimpleRequest, SimpleResponse}
import com.linecorp.armeria.server.Server
import com.linecorp.armeria.server.annotation.{ConsumesJson, Post}
import com.linecorp.armeria.server.docs.DocService

object ScalapbExample {
  def main(args: Array[String]): Unit = {
    val server = Server
      .builder()
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
