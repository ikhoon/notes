package fpinsnote

import scalaz.Monoid


/**
  * Created by liam on 27/01/2017.
  */
object comonad {

  // 원문 : http://blog.higher-order.com/blog/2015/06/23/a-scala-comonad-tutorial/
  // Monad 복습하기
  // 1. Monad는 Functor이다, 즉 map함수를 가지고 있다.
  trait Functor[F[_]] {
    def map[A, B](x: F[A])(f: A => B): F[B]
  }
  // map(x)(a => a) == x 를 만족해야 한다.

  // Monad는 2개의 추가적인 polymorphic 함수를 가지고 있는 functor이다.

  trait Monad[M[_]] extends Functor[M] {
    def unit[A](a: A): M[A]
    def join[A](mma: M[M[A]]): M[A]
  }

  // 모든 monad는 `proper morphism`이라 불리는 위의 함수를 가지고 있다.
  // 또한 `nonproper morphism`이라 불리는 특정 monad에 한정된 연산이 있다.


  // Reader monad
  case class Reader[R, A](run: R => A) {
    // Reader monad는 값에 대해서 묻는(ask)것이 있다.
    def ask: Reader[R, R] = Reader(r => r)

    // Reader monad에서 join은 type `R`의 같은 context를 inner scope와 outer scope에 전달하는데 있다.
    def join(r: Reader[R, Reader[R, A]]): Reader[R, A] =
      Reader(c => r.run(c).run(c))
  }


  // Writer monad
  case class Writer[W, A](value: A, log: W) {
    // Writer monad는 하나의 값을 옆에 저장하는 것이 가능하다.
    def tell(w: W): Writer[W, Unit] = Writer((), w)

    // Writer monad에서 join의 의미는 W에 대한 Monoid를 이용하여 log를 합치는(concatenate)것이다.
    def join(w: Writer[W, Writer[W, A]])(implicit M: Monoid[W]): Writer[W, A] =
      Writer(w.value.value, M.append(w.log, w.value.log))

    // unit의 의미는 빈 로그를 생성하는것이다.
    def unit(a: A)(implicit M: Monoid[W]) =
      Writer(a, M.zero)

  }


  // State monad
  case class State[S, A](run: S => (A, S)) {
    // State monad의 경우에는 상태를 저장하는것과 가저오는것이 모두 있다.
    def get: State[S, S] = State(s => (s, s))
    def set(s: S): State[S, Unit] = State(_ => ((), s))

    // State monad에서의 join은 외부 행위(outer action)에 대해서상태를 가져오고 그상태를 저장하는 기회를 갖는다.
    // 그리고 내부 행위(inner action)에 대해서 같은것을 반복한다.
    // 이것은 하위 연속된 행위들이 그전 것들에 의해서 바뀐것을 볼수 있게 한다.
    def join(v1: State[S, State[S, A]]): State[S, A] =
      State(s1 => {
        val (v2, s2) = v1.run(s1)
        v2.run(s2)
      })

  }


  // Option monad
  // Option monad는 정답없이 중지할수 있다.
  def none[A]: Option[A] = None

  // monad에 대한 충분한 설명 이다. 이제 comonad의 차례이다.



  // Comonad
  // Comonad는 monad와 같은 것이지만, 단지 반대 방향이다.
  trait Comonad[W[_]] extends Functor[W] {
    // counit은 W[A]에서 A값을 끄집어 내는것을 허락하기 때문에 종종 `extract`라 불린다.
    def counit[A](w: W[A]): A

    // join은 2단계의 레벨의 monad를 1개로 만들지만 duplicate는 1단계의 comonad를 2단계로 만든다.
    def duplicate[A](w: W[A]): W[W[A]]
  }


  // Identity comonad
  // 단순하고 명확한 comonad는 dumb wrapper이다.(identity comonad)
  case class Id[A](a: A) {
    def map[B](f: A => B): Id[B] = Id(f(a))
    def counit: A = a
    def duplicate: Id[Id[A]] = Id(this)
  }


  // Reader comonad
  // Reader comonad는 reader monad와 같은 능력을 가지고 있다.
  // 값에 대해서 묻는것을 허락한다.
  case class Coreader[R, A](extract: A, ask: R) {
    def map[B](f: A => B): Coreader[R, B] = Coreader(f(extract), ask)
    def duplicate: Coreader[R, Coreader[R, A]] = Coreader(this, ask)
    def extend[B](f: Coreader[R, A] => B): Coreader[R, B] = duplicate map f
  }

  def coreaderComonad[R]: Comonad[Coreader[R, ?]] = new Comonad[Coreader[R, ?]] {
    def map[A, B](x: Coreader[R, A])(f: A => B): Coreader[R, B] = x map f
    def counit[A](w: Coreader[R, A]): A = w extract
    def duplicate[A](w: Coreader[R, A]): Coreader[R, Coreader[R, A]] = w duplicate
  }

  // 논쟁이 있을수 있겠지만, coreader는 scala에서 reader monad보다 훨씬더 이해하기 쉽다.
  // reader monad에서는 ask 함수는 identity 함수이다.
  // 이것은 "R의 값이 가능하면 그것을 돌려줘" 말하는 것이다.
  // 그리고 그것을 map 과 flatMap의 하위 연산에 사용할수 있도록 한다.
  // (이것은 Reader의 run이 R => A 함수로 되어 있어서 이렇게 말하는것 같다)
  // 하지만 Coreader에서는 R이 있는척 할필요가 없다. 바로 거기 있고 우리는 볼수 있다.
  // (이건 Coreader(extract: A, ask: R)에서 ask에 바로 접근이 가능해서 그런거 같다)

  // Coreader는 단지 어떤값 A와 추가적인 context R을 함께 wrap 한것이다.
  // 이것이 comonad라는것이 왜 중요한가? 여기에서 duplicate의 의미는 무엇인가?

  // duplicate의 의미를 알아보기 위해, 우리는 Coreader 전체를 value slot(extract의 위치)에 놓인다는것은 알아야 한다.
  // 즉 `def duplicate: Coreader[R, Coreader[R, A]] = Coreader(this, ask)` 여기에서 extract 위치에 this가 옴
  // 그렇기 때문에 어떤 extract나 map의 하위 연산이라도 value 타입 A와 context 타입 R을 모두 관찰할수 있게 된다.
  // 우리는 이것을 Reader monad가 하는것과 같이 하위 연산으로 context를 전달하는것 으로 생각할수 있다.

  // 실제로 map에 join이 붙어서 flatMap을 표현하듯이, 같은 말로 duplicate에 map이 붙어서 일반적으로 `extend`라는 하나의 함수를 만든다.
  // def extend[B](f: Coreader[R, A] => B): Coreader[R, B] = duplicate map f
  // extend의 type signature는 f의 방향이 뒤집어진 flatMap처럼 보인다.
  // 그리고 우리는 flatMap으로 연결했던것 처럼 comonad는 extend를 이용하여 연결할수 있다.
  // Coreader에서는 f는 context의 타입 R을 사용이 가능하고 B를 만들어 낸다.


  // Monad의 flatMap을 이용한 연결하는 연산을 Kleisli composition이라 부르기도 한다.
  // 그리고 Comonad의 extend를 이용한 연결하는 연산을 coKleisli composition이라 부른다.
  // 혹은 Kelisli composition in comonad

  // `extend` 이름은 어떤 구조에 어떤 구조에 작용하는 "local" 연산을 가지고
  // 그것을 큰 구조의 모든 하위 구조에 동작하는 "global" 연산으로 "extend" 하는것을 표현한다.


  // Writer comonad
  // Wirter monad와 같이, writer comonad는log를 추가하거나 monoid를 이용하여 계산을 수행할수 있다.
  // 하지만 log를 항상 사용가능하게 간직하고 있는것이 아니라 추가할수 있게 한다.
  // 이것은 연산을 합성하고 실행을 했을때 로그가 이용가능한것과 reader monad에서 사용했던것과 같은 트릭이다.

  case class Cowriter[W: Monoid, A](tell: W => A) {
    def map[B](f: A => B): Cowriter[W, B] = Cowriter(tell andThen f)
    def extract: A = tell(Monoid[W].zero)
    def duplicate: Cowriter[W, Cowriter[W, A]] =
      Cowriter(w1 => Cowriter(w2 => tell(Monoid[W].append(w1, w2))))
    def extend[B](f: Cowriter[W, A] => B): Cowriter[W, B] =
      duplicate map f
  }

  // duplicate는 run 함수로 생성된 전체 Cowriter를 반환한다는것을 주의해 보면,
  // 이것의 의미는 하위 연산(map이나 extend로 으로 합성된)의 기존의 로그에 추가하거나 합사하는 `tell`함수의 하나의 의해서만 접근된다.
  // 예를 들어 `foo.extend(_.tell("hi"))는 "hi"를 foo의 로그에 추가한다.


  // Comonad laws
  // Comonad의 규칙은 monad의 규칙과 유사하다.
  // 1. left identity : wa.duplicate.extract = wa
  // 2. right identity : wa.extend(extract) = wa
  // 3. associativity: wa.duplicate.duplicate = wa.extend(duplicate)











}
