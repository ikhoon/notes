package catsnote

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import scala.util.Try

/**
  * Created by ikhoon on 13/12/2016.
  */
class MonadProofSpec extends AnyWordSpec with Matchers {

  "Monad as a Monoid in Category of Endofunctors" should {
    // 원문 - http://w.pitula.me/2016/monad-proof/

    trait Monoid[A] {
      def zero: A
      def append(a1: A, a2: A): A
    }

    // 이건 쉽다.
    object IntAddMonoid extends Monoid[Int] {
      override def zero: Int = 0
      override def append(a1: Int, a2: Int): Int = a1 + a2
    }

    // The category
    // MonoidalCategory

    trait MonoidalCategory {
      type Morphism[F, G]
      type MonoidalProduct[F, G]
      type IdentifyObject
    }

    // 깊게 들어가지 말자. 어려우니.
    // 하지만 CategoryOfSets 를 정의하면 좀더 명확해진다.

    trait CategoryOfSets extends MonoidalCategory {
      type Morphism[F, G] = F => G // 그냥 함수
      type MonoidalProduct[F, G] = (F, G) //  두개의 요소를 합침
      type IdentifyObject = Unit // (F, Unit) = F, 그러니께 Unit 이 항등원이 된다.
    }

    // category랑 category of set을 이용해서 monoid를 정의해보자.

    trait MonoidInCategory[A] {
      type Category <: MonoidalCategory
      def zero: Category#Morphism[Category#IdentifyObject, A]
      def combine: Category#Morphism[Category#MonoidalProduct[A, A], A]
    }

    trait MonoidInCategorySets[A] extends MonoidInCategory[A] {
      type Category <: CategoryOfSets
      def zero: Unit => A
      def combine: ((A, A)) => A
    }

    // MonoidInCategorySets은 처음에 정의했던 Monoid와 유사하다.

    object IntAddMonoidInCategorySets extends MonoidInCategorySets[Int] {
      def zero: (Unit) => Int = (_: Unit) => 0
      def combine: ((Int, Int)) => Int = (t: (Int, Int)) => t._1 + t._2
    }
    // 그렇게 이상하진 않다.

    // Functor에 대해 이야기 해보자.

    // EndofunctorInCategoryOfSets를 줄여서 여기서는 그냥 Functor라고 하자

    trait Functor[F[_]] {
      def fmap[A, B](f: A => B): F[A] => F[B]

      // map[A, B](f: A => B)(fa: F[A]): F[B]
      // 와 거의 같은 모양이다.
      // A => B의 함수를 받아서 F로 lift한다.
    }

    // The category of endofunctors
    // CategoryOfEndofunctors를 만들기 위해서는 몇가지 스텝이 필요하다.

    // > 스칼라는 kind에 대한 다형성이 지원되지 않는다.
    // > 뭔말인가 하면 Functor[F[_]]라 정의하면 F에 Option은 들어갈수 있지만 Int는 들어갈수 없다.
    // > 또한 Monoid[A]라 정의하면 A에 Int는 들어갈수 있지만 Option은 들어갈수 없다.

    trait Product[F[_], G[_]] {
      type Out[T]
    }

    trait MonoidalCategoryK2 {
      type Morphism[F[_], G[_]]
      type MonoidalProduct[F[_], G[_]] <: Product[F, G]
      type IdentifyObject[T]
    }

    // MonoidalCategory의 kind를 `*` 에서 `* -> *`로 변경했다.

    // 하지만 Product때문에
    // `* -> * -> *`를 `(* -> *) -> (* -> *) -> (* -> *)`
    // 로 변경하는게 필요하다.
    case class Id[A](value: A)

    trait NaturalTransformation[-F[_], +G[_]] { self =>
      def apply[A](fa: F[A]): G[A]
    }

    type ~~>[-F[_], +G[_]] = NaturalTransformation[F, G]

    trait Compose[F[_], G[_]] extends Product[F, G] {
      type Out[T] = F[G[T]]
    }

    // 간단한 예는
    object TryToOption extends NaturalTransformation[Try, Option] {
      def apply[A](t: Try[A]): Option[A] = t.toOption
    }
    def example[A](a: A): Compose[Option, Try]#Out[A] = {
      val result: Option[Try[A]] = Option(Try(a))
      result
    }

    // 이제 CategoryOfEndofuctors를 만들수 있다.

    trait CategoryOfEndofunctors extends MonoidalCategoryK2 {
      type Morphism[F[_], G[_]] = NaturalTransformation[F, G]
      type MonoidalProduct[F[_], G[_]] = Compose[F, G]
      type IdentifyObject[A] = Id[A]
    }

  }
}
