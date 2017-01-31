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



  // scala comonad tutorial, part 2

  // Nonempty structures
  // comonad는 반드시 counit을 가지고 있었야한다. 이것은 반드시 "pointed"이거나 nonempty가 경우에 맞다.
  // 주어진 어떤 comonad `W`의 타입 `W[A]`의 값에 대하여, 반드시 타입 `A`의 값을 얻을수 있어야 한다.

  // identity comonad가 이것의 간단한 예이다. 우리는 항상 Id[A]로 부터 A 타입의 값을 얻을수 있다.
  // 약간 더 재미있는 예가 non-empty 리스트 이다.

  case class NEL[A](head: A, tail: Option[NEL[A]]) {
    def map[B](f: A => B): NEL[B] = NEL(f(head), tail.map(_.map(f)))
    def tails: NEL[NEL[A]] =
      NEL(this, tail.map(_.tails))
    def extend[B](f: NEL[A] => B): NEL[B] =
      tails map f
  }
  // nonempty 리스트는 type A의 값과 다른 리스트나 None으로 마크되어 있는 리스트의 종료를 포함하고 있다.
  // 전통적인 `List`와는 달리 head값을 얻는것이 언제나 안전하다.

  // 하지만 `comonadic`한 duplicate 연산은 여기서 무언인가?
  // 그것은 comonad의 연산을 준수하여 NEL[A]을 NEL[NEL[A]으로 가게 하는것을 허락한다.
  // nonempty list에 대해서, 하나의 구연히 이들의 규칙을 만족하는것으로 나온다
  // `def tails: NEL[NEL[A]] = NEL(this, tail.map(_.tails))`

  // tails 연산은 주어진 리스트의 부분 모든 suffix를 반환한다.
  // [1, 2, 3]의 tails연산은 [[1,2,3], [2,3], [3]]이 된다.

  // 이것이 comonadic 프로그램의 context에서 아이디어를 얻기 위해서,
  // coKleisli의 합성 측면, 혹은 comonad의 `extend`에서 생각해보면
  // `def extend[B](f: NEL[A] => B): NEL[B] = tails map f`

  // tails에 대해서 map을 행할때, 함수 f는 모든 리스트의 suffix를 받게 된다.
  // 우리는 각각의 suffix에 대해서 f를 적용하고 그 결과를 리스트(nonempty)에 모은다.
  // 그래서 [1, 2, 3].extend(f)는 [f([1, 2, 3]), f([2, 3]), f([3])]가 된다.

  // extend의 이름은 "local" 연산을 받고(여기에서의 연산은 리스트에 대한 것이다)
  // "global" 연산으로 확장된다.(여기서는 리스트의 모든 suffix가 여기에 해당한다)


  // 또는 nonempty tree를 고려해보라 (Rose Trees로 불린기도 한다.)
  case class Tree[A](tip: A, sub: List[Tree[A]]) {
    def duplicate: Tree[Tree[A]] =
      Tree(this, sub.map(_.duplicate))
  }
  // tip에 A타입의 값을 가지고 있는 트리이다. 그리고 바로 아래 sub 트리를 가지고 있다(이것은 empty가 될수 도 있다.)
  // 하나의 명확한 use case는 디렉토리 구조와 같은 것이다.
  // tip이 하나의 디렉토리이고 sub은 그에 해당하는 하위 디렉토리이다.

  // 이것 또한 comonad이다. `counit`이 명확하다. 단지 `tip`만 get하면 된다. 그리고 duplicate의 구조이다.
  // `def duplicate: Tree[Tree[A]] = Tree(this, sub.map(_.duplicate))`

  // 이것은 명확히 우리에게 tree of tree를 준다. 하지만 이 트리는 무슨 구조인가?
  // 이것은 모든 하위 트리의 트리일것이다.
  // `tip`은 `this` 트리가 되고
  // 각각의 하위 트리의 `tip`은 원래 트리의 전체 subtree에 상응한다.

  // 그것은 `t.duplicate.map(f)`(혹은 동일하게 `t extend f`)를 말할때
  // 우리의 f는 `t`의 하위 트리를 차례로 받고 하위 트리 전체에 대해서 특정 연산을 수행하게 된다.
  // `t extend f` 전체 표현식의 결과가
  // 각각의 노드가 상응하는 하위 트리 `t`에 적용된 f를 포함하는 노드를 제외하고는 (뭔말이레??)
  // `t`의 구조를 미러링한 트리의 구조일것이다.


  // 디렉토리 예제에 관철하기 위해서,
  // 우리는 디렉토리 구조가 사용하는 자세한 공간의 용량
  // tip의 전체 트리 사이즈와 각각의 하위 트리의 tip들의 사이즈를 등등
  // 을 원하는건 상상할수 있다.

  // 그럴땐 `d extend size`는 d의 하위 디렉토리의 사이즈 트리를 재귀적으로 만들수 있다.


  // The cofree monad
  // TODO
















}
