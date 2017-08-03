package freemonad

object free {
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
  // def ask(prompt: String): M[String] = ???
  // 이런 형태로 바뀌게 될것이다.
  case class Ask(prompt: String) extends Interact[String]

  // 위와 마찬가지로 아래 Tell은
  // def tell(msg: String): M[Unit] 의 형태로 바뀌게 된다.
  case class Tell(msg: String) extends Interact[Unit]

  // 이제 모나드르 정의해보자.
  // interface만 정의하자.
  trait Monad[M[_]] {
    def pure[A](a: A): M[A]

    def flatMap[A, B](ma: M[A])(f: A => M[B]): M[B]
  }

  // 사용하기 쉽게 factory를 만들어보자
  // 아래와 같이 선언하면
  // Monad[Option] 이런식으로 하면 monad instance를 가져올수 있다.
  object Monad {
    def apply[F[_] : Monad]: Monad[F] = implicitly[Monad[F]]
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
    def or[H[_]](f: H ~> G): ({type l[x] = Coproduct[F, H, x]})#l ~> G = {
      new (({type l[x] = Coproduct[F, H, x]})#l ~> G) {
        def apply[A](fa: Coproduct[F, H, A]): G[A] = fa.run match {
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
  // 이를 구현한 Some과 None이 있듯이
  // Free 를 구현한 Return과 Bind가 있다.
  sealed trait Free[F[_], A] {
    def flatMap[B](f: A => Free[F, B]): Free[F, B] =
      this match {
        case Return(a) => f(a)
        case Bind(a, g) => Bind(a, g andThen(_ flatMap f))
      }

    def map[B](f: A => B): Free[F, B] =
      flatMap(a => Return(f(a)))

    // 이걸 보면 Bind에서 값을 하나씩 꺼내서 재귀적으로 실행 시킨다.
    def foldMap[G[_]: Monad](f: F ~> G): G[A] =
      this match {
        case Return(a) => Monad[G].pure(a)
        case Bind(a, g) => Monad[G].flatMap(f(a)) { i =>
          g(i).foldMap(f)
        }
      }
  }

  // Return은 그만 반환하라는 이야기이다. 즉 끝났음을 의미한다.
  // Return안에 있는 a를 F로 감싸서 반환하면 된다.
  case class Return[F[_], A](a: A) extends Free[F, A]
  // Bind는 왜이런 모양인가?
  // 이모양은 흡사 Cons, :: 과 유사하다. 리스트 구조이다.
  // 즉 a에서 값을 뽑아서 f에 대입해서 연산을 하고
  // 그 결과값에 따라서 Bind이면 또 연산을 하고
  // Return이면 연산을 종료한다.
  // a: F[I]에서 어떻게 값을 뽑아 낼수 있는가?
  // Container에서 값을 뽑아내는건 모나드의 주특기이지만 F[I]는 모나드가 아니다.
  // F[I]에서는 바로 값을 뽑아낼수 없다.
  // 그렇지만 natural transformation을 활용하면 가능하다.
  // F ~> G로 보내는 natural tranformation을 이용하면
  // F[I] => G[I]가 되고 이렇게 된 G[I]는 map이나 flatMap을 통해서 안의 값을 끄집어 낼수 있다.
  // 그렇기 때문에 foldMap에서 `[G[_]: Monad]`를 통해서 G에 대한 Monad instance를 요구를 한다.
  // 여기서 I는 왜 I라고 부르는 지는 잘 모르겠다. 뭔가 심볼에 의미가 있을것 같은데
  case class Bind[F[_], I, A](a: F[I], f: I => Free[F, A]) extends Free[F, A]


  // 이제 interpreter를 구현해보자.
  type Id[A] = A

  implicit val identityMonad = new Monad[Id] {
    def pure[A](a: A) = a
    def flatMap[A, B](fa: Id[A])(f: A => B) = f(fa)
  }

  object Console extends (Interact ~> Id) {
    override def apply[A](f: Interact[A]) = {
      f match {
        case Ask(prompt) =>
          println(prompt)
          readLine
        case Tell(msg) =>
          println(msg)
      }
    }
  }

  // interpreter pattern은 테스트를 해보자.
  type Tester[A] = Map[String, String] => (List[String], A)

  object TestConsole extends (Interact ~> Tester) {
    override def apply[A](f: Interact[A]): Tester[A] = {
      f match {
        case Ask(prompt) => m => (List(), m(prompt))
        case Tell(msg) => _ => (List(msg), ())
      }
    }
  }
  // interpreter를 구현하면 intellij 에서는 빨간불이 뜬다.
  // 조금 신경쓰인다.
  // https://github.com/Thangiee/Freasy-Monad
  // 이런 프로젝트가 있긴 하지만 maintainer가 얼마나 support해줄지 의문이긴하다.
  // 아직은 빨간색에 만족하자.
  // 언젠가는 intellij가 해결해줄거라 믿는다 ㅋ
  // 아니면 다른걸로 갈아타는 수도.

  implicit val testerMonad = new Monad[Tester] {
    def pure[A](a: A): Tester[A] = _ => (List(), a)

    def flatMap[A, B](ma: Tester[A])(f: (A) => Tester[B]): Tester[B] = {
      m => {
        val (l1, a1) = ma(m)
        val (l2, a2) = f(a1)(m)
        (l1 ++ l2, a2)
      }
    }
  }

  type UserId = String
  type Password = String
  type Permission = String
  case class User(id: String)

  // 두번째 algebra 이다.
  sealed trait Auth[A]

  case class Login(u: UserId, p: Password) extends Auth[Option[User]]

  case class HasPermission(u: User, p: Permission) extends Auth[Boolean]

  // 두개의 algebra를 합칠때 사용한다.
  case class Coproduct[F[_], G[_], A](run: Either[F[A], G[A]])

  // inject를 정의했다.
  // inj는 F[A]를 넣으면 G[A]를 반환하고
  // prj는 G[A]를 넣으면 Option[F[A]]를 반환한다.
  // 뭔가 natural transformation의 양방향 구현 같은 느낌이 들지만
  // 아직은 잘 모르겠다. 조금더 따라가보자.
  sealed trait Inject[F[_], G[_]] {
    def inj[A](sub: F[A]): G[A]
    def prj[A](sup: G[A]): Option[F[A]]
  }

  // Inject에 대한 글은 아래 글에 잘 나와있다.
  // http://underscore.io/blog/posts/2017/03/29/free-inject.html
  // 아.... 어렵구만
  // 자체 결론 : 구현은 cats에 되어 있으니 갖다 쓰자. 개념은 차차 더 이해하게 되겠지 ㅠ.ㅠ

  // inject는 ADT를 Coproduct로 합쳐놓으면
  // Coproduct[Interact, Auth, Id]
  // 이런식으로 합쳐 놓으면
  // 이 구조에 맞게 ADT를 알아서 Free 모나드로 lift 해준다.
  // 이걸 이용해서 여러개의 ADT를 합성하는 원리하고나 할까?

  object Inject {

    // 이제 instance들을 만들어 보자
    // 아래 나오는 refl은 reflexive의 약어다.
    implicit def injRefl[F[_]]: Inject[F, F] = new Inject[F, F] {
      def inj[A](sub: F[A]): F[A] = sub
      def prj[A](sup: F[A]): Option[F[A]] = Some(sup)
    }

    // 이번엔 왼쪽이다.
    // Coproduct(F[A], G[A]) 이런 모양이 형성된다.
    implicit def injLeft[F[_], G[_]]: Inject[F, ({type L[x]=Coproduct[F, G, x]})#L] =
      new Inject[F, ({type L[x]=Coproduct[F, G, x]})#L] {
        def inj[A](sub: F[A]): Coproduct[F, G, A] =
          Coproduct(Left(sub))

        def prj[A](sup: Coproduct[F, G, A]): Option[F[A]] = sup.run match {
          case Left(fa) => Some(fa)
          case Right(_) => None
        }
      }

    // 이번엔 오른쪽
    // 여기서 G는 Coproduct가 된다.
    // Coproduct(H[A], Coproduct(F[A], G[A]))
    // 대충 이런 모양이 되는것 같다.
    implicit def injRight[F[_], G[_], H[_]](implicit I: Inject[F, G]): Inject[F, ({type L[x]=Coproduct[H, G, x]})#L] =
      new Inject[F, ({type L[x]=Coproduct[H, G, x]})#L] {
        def inj[A](sub: F[A]): Coproduct[H, G, A] =
          Coproduct(Right(I.inj(sub)))

        def prj[A](sup: Coproduct[H, G, A]): Option[F[A]] =
          sup.run match  {
            case Left(_) => None
            case Right(a) => I.prj(a)
          }
    }

    // 아 그래도 여전히 헷갈리는구나
  }

  // lift 함수를 이용해서 ADT를 Coproduct로 승격 시키자
  def lift[F[_], G[_], A](fa: F[A])(implicit I: Inject[F, G]): Free[G, A] = {
    Bind(I.inj(fa), Return(_: A))
  }

  // 이제 smart constructor를 만들어 보자.
  // 어떤 coproduct가 들어오든지 그 coproduct에 맞춘 Free Monad로 만들어준다.
  // 여기서 F가 coproduct이다.
  class Interacts[F[_]](implicit I: Inject[Interact, F]) {
    def tell(msg: String): Free[F, Unit] = lift(Tell(msg))
    def ask(prompt: String): Free[F, String] = lift(Ask(prompt))
  }

  class Auths[F[_]](implicit I: Inject[Auth, F]) {
    def login(u: UserId, p: Password): Free[F, Option[User]] = lift(Login(u, p))
    def hasPermission(u: User, p: Permission): Free[F, Boolean] = lift(HasPermission(u, p))
  }

  // instance를 쉽게 찾을수 있도록 companion object에 만들어 놓는다
  object Interacts {
    implicit def interact[F[_]](implicit I: Inject[Interact, F]): Interacts[F] = new Interacts
  }

  object Auths {
    implicit def auth[F[_]](implicit I: Inject[Auth, F]): Auths[F] = new Auths
  }


  val KnowSecret = "KnowSecret"

  def prg[F[_]](implicit I: Interacts[F], A: Auths[F]): Free[F, Unit] = {
    import A._
    import I._
    for {
      uid <- ask("What's your user ID?")
      pwd <- ask("Password Please.")
      u <- login(uid, pwd)
      b <- u.map(hasPermission(_, KnowSecret)).getOrElse(Return(false))
      _ <- if(b) tell("UUDDLRLRBA") else tell("Go away!")
    } yield ()
  }


  // ADT를 Coproduct를 이용해서 합친다.
  type App[A] = Coproduct[Auth, Interact, A]


  // ADT를 만들어서 넘겨주면 Inject가 implicit resolution을 하면서 Coproduct의 모양에 맞게
  // algebra를 lift해서 program을 만들어 준다.
  // 즉 Free[Coproduct[Auth, Interact, ?], Unit]의 모양으로 lift한다.
  val app = prg[App]

  val TestAuth: Auth ~> Id = new (Auth ~> Id) {
    def apply[A](f: Auth[A]): Id[A] = {
      f match {
        case Login(uid, pwd) =>
          if(uid == "john.show" && pwd == "Ghost")
            Some(User("john.show"))
          else None
        case HasPermission(u, permission) =>
          u.id == "john.show"
      }
    }
  }

  // 앞에 interpreter를 coproduct를 이용해서 합치는 로직은
  // or를 이용해서 구현했었다.
  // 그래서 마지막으로 interpreter를 합하고 이를 주입하면 된다
  def runApp = app.foldMap(TestAuth or Console)

  // 아래 코드는 안된다.
  // 역시나 Coproduct의 순서는 중하다.
  // ADT의 합성 순서는 interpreter의 합성 순서와 동일해야 한다.
  // 물론 컴파일 타임에 에러가 발생하기 때문에 잘못 넣을수 없다.
  // 애초에 컴파일이 되지 않을 것이기 때문이다.
//  def runApp2 = app.foldMap(Console or TestAuth)

  def main(args: Array[String]): Unit = {
    runApp
  }

}
