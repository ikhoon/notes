package nettynote

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import io.netty.bootstrap.{Bootstrap, ServerBootstrap}
import io.netty.buffer.{ByteBuf, Unpooled}
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.{NioServerSocketChannel, NioSocketChannel}
import io.netty.channel.{
  ChannelHandlerContext,
  ChannelInboundHandlerAdapter,
  ChannelInitializer,
  SimpleChannelInboundHandler
}
import java.nio.charset.Charset
import scala.concurrent.duration._

object DiscardServer {
  def main(args: Array[String]): Unit = {

    val server = IO((new NioEventLoopGroup(1), new NioEventLoopGroup)).bracket {
      case (bossGroup, workerGroup) =>
        IO {
          val b = new ServerBootstrap
          b.group(bossGroup, workerGroup)
            .channel(classOf[NioServerSocketChannel])
            .childHandler(new ChannelInitializer[SocketChannel] {
              override def initChannel(ch: SocketChannel): Unit = {
                val p = ch.pipeline()
                p.addLast(new DiscardServerHandler)
              }
            })

          val f = b.bind(8888).sync()
          f.channel().closeFuture().sync()
        }
    } {
      case (bossGroup, workerGroup) =>
        IO {
          bossGroup.shutdownGracefully()
          workerGroup.shutdownGracefully()
        }

    }
    server.unsafeRunSync()
  }

  class DiscardServerHandler extends SimpleChannelInboundHandler[AnyRef] {
    override def channelRead0(ctx: ChannelHandlerContext, msg: scala.AnyRef): Unit = {
      println(s"channel read: $msg")
      ()
    }

    override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable): Unit = {
      cause.printStackTrace()
      ctx.close()
    }
  }
}

object EchoServer {
  def main(args: Array[String]): Unit = {
    val server = IO((new NioEventLoopGroup(1), new NioEventLoopGroup())).bracket {
      case (bossGroup, workerGroup) =>
        IO {
          val b = new ServerBootstrap()
          b.group(bossGroup, workerGroup)
            .channel(classOf[NioServerSocketChannel])
            .childHandler(new ChannelInboundHandlerAdapter {
              override def channelRead(ctx: ChannelHandlerContext, msg: Any): Unit = {
                val readMessage: String = msg.asInstanceOf[ByteBuf].toString(Charset.defaultCharset())
                println(readMessage)
                ctx.write(readMessage)
              }

              override def channelReadComplete(ctx: ChannelHandlerContext): Unit = {
                println("channel readcomplete")
                ctx.flush()
              }
            })
          val f = b.bind(8888).sync()
          f.channel().closeFuture().sync()
        }
    } {
      case (bossGroup, workerGroup) =>
        IO {
          println("release resources")
          bossGroup.shutdownGracefully()
          workerGroup.shutdownGracefully()
        }
    }
    server.unsafeRunTimed(1.seconds)
  }
}

object EchoClient {
  def main(args: Array[String]): Unit = {
    val group = new NioEventLoopGroup()
    val b = new Bootstrap()
    b.group(group)
      .channel(classOf[NioSocketChannel])
      .handler(new ChannelInitializer[SocketChannel] {
        override def initChannel(ch: SocketChannel): Unit = {
          val pipeline = ch.pipeline()
          pipeline.addLast(new EchoClientHandler())
        }
      })
    val future = b.connect("localhost", 8888).sync()
    future.channel().closeFuture().sync()
  }

  class EchoClientHandler extends ChannelInboundHandlerAdapter {
    override def channelActive(ctx: ChannelHandlerContext): Unit = {
      val sendMessage = "Hello netty!"
      val messageBuffer = Unpooled.buffer()
      messageBuffer.writeBytes(sendMessage.getBytes)
      println(s"Send message ${sendMessage}")
      ctx.writeAndFlush(messageBuffer)
    }

    override def channelRead(ctx: ChannelHandlerContext, msg: Any): Unit = {
      val readMessage = msg.asInstanceOf[ByteBuf].toString(Charset.defaultCharset())
      println(s"read message : ${readMessage}")
    }

    override def channelReadComplete(ctx: ChannelHandlerContext): Unit = {
      ctx.close()
    }

    override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable): Unit = {
      cause.printStackTrace()
      ctx.close()
    }

  }
}
