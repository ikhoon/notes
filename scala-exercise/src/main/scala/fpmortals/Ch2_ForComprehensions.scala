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
    show {
      reify {
        for { i <- a; j <- b; k <- c } yield (i + j + k)
      }
    }
  }


  a.flatMap {
    i => b.flatMap {
      j => c.map {
        k => i + j + k }}}

  // Assingment
  // for문 안에도 할당할수 있다 ij = i + j(val은 필요하지 않다)
  for {
    i <- a
    j <- b
    ij = i + j
    k <- c
  } yield (ij + k)

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





}
