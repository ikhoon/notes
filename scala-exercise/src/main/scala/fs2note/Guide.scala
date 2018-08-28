package fs2note

import fs2.{Chunk, Pure, Segment, Stream}
import monix.reactive.Observable

/**
  * Created by ikhoon on 16/04/2018.
  *
  * https://functional-streams-for-scala.github.io/fs2/guide.html
  * 여기 나와 있는 내용애 대해서 공부해보자.
  */
object FS2Example extends App {

  // 스트림
  // 4차원 시대에 살고 있다.
  // 시간 고려안하면서 코딩하고 있었다.
  //

  // 데이터의 핸들링을 잘할수 있다.
  //

  // Building streams

  // Stream[F, O]는 그전에는 Process라 불리던 녀석
  // O는 이산 스트림의 타입을 나타낸다
  // F는 effect

  // 이것의 effect는 Pure
  // 출력 타입은 Nothing
  val s0: Stream[Pure, Nothing] = Stream.empty

  // 이것은 effect는 Pure
  // 출력타입은 Int
  val s1: Stream[Pure, Int] = Stream.emit(1)

  // 이것도 출력타입은 Int
  val s1a: Stream[Pure, Int] = Stream(1, 2, 3)
//  s1a.mapChunks {
//    ints => Segment(ints.foldLeft(0) { _ + _})
//  }

  // seq 타입을 생성자로 받기도 한다.
  val s1b: Stream[Pure, Int] = Stream.emits(List(1, 2, 3))

  // 아무런 effect도 사용하지 않으면 pure stream 이라 한다.
  // pure stream은 List, Vector로 변환이 가능하다.

  // 중간에 cats.effect.Sync를 필요로 함
  s1.toList
  private val vector0: Vector[Int] = s1.toVector

  // 그리고 list와 관련된 함수는 많이 있음
  // map
  // filter
  // fold
  // collect
  // intersperse
  // flatMap
  // ...

  // 이때까지는 pure stream이었다면
  // FS2는 effect도 포함할수 있다.

  import cats.effect.IO

  val eff: Stream[IO, Int] = Stream.eval(IO { println("BEGIN RUN!!"); 1 + 1 })
  // 역시나 위의 코드는 side effect가 없다
  // 왜냐면 아직 실행이 되지 않았으니까

  // 그리고 eval은 단순히 IO타입만 받는게 아니라
  // cats.effect.MonadError[?, Throwable],
  // cats.effect.Sync,
  // cats.effect.Async,
  // cats.effect.Effect
  // 같은 타입의 instance가 있으면 된다.
  def eval[F[_], A](f: F[A]): Stream[F, A] = ???

  // 이제 아래 코드는 컴파일이 되지 않는다.
  // eff.toList

  // 실행하려면
  val ints: Vector[Int] = eff.compile.toVector.unsafeRunSync()
  println(ints)

  // 모든 결과물을 vector로 모은다.
  val vector: IO[Vector[Int]] = eff.compile.toVector

  // 실행은 시키되 결과물은 다 버린다.
  val drain: IO[Unit] = eff.compile.drain

  // 실행시키고 결과물을 특정 결과에 쌓는다.
  val value: IO[Int] = eff.compile.fold(0)(_ + _)

  // 위의 모든 결과물의 반환 타입은 IO이다.

  // 실행시키고 싶은면
  // IO 타입에 있는 unsafe* 함수를 호출하면 된다.
  vector.unsafeRunSync()

  drain.unsafeRunSync()

  value.unsafeRunSync()

  // 다양한 run* 함수들은 IO타입에 특화되어 있지 않다.
  // implicit Sync[F]를 가지고 있는 어떤 F[_] 타입에도 동작한다.

  ////////////////////////
  // Segments & Chunks

  // fs stream은 성능을 위해서 내부적으로 segment화 한다.
  // Stream.segment를 이용해서 각각의 stream segment를 만들수 있다.

  //
  val s1c: Stream[Pure, Double] =
    Stream.chunk(Chunk.doubles(Array(1.0, 2.0, 3.0)))

  // 1............100
  // 1, 2, 3
  // [      ] => GET ids=1,2,3

  val err2: Stream[Pure, Int] = Stream(1, 2, 3) ++ (throw new Exception("!@#$"))
}
