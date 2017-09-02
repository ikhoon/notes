package exp

import exp.path.{a1, a2}

/**
  * Created by ikhoon on 24/08/2017.
  */

object path {

  class A {

    class B

    var b: Option[B] = None
  }

  val a1: A = new A
  val a2: A = new A

  val b1: a1.B = new a1.B
  val b2: a2.B = new a2.B

  a1.b = Some(b1)
//  a2.b = Some(b1) // does not compile


}
trait Client {
  type Connection

  def connect: Connection
  def disconnect(connection: Connection): Unit
}

trait Connector
class ClientImpl extends Client {
  type Connection = Connector

  def connect: Connection = new Connector {}
  def disconnect(connection: Connection): Unit = {}
}

object Client {
  def apply(): Client = new ClientImpl
}
object run {
  val client  = Client()
  val client2 = Client()

  val connection = client.connect

  client.disconnect(connection)
//  client2.disconnect(connection)  // Type mismatch
}
