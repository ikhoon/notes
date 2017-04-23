package effnote

/**
  * Created by ikhoon on 23/04/2017.
  *
  * 원문 : http://atnos-org.github.io/eff/org.atnos.site.Introduction.html
  */
object Introduction extends App{

  // `EFF[R, A]` 타입에서 R은 effect의 set, A는 return value를 의미한다.

  // effect R 은 "effect constructors"의 type level 리스트로 형성된다.

  import cats._, data._
  import org.atnos.eff._

  type ReaderInt[A] = Reader[Int, A]
  type WriterString[A] = Writer[String, A]

  type Stack = Fx.fx3[WriterString, ReaderInt, Eval]


  // 위의 Stack은 3개의 effect를 선언한다
  // ReaderInt, Int 타입의 설정에 접근한다
  // WriterString, log string의 effect이다
  // Eval, 늦은 연산을 처리하는 effect이다

  // ReaderEffect, WriterEffect 그리고 EvalEffect에서 제공해주는 연산자로 프로그램을 작성할수 있다.
  import org.atnos.eff.all._
  import org.atnos.eff.syntax.all._

  type _readerInt[R] = ReaderInt |= R
  type _writerString[R] = WriterString |= R

  /**
    * ask, tell, delay 이 함수는 [[org.atnos.eff.all.ask]] 에 정의 되어 있다.
    * 즉 타입만 선언을 해주면 된다?
     */
  def program[R: _readerInt : _writerString : _eval]: Eff[R, Int] = for {
    n <- ask[R, Int]
    _ <- tell("the required power is " + n)
    a <- delay(math.pow(2, n.toDouble).toInt)
    _ <- tell("result is " + a)
  } yield a

  println(program[Stack].runWriter.runReader(6).runEval.run)



}
