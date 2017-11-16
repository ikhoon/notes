package fpmortals

/**
  * Created by ikhoon on 16/11/2017.
  */
object Ch2_ForComprehensions {

  // 스칼라의 for comprehension은 sequential programming을 위한
  // 이상적인 FP의 추상화이다.

  // 이 챕터는 pure programm을 작성하려고 노력하지 않는다.

  // syntax sugar
  // scala의 for는 단순히 rewrite rule이다.

  val a, b, c = Option(1)

  def main(args: Array[String]): Unit = {
    import scala.reflect.runtime.universe._

    // for comprehension
    val tree = show {
      reify {
        for {i <- a; j <- b; k <- c} yield (i + j + k)
      }
    }
    println(tree)

    // assingment
    // for문 안에도 할당할수 있다 ij = i + j(val은 필요하지 않다)
    val assign = show {
      reify {
        for {
          i <- a
          j <- b
          ij = i + j
          k <- c
        } yield (ij + k)
      }
    }
    println(assign)
    // filter
    val filtered = show {
      reify {
        for {
          i <- a
          j <- b if i > j
          k <- c
        } yield (i + j + k)
      }
    }
    println(filtered)

    //foreach
    val each = show {
      reify {
        for {
          i <- a
          j <- b
        } println(s"$i $j")
      }
    }
    println(each)




  }


  // for-comprehension
  a.flatMap {
    i => b.flatMap {
      j => c.map {
        k => i + j + k }}}

  // Assingment

  // 위 코드는 와 같이 보이고 할당은 map으로 표현된다
  a.flatMap {
    i => b.map { j => (j, i + j) }.flatMap {
      case (j, ij) => c.map {
        k => ij + k }}}

  // 그리고 for문의 시작위치에 할당은 올수 없다.
  /* 아래는 유효하지 않다.
  for {
    initial = getDefault
    i <- a
  } yield initial + i
  */

  // filter
  a.flatMap {
    i => b.withFilter {
      j => i > j }.flatMap {
      j => c.map {
        k => i + j + k }}}

  // foreach
  a.foreach { i =>
    b.foreach { j =>
      println(s"$i $j")
    }
  }

  // ForComprehensible

  trait ForComprehensible[C[_]] {
    def map[A, B](f: A => B): C[B]
    def flatMap[A, B](f: A => C[B]): C[B]
    def withFilter[A](p: A => Boolean): C[A]
    def foreach[A](f: A => Unit): Unit
  }


  // Unhappy path
  for {
    i <- a
    j <- b
    k <- c
  } yield (i + j + k)

  // a, b, c중에 하나가 None이면 결과는 None이다 그러나 뭐가 잘못된지는 알수가 없다.


  // Either를 사용하자
  val aa = Right(1)
  val bb = Right(2)
  val cc: Either[String, Int] = Left("sorry, no c")

  for { i <- aa
        j <- bb
        k <- cc
  } yield (i + j + k)

  // Left(sorry, no c)


  // future도 똑같다.
  // 에러가 발생하면 short circuting이 되어서 끝난다
  import scala.concurrent._
  import ExecutionContext.Implicits.global
  val f = for {
    i <- Future.failed[Int](new Throwable)
    j <- Future { println("hello") ; 1 }
  } yield (i + j)
  Await.result(f, duration.Duration.Inf)
  // caught java.lang.Throwable




  val key = _
  // Fallback Logic
  // 어떤게 실패하면 다른걸 넣고 싶다.
  def getFromRedis(s: String): Option[String] = ???
  def getFromSql(s: String): Option[String] = ???
  getFromRedis(key) orElse getFromSql(key)


  // Async버전의 같은 api가 있다면
  def getFromRedis1(s: String): Future[Option[String]] = ???
  def getFromSql1(s: String): Future[Option[String]] = ???

  for {
    cache <- getFromRedis1(key)
    sql   <- getFromSql1(key)
  } yield cache orElse sql














}
