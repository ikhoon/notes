import cats.effect.IO
def foo: IO[Unit] = IO {
  println("hello")
}
def bar : IO[Unit] = IO {
  println("world")
}

def quz: IO[Unit] = IO {
  println("martin")
}

println("io start")
val d = for {
  a <- foo
  a1 <- foo
  a2 <- foo
  b <- bar
  c <- quz
} yield  ()

d.unsafeRunSync()


import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

def foo1: Future[Unit] = Future {
  println("hello1")
}
def bar1: Future[Unit] = Future {
  println("world1")
}

def quz1: Future[Unit] = Future {
  println("martin1")
}

println("future start")
val d1 = for {
  a <- foo1
  a1 <- foo1
  a2 <- foo1
  b <- bar1
  c <- quz1
} yield  ()

Await.result(d1, Duration.Inf)
println("???")


import cats.implicits._
val a = IO(10)
val b = IO(20)
a <+> b
a .getb









// fp 근간이 된다.
// functional programming
// pure
// side effect x
// immutable
// compose

