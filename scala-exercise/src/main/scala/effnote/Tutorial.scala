package effnote

/**
  * Created by ikhoon on 23/04/2017.
  *
  * 원문 : http://atnos-org.github.io/eff/org.atnos.site.Tutorial.html
  */
object Tutorial extends App {

  // cats의 free monad tutorial을 활용한다.

  /**
    * 예를 들어
    * put("toto", 3)
    * get("toto") // return 3
    * delete("toto")
    * 이걸만들고 싶다.
    *
    * 그리고 연산은 pure하고 불변의 값이었으면 좋겠다.
    * 프로그램의 생성과 실행이 분리되었으면 좋겠다.
    * 실행에 많은 다른 함수가 가능했으면 좋겠다.
    */


  // 문법 만들기

  // 타입 파라메터 A는 연산의 반환 값의 타입이 된다.
  sealed trait KVStore[+A]

  case class Put[T](key: String, value: T) extends KVStore[Unit]
  case class Get[T](key: String) extends KVStore[Option[T]]
  case class Delete(key: String) extends KVStore[Unit]

  // ADT를 freeing하는 기본적인 4가지 step이 있다.
  // 1. Eff.send를 활용하여 KVStore[_]의 똑똑한 생성자를 만든다
  // 2. key value DSL를 활용한 program을 만든다.
  // 3. program DSL 연산을 위한 interpreter를 만든다.
  // 4. 해석된 프로그램을 실행시킨다.
  // 5. [선택사항] interpreter를 위한 syntax를 추가한다.


  // 1. Eff.send를 활용하여 KVStore[_]의 똑똑한 생성자를 만든다

  import org.atnos.eff._
  import cats.implicits._
  // T |= R 은 MemberIn[T, R]의 alias이다.
  // T[_]타입의 effect는 R의 effect stack에 삽입될수 있다.


  // 아래는 MemberIn[KVStore, R]과 같다.
  type _kvStore[R] = KVStore |= R


  // store는 아무것도 반환하지 않는다 (eg. Unit)
  def store[T, R: _kvStore](key: String, value: T): Eff[R, Unit] =
    Eff.send[KVStore, R, Unit](Put(key, value))

  // find는 T가 있으면 반환한다.
  def find[T, R: _kvStore](key: String): Eff[R, Option[T]] =
    Eff.send[KVStore, R, Option[T]](Get(key))

  // delete 는 아무것도 반환하지 않는다
  def delete[T, R: _kvStore](key: String): Eff[R, Unit] =
    Eff.send(Delete(key))


  // update는 get과 put을 합성하고 아무것도 반환하지 않는다.
  def update[T, R: _kvStore](key: String, f: T => T): Eff[R, Unit] =
    for {
      ot <- find[T, R](key)
      _ <- ot.map(t => store[T, R](key, f(t))).getOrElse(Eff.pure(()))
    } yield ()

  // 각각의 함수는 KVStore를 필요로 한다. 이 effect는 effect stack R의 member가 된다.
  // Eff[R, A]





}
