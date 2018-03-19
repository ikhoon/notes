package stdnote

/**
  * Created by Liam.M on 2018. 03. 06..
  */

trait Base {
  def id: Int
}
case class Foo(id: Int) extends Base
case class Foo2(id: Int)

object Foo2 {
  implicit val fooInstance = new HasId[Foo2] {
    def apply(a: Foo2): Int = a.id
  }
}


trait HasId[A] {
  def apply(a: A): Int
}

object Test {
  def getId(x: Base): Int = {
    x.id
  }
  def getId2[A](x: A)(implicit hasId: HasId[A]): Int = {
    hasId(x)
  }
  getId2(Foo2(10))
  getId2(Foo2(10))(Foo2.fooInstance)
}


// 1 번째 : 인터페이스 정의
// 2 번째 : 인스턴스 만들기
// 3 번째 : 세상과의 통로 만들기 인터페이스 오브젝트
// 4 번째 : 문법 요소 만들기

// F[_] => Higher kind, type constructor
// List => Int => List[Int]
//      => String => List[String]
// 1 번째 : 인터페이스 정의
trait Functor[F[_]]  {
  def map[A, B](fa: F[A])(f: A => B): F[B]

}

// T U V
object Functor {
  // 2 번째 : 인스턴스 만들기
  implicit val functor = new Functor[List] {
    def map[A, B](fa: List[A])(f: A => B): List[B] = ???
  }

  // 3 번째 : 세상과의 통로 만들기 인터페이스 오브젝트
  def map[F[_], A, B](fa: F[A])(f: A => B)(implicit F: Functor[F]) = {
    F.map(fa)(f)
  }

  // 4 번째 : 문법 요소 만들기
  implicit class FunctorOps[F[_], A](fa: F[A]) {
    def map[B](f: A => B)(implicit F: Functor[F]): F[B] = F.map(fa)(f)
  }

  case class Bar[A](x: A)

  implicit val barInstance = new Functor[Bar] {
    def map[A, B](fa: Bar[A])(f: A => B): Bar[B] = Bar(f(fa.x))
  }
  import cats.implicits._

  val bar = Bar(10)
  bar map (_ + 10)

}




















