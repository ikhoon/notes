package fpmortals

import monoclenote.PrismExample.Json
import simulacrum.typeclass

/**
  * Created by Liam.M on 2018. 03. 07..
  */

// exponential functor라 햠
// 뭔말이래
// http://comonad.com/reader/2008/rotten-bananas/
// F[A] => F[B] 로 바꾸기 위해서 A => B와 B => A가 다 있어야 하는 경우
// 그걸 exponential functor라 한다함
/*
@typeclass trait InvariantFunctor[F[_]] {
  def xmap[A, B](fa: F[A], f: A => B, g: B => A): F[B]
}




// xmap은 map이나
@typeclass trait Functor[F[_]] extends InvariantFunctor[F] {
  def map[A, B](fa: F[A])(f: A => B): F[B]
  def xmap[A, B](fa: F[A], f: A => B, g: B => A): F[B] = map(fa)(f)
}

// xmap은 contramap을 이용해서 구현한다.
@typeclass trait Contravariant[F[_]] extends InvariantFunctor[F] {
  def contramap[A, B](fa: F[A])(f: B => A): F[B]
  def xmap[A, B](fa: F[A], f: A => B, g: B => A): F[B] = contramap(fa)(g)
}


// encode `A` => Json
// decode Json => `A`

// 각각의 typeclass의 특징에 맞추어서


// 어떤 애들이 ContraVariant의 특성을 가지고 있나?
// 지난번에 카레가 말했던 Encoder가 contra variant의 특성을 가지고 있다.
// A => Json  ====> Encoder[A]
// B => A
// B => A => Json ====> B => Json => Encoder[B]

// Encoder[A] =>  contramap(B => A)   => Encoder[B] (B => A => Json)

@typeclass trait Encoder[A] { self =>
  def encodeJson(a: A): Json
  def contramap[B](f: B => A): Encoder[B] = new Encoder[B] {
    def encodeJson(b: B): Json = self(f(b))
  }
}

// Decoder는 Functor의 특성을 가지고 있다.
@typeclass trait Decoder[A] { self =>
  def decodeJson(j: Json): Decoder.Result[A]
  def map[B](f: A => B): Decoder[B] = new Decoder[B] {
    def decodeJson(j: Json): Decoder.Result[B] = self.decodeJson(j).map(f)
  }
}

object Decoder {
  type Result[A] = Either[String, A]
}


// Format은 두개의 특성이 다 있어야 하기 때문에
// Invari
@typeclass trait Format[A] extends Encoder[A] with Decoder[A] { self =>
  def xmap[B](f: A => B, g: B => A): Format[B] = new Format[B] {
    def encodeJson(b: B): Json = self(g(b))
    def decodeJson(j: Json): Decoder.Result[B] = self.decodeJson(j).map(f)
  }
}


*/
