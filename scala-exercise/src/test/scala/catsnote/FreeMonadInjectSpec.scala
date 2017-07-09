package catsnote

import cats.data.Coproduct
import org.scalatest.{Matchers, WordSpec}

/**
  * Created by ikhoon on 09/07/2017.
  *
  * Composable application architecture with reasonably priced monads
  * https://youtu.be/M258zVn4m2M
  * 여기나오는 발표를 한번 구현해보면서 이해를 해보자.
  * 주된 내용은 free-monad와 그걸 어떻게 합치는냐(compose)이다.
  *
  * 코드는 발표자 Runar Oli Bjarnason의 gist를 참조했다.
  * https://gist.github.com/runarorama/a8fab38e473fafa0921d
  */

//  먼저 ADT, algebra를 완성해보자
// 여기서 A는 결과타입이 담긴다고 생각하면 된다.
sealed trait Interact[A]

// Interact[String]은 Ask 의 반환 타입이라 생각하면 된다.
// def ask(promote: String): M[String] = ???
// 이런 형태로 바뀌게 될것이다.
case class Ask(promote: String) extends Interact[String]

// 위와 마찬가지로 아래 Tell은
// def tell(msg: String): M[Unit] 의 형태로 바뀌게 된다.
case class Tell(msg: String) extends Interact[Unit]

// 이제 모나드르 정의해보자.
// interface만 정의하자.
trait Monad[M[_]] {
  def pure[A]: M[A]
  def flatMap[A, B](ma: M[A])(f: A => M[B]): M[B]
}

// 사용하기 쉽게 factory를 만들어보자
// 아래와 같이 선언하면
// Monad[Option] 이런식으로 하면 monad instance를 가져올수 있다.
object Monad {
  def apply[F[_]: Monad]: Monad[F] = implicitly[Monad[F]]
}

// 이번엔 natural transformation 이다.
// natural transformation 이라 하면
// F[A] => G[A]로 바꾸는 함수, 자료구조형이다.
// 이게 왜 필요할까?
// 위의 ADT, algebra를 생각해보자
// ask 반환 함수는 M[String]이다. 하지민 ADT는 Interact[String]이다.
// 그렇기 때문에 Interact[String] => M[String]으로 바꾸는 함수
// natural transformation이 필요한것이다.
sealed trait ~>[F[_], G[_]] { self =>
  def apply[A](f: F[A]): G[A]

  // 이부분은 합성을 하는게 필요한 부분이다.
  // natural transformation을 상속 받은 애들은 interpreter에 해당한다.
  // 그렇기 때문에 이걸 합성한다는것은 interpreter 여러개를 합친다는 의미이다.

  // 여기에서 f의 시그니쳐를 보자. H ~> G이다. 여러개를 조합하더라도 최종 변환 타입 G는 그래돌 유지된다.
  // F의 algebra에서 G로
  // H의 algebra에서 G로
  // G(특정 effect)로 변환시킨다.
  // 여기서 G는 비동기이면 Task, 동기이면 Id, 그외에 다른 monad가 필요하면 그 effect가 될것이다.
  // 그리고 반환타입을 눈여겨 봐야한다.
  // `F ~> G` 와 `H ~> G`를 합친 `F or H ~> G`가 되었다.
  // type lambda가 들어 있어서 조금 복잡해 보이지만 의미는 `F or H`이다.
  // kind projector style로 하면 Coproduct[F, H, ?] 이다.

  // 합쳐진 natural transformation은
  // Either에 대한 pattern matching을 통한 함수의 실행이 될수있도록
  // 기존에 natural transformation 의 함수를 다시 감싼다.
  def or[H[_]](f: H ~> G): ({type l[x]=Coproduct[F, H, x]})#l ~> G = {
    new (({type l[x]=Coproduct[F, H, x]})#l ~> G) {
      override def apply[A](fa: Coproduct[F, H, A]): G[A] = fa.run {
        case Left(l) => self(l)
        case Right(r) => f(r)
      }
    }
  }
}


// 이번엔 free monad를 만들어 보자
// free monad 안에는 3가지의 연산자가 있다.
// 잘보면 알겠지만 free momad는 type class가 아니다.
// Option 같은 data type이다.
// Option 내부에 flatMap, map 연산자가 있고
// 이를 구현한 Some 과 None이 있듯이
// Free 를 구현한 Return과
sealed trait Free[F[_], A] {
  def flatMap[B](f: A => Free[F, B]): Free[F, B] = ???
  def map[B](f: A => B): Free[F, B] = ???
  def foldMap[G[_]: Monad](f: F ~> G): G[A] = ???
}



class FreeMonadInjectSpec extends WordSpec with Matchers {

}
