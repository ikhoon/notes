package catsnote

import java.util.Date

import cats.free.Free
import cats.implicits._
import cats.{Monad, ~>}

import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by ikhoon on 2016. 10. 4..
  */
/**

  ## Free Monad에 대해서 알아보자. - https://github.com/typelevel/cats/blob/master/docs/src/main/tut/freemonad.md
  Free Monad는 scala-exercise-cats에 나와 있지는 않음

  Free Monad는 어떤 functor로 부터 monad를 만들수 있게 한다.
  Free Monad가 사용되는 곳은
  * 상태를 가지고 있는 데이터를 표현하고 실행할때
  * stack overflow 를 피하면서 재귀문을 실행할때
  * 내부 DSL을 만들때
  * natural transaformation을 이용하여 다른 interperter로 연산을 재조정할때

  cats에서 free monad 쓸려면 `cats-free` 모듈이 의존성에 추가되어야 한다.

  ## 주제 공부하기
  key-value 저장소를 위한 DSL만드는것을 생각해보자.
  key를 가지고 3가지 행위가 가능해야 한다.

  * put - 키와 연관된 값을 저장한다.
  * get - 주어진 키의 값을 반환한다.
  * delete - 주어진 키의 값을 지운다.

  위 DSL의 연산자의 일련의 행위들을 작성한다 - `program`이 된다.
  program을 컴파일 한다.
  마지막으로 program을 실제 key-value 저장소와 상호 작용할수 있게 실행한다.

  ```
  put("toto", 3)
  get("toto") // returns 3
  delete("toto")
  ```
  요런것들이다.

  그러나 우리가 원하는건
  * 연산이 순수하고 불변의 값으로 표현되었음 한다.
  * program의 생성과 실행을 분리하고 싶다.
  * 다양한 실행가능한 함수를 지원가능해야 한다.

  ## 문법 공부하기

  3개의 명령어가 있다
  * Put - 저장소에 들어가는 key와 연관된 값이다.
  * Get - 저장소에서 나오는 key와 연관된 값이다.
  * Delete - 저장소에서 지울 key와 연관된 값이다.

  */
// ## 문법에 대해서 ADT를 만들어 보자.
sealed trait KVStoreA[A]
case class Put[T](key: String, value: T) extends KVStoreA[Unit]
case class Get[T](key: String) extends KVStoreA[Option[T]]
case class Delete(key: String) extends KVStoreA[Unit]
case class Gets[T](key: List[String]) extends KVStoreA[List[T]]

/**
  ## ADT를 free 해보자.
  "freeing" ADT를 위해서는 6가지 스탭이 있다. - 아따 많다잉
  1. 타입 기반의 Free[_] 와 KVStoreA[_]를 만들어라.
  2. `liftF`를 이용하여 KVStoreA[_]를 위한 똑똑한 생성자를 만들어라.
  3. key-value DSL 연산에 기인한 program을 만들어라.
  4. DSL 연산을 위한 컴파일러를 만들어라.
  5. 컴파일된 코드를 실행하라.

  */
// 구현
object kv {
  // 1. ADT기반으로 Free type을 만들어라.
  // KVStoreA에 A를 뒤에 붙인 이유가 있었구만. type alias에 사용할라 했었다.
  type KVStore[A] = Free[KVStoreA, A]

  import cats.free.Free.liftF
  // 2. liftF을 이용하여 똑똑한 생성자를 만들라.
  // liftF를 이용해서 KVStoreA를 KVStore로 바꾸었다.
  // ADT를 Free 타입으로 lifting하였다.
  def put[T](key: String, value: T): KVStore[Unit] =
    liftF[KVStoreA, Unit](Put(key, value))

  def get[T](key: String): KVStore[Option[T]] =
    liftF[KVStoreA, Option[T]](Get(key))

  def delete(key: String): KVStore[Unit] =
    liftF[KVStoreA, Unit](Delete(key))

  // get이랑 put을 조합해서 만들자.
  def update[T](key: String, f: T => T): KVStore[Unit] =
    for {
      maybeV <- get[T](key)
      _ <- maybeV.map(v => put[T](key, f(v))).getOrElse(cats.free.Free.pure(()))
    } yield ()

  // 3. 프로그램을 만들어 보자.
  // 이제 for comprehension을 이용하여 DSL을 사용하여 프로그램을 작성하여 KVStore[_]을 만드는 것이 가능하다.

  def program: KVStore[Option[Int]] =
    for {
      _ <- put("wild-cats", 2)
      _ <- update[Int]("wild-cats", _ + 12)
      _ <- put("tame-cats", 5)
      n <- get[Int]("wild-cats")
      _ <- delete("tame-cats")
    } yield n

  // 이것은 monadic flow 처럼 보인다. 그러나 **일련의 작업을 표현하기 위해 재귀적인 데이터 구조를 만든것 뿐이다.**

  // 4. 프로그램을 위한 컴파일러를 만들어 보자.

  // 지금까지 이해가 된걸로는, Free[_]는 내장 DSL을 만들기 위해서 사용되었다.
  // 이 DSL은 일련의 작업을 표현한다.(재귀 자료구조형을 이용하여) 그리고 그 자체로 아무것도 생산하지 않는다.

  // Free[_]는 프로그램 언어안에 있는 하나의 프로그램 언어이다.
  // 다른 프로그램 언어와 마찬가지로, 추상적인 언어를 효율적인 언어로 컴파일 할필요가 있다. 그리고 그것을 실행한다.

  // 이것을 위해서, type container간에 natural transformation을 사용할것이다.
  // natural transformation은 F[_]와 G[_] 간의 타입을 바꾸는 것이다.
  // 이 tranformation은 `FunctionK[F, G]`로 작성이 되어 있으며 여기서는 `F ~> G` 심볼을 이용한다.

  // key value store를 표현하기 위해서 단순히 mutable map을 사용할것 이다.

  // F[A] => F[B] // functor
  val a: Option[Int] = Some(1)
  val b: Option[String] = a.map(_.toString)

  type MapInt[K] = Map[Int, K]
  // F[G[A]] => G[F[A]] // sequence
  // F[G[A]] => G[F[B]] // sequence + map = traverse
  // F[A] => G[A] // natural transformation

  val c: List[Int] = a.toList

  // KVStoreA[A] => Id[A]
  //

  def impureCompiler[M[_]: Monad]: KVStoreA ~> M =
    new (KVStoreA ~> M) {
      println("new transformer" + new Date)
      val kvs = mutable.Map.empty[String, Any]

      def apply[A](fa: KVStoreA[A]): M[A] =
        fa match {
          case Get(key) =>
            println(s"get($key)")
            Monad[M].pure(kvs.get(key).map(_.asInstanceOf[A]))
          case Put(key, value) =>
            println(s"put($key, $value)")
            kvs.put(key, value)
            Monad[M].pure(())
          case Delete(key) =>
            println(s"delete($key)")
            kvs.remove(key)
            Monad[M].pure(())
        }
    }

  /**

    이 impureCompiler는 순수함수가 아니다(impure 이다)라는 점을 주목하라.
    kvs의 상태를 변화 시키고 println을 통해서 logging을 출력하였다.
    함수형 프로그래밍의 목적은 side-effect를 막는것이 아니다.
    단지 side-effect를 제어가 되는 방법으로 시스템에서 경계선을 가지고 모아 놓는 것이다.

    Id[_]은 단순한 타입 컨테이너이다. Id[Int]는 Int이다. 즉 우리의 프로그램은 바로 실행이 될것이고, 마지막 값이 반환될때까지 블로킹될 것이다.

    다양한 행동을 위해서 우리는 다른 타입을 활용할수 있다.

    * Future[_]는 비동기 연산을
    * List[_]는 여려개의 결과를 모을때
    * Option[_]은 선택적인 결과를 지원하기 위해
    * Either[E, *]는 실패를 지원하기 위해
    * 그리고 더 있단다....

    # 5. 프로그램을 실행하자
    마지막 단계로 컴파일된 녀석을 실행시키자.

    Free[_]는 단지 순차적인 연산으로 다른 연산을 만드는 재귀적인 구조일뿐이다.
    이방식은 List[_]와 유사하다.
    우리는 list에서 하나의 값을 얻기 위해서 종종 fold(foldRight 등등)를 사용 한다.
    이것은 그 구조(list)를 재귀하며 그의 결과를 합친다.

    **Free[_]의 실행하는 아이디어도 이것과 동일하다.**
    우리는 재귀적인 구조를 아래 사항을 이용하여 fold한다.
    * 각각의 연산을 소비한다.
    * 이 연산을 impureCompiler를 이용해서 효율적인 언어로 컴파일한다.(만약 그것에 effect들이 있으면 적용한다. 어떤것이라도)
    * 다음 연산을 실행한다.
    * `Pure` 상태가 될때까지 재귀적으로 계속 진행하고 반환한다.

    이 연산을 `Free.foldMap`이라고 부른다.

    ```
    final def foldMap[M[_]](f: FunctionK[S, M])(M: Monad[M]): M[A] = ...
    ```
    M은 flatten하기 위해서 모나드가 되어야 한다.
    Id는 Monad로서 foldMap을 사용할수 있다.

    */
  // TODO FIXME
  //def result = program.foldMap(impureCompiler)

  /**
    foldMap의 중요한점은 stack-safety하다는 것이다.
    stack에 있는 각각의 step을 진행 unstack하고 재시작한다.

    당신의 natural tranformation이 stack-safety하는한 foldMap은 절대 stack overflow하지 않는다.
    힙 사용에 중점을 둔, stack-safety를 제공하는 Trampoline은 데이터 집중적인 작업들과 무한대의 처리과정이 있는 stream 에 대해서도
    Free[_]를 사용하는것을 요구함으로서 안정성을 제공한다.

    *
    */
  private val map: Int = List(1, 2, 3).foldMap(i => i)

  println(map)
}
