package catsnote


/**
  * Created by Liam.M on 2018. 03. 16..
  */


// 제한적이다.
//
trait KVStore[F[_], V] {
  def get(key: String): F[V]
  def put(key: String, value: V): F[Unit]
}

import cats.{Applicative, FlatMap, Monad, Parallel}
import cats.implicits._
object KVStore {

  def newF[F[_]: Applicative, A](a: A): F[A] = {
    Applicative[F].pure(a)
  }

  def program[F[_]: FlatMap](kv: KVStore[F, Int]): F[Int] = {
    for {
      _ <- kv.put("a", 10)
      _ <- kv.put("b", 11)
      a <- kv.get("c")
    } yield a
  }









  def program2[M[_]: Monad, F[_]: Applicative]
    (kv: KVStore[M, Int])(implicit P: Parallel[M]): M[Int] = {
    (kv.put("a", 10), kv.put("b", 11), kv.get("c")).parMapN((_, _, c) => c)
  }

  def program3[M[_]: Monad]
  (kv: KVStore[M, Int])(implicit P: Parallel[M]): M[Int] = {
    (kv.put("a", 10), kv.put("b", 11), kv.get("c")).parMapN((_, _, c) => c)
  }
}
