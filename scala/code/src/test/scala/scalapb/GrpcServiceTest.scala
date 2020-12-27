package scalapb

import com.linecorp.armeria.client.Clients
import com.linecorp.armeria.server.Server
import example.grpc.hello.hello.{HelloRequest, HelloServiceGrpc}
import io.grpc.{ManagedChannel, ManagedChannelBuilder}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class GrpcServiceTest extends AnyFunSuite with Matchers with BeforeAndAfterAll {

  var server: Server = _

  override protected def beforeAll(): Unit = {
    server = GrpcServer.startServer()
  }

  override protected def afterAll(): Unit = {
    server.stop().join()
  }

  test("abc") {
    val blockingStub = Clients
      .builder("gproto+http://127.0.0.1:" + server.activeLocalPort())
      .build(classOf[HelloServiceGrpc.HelloServiceBlockingStub])
    val reply = blockingStub.hello(HelloRequest("hello"))
    reply.message shouldBe "hello"
  }

  test("upstream") {
    val channel: ManagedChannel = ManagedChannelBuilder
      .forAddress("127.0.0.1", server.activeLocalPort())
      .usePlaintext()
      .build()

    val stub = HelloServiceGrpc.blockingStub(channel)
    val reply = stub.hello(HelloRequest("hello"))
    reply.message shouldBe "hello"
  }
}
