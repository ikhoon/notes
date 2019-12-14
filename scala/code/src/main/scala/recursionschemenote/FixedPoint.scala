package recursionschemenote

import cats.Functor

/**
 * Original blog: https://japgolly.blogspot.com/2017/11/practical-awesome-recursion-ch-01.html
 * Let's try writing code by myself
 */

// IntList를 구현해보자.

// Before
sealed trait IntList

final case class IntCons(head: Int, tail: IntList) extends IntList

case object IntNil extends IntList

// `IntCons#tail` 에서 자기 자신을 참조한다. 이부분을 없애보자.
// 자기 참조하는것이 없어야 한다.

// After (v1)
sealed trait IntListV1[F]

final case class IntConsV1[F](head: Int, tail: F) extends IntListV1[F]

final case class IntNilV1[F]() extends IntListV1[F]

// IntNilV1은 case class인게 구리다 바꾸자.
sealed trait IntListV2[+F]

final case class IntConsV2[+F](head: Int, tail: F) extends IntListV2[F]

case object IntNilV2 extends IntListV2[Nothing]


// BinaryTree도 한번 구현해보자.

// Before
sealed trait BinaryTree[+A]

final case class Node[A](left: BinaryTree[A], value: A, right: BinaryTree[A]) extends BinaryTree[A]

case object Leaf extends BinaryTree[Nothing]

// After
sealed trait BinaryTreeV1[+A, +F]

final case class NodeV1[+A, +F](left: F, value: A, right: F) extends BinaryTreeV1[A, F]

case object LeafV1 extends BinaryTreeV1[Nothing, Nothing]


// Json도 재귀구조이다.

// Before
sealed trait Json

object Json {

  case object Null extends Json

  case class Str(value: String) extends Json

  case class Bool(value: Boolean) extends Json

  case class Num(value: Double) extends Json

  case class Arr(values: List[Json]) extends Json

  case class Obj(fields: List[(String, Json)]) extends Json

}

// After
sealed trait JsonV1[+F]

object JsonV1 {

  case object Null extends JsonV1[Nothing]

  final case class Str(value: String) extends JsonV1[Nothing]

  final case class Bool(value: Boolean) extends JsonV1[Nothing]

  final case class Num(value: Double) extends JsonV1[Nothing]

  final case class Arr[+F](values: List[F]) extends JsonV1[F]

  final case class Obj[+F](fields: List[(String, F)]) extends JsonV1[F]

}


// Step 2: Make functor

// IntList
object functors {

  implicit object IntListFunctorV2 extends Functor[IntListV2] {
    override def map[A, B](fa: IntListV2[A])(f: A => B): IntListV2[B] = {
      fa match {
        case IntConsV2(head, tail) =>
          IntConsV2(head, f(tail))
        case IntNilV2 => IntNilV2
      }
    }
  }

  implicit def binaryTreeV2Functor[E]: Functor[BinaryTreeV1[E, *]] = new Functor[BinaryTreeV1[E, *]] {
    override def map[A, B](fa: BinaryTreeV1[E, A])(f: A => B): BinaryTreeV1[E, B] = {
      fa match {
        case NodeV1(left, value, right) => NodeV1(f(left), value, f(right))
        case LeafV1 => LeafV1
      }
    }
  }

  implicit object JsonFunctor extends Functor[JsonV1] {
    override def map[A, B](fa: JsonV1[A])(f: A => B): JsonV1[B] = {
      fa match {
        case JsonV1.Null => JsonV1.Null
        case s: JsonV1.Str => s
        case b: JsonV1.Bool => b
        case n: JsonV1.Num => n
        case JsonV1.Arr(values) => JsonV1.Arr(values.map(f))
        case JsonV1.Obj(fields) => JsonV1.Obj(fields.map({ case (field, v) =>
          field -> f(v)
        }))
      }
    }
  }

}



