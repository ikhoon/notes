package stdnote

import java.time.LocalDateTime

/**
  * Created by Liam.M on 2018. 09. 03..
  */


object Typeclass {

  trait Base {
    def id: Int
    def createdAt: LocalDateTime
  }
  case class Foo(id: Int, createdAt: LocalDateTime, name: String) extends Base
  case class Bar(primaryKey: Int, yymmdd: LocalDateTime, name: String)
  case class Quz(id: Int, createdAt: LocalDateTime, name: String)

  trait TC[A] {
    def id(a: A): Int
    def createdAt(a: A): LocalDateTime
  }

  implicit val fooInstance = new TC[Foo] {
    override def id(foo: Foo): Int = foo.id
    override def createdAt(foo: Foo): LocalDateTime = foo.createdAt
  }

  implicit val barInstance = new TC[Bar] {
    override def id(a: Bar): Int = a.primaryKey

    override def createdAt(a: Bar): LocalDateTime = a.yymmdd
  }

  def withPrimayKey[A](foo: A)(implicit tc: TC[A]) = {
    val id = tc.id(foo)
    val createdAt = tc.createdAt(foo)
  }

}
