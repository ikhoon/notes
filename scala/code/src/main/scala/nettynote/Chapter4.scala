package nettynote

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.{ChannelInboundHandlerAdapter, ChannelInitializer, ChannelOption}
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.http.HttpServerCodec
import io.netty.handler.logging.{LogLevel, LoggingHandler}
import io.netty.handler.ssl.{SslContext, SslContextBuilder}
import io.netty.handler.ssl.util.SelfSignedCertificate

object HttpHelloWorldServer {
  val PORT = 8443

  def main(args: Array[String]): Unit = {

    val ssc = new SelfSignedCertificate
    val sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build()
    val bossGroup = new NioEventLoopGroup(1)
    val workerGroup = new NioEventLoopGroup()
    val b = new ServerBootstrap
    b.option(ChannelOption.SO_BACKLOG, new Integer(1024))
      .channel(classOf[NioServerSocketChannel])
      .handler(new LoggingHandler(LogLevel.INFO))
      .childHandler(new HttpHelloWorldServerInitializer(sslCtx))

    val ch = b.bind(PORT).sync().channel()
    ch.closeFuture().sync()

  }
}

class HttpHelloWorldServerInitializer(sslCtx: SslContext) extends ChannelInitializer[SocketChannel] {
  override def initChannel(ch: SocketChannel): Unit = {
    val p = ch.pipeline()
    p.addLast(sslCtx.newHandler(ch.alloc()))
    p.addLast(new HttpServerCodec())
 }
}

