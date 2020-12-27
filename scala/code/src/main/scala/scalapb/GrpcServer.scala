package scalapb

import com.linecorp.armeria.common.SessionProtocol
import com.linecorp.armeria.server.docs.DocService
import com.linecorp.armeria.server.grpc.GrpcService
import com.linecorp.armeria.server.logging.LoggingService
import com.linecorp.armeria.server.{Server, ServiceRequestContext}
import example.grpc.hello.hello.HelloServiceGrpc.HelloService
import example.grpc.hello.hello.{HelloReply, HelloRequest, HelloServiceGrpc}
import io.grpc.stub.StreamObserver

import scala.concurrent.{ExecutionContext, Future}

class HelloServiceImpl extends HelloServiceGrpc.HelloService {
  override def hello(request: HelloRequest): Future[HelloReply] = {
    println(ServiceRequestContext.current())
    Future.successful(HelloReply(request.name))
  }

  override def lazyHello(request: HelloRequest): Future[HelloReply] = ???

  override def blockingHello(request: HelloRequest): Future[HelloReply] = ???

  override def lotsOfReplies(request: HelloRequest, responseObserver: StreamObserver[HelloReply]): Unit = ???

  override def lotsOfGreetings(responseObserver: StreamObserver[HelloReply]): StreamObserver[HelloRequest] = ???

  override def bidiHello(responseObserver: StreamObserver[HelloReply]): StreamObserver[HelloRequest] = ???
}

object GrpcServer {
  def startServer(port: Option[Int] = None): Server = {
    val helloService =
      GrpcService
        .builder()
        .addService(HelloService.bindService(new HelloServiceImpl, ExecutionContext.global))
        .build()

    val serverBuilder = Server
      .builder()
      .service("/", helloService)
      .serviceUnder("/docs", new DocService())
      .decorator(LoggingService.newDecorator())
    port.foreach {
      serverBuilder.port(_, SessionProtocol.HTTP)
    }

    val server = serverBuilder.build()
    server.start().join()
    server
  }
}
