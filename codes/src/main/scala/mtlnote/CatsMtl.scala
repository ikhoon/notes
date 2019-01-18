package mtlnote
import scalaz.MonadReader

/**
 * Created by ikhoon on 2018-12-15.
 */

object CatsMtl {

  /**
    * study cats mtl based on https://typelevel.org/blog/2018/10/06/intro-to-mtl.html
    */

  // mtl이란?
  // mtl는 monad transformer의 약어이다.
  // 이것의 주된 목적은 중복된 monad transformer를 쉽게 사용하게 하는것이다.
  // 대부분의 공통 monad transformer에 대해서 type class로 encoding하는것이 가능하다.
  // 이것을 이해하기 위해서는 우리는 먼저 몇가지 공통의 monad transformer를 살펴본다.

  // ReaderT
  // ReaderT는 environment로 부터 읽들이게 하고 environment에 의존되어 있는 다른 값을 생성하게 한다.
  // 이것은 외부로 부터 설정값을 읽어들일때 유용하다.
  // 일부는 이것을 함수형 프로그래밍의 의존성 주입이라고 설명한다.

  // 서비스를 호출하는것을 상상해보자.
  // 우리는 설정을넘겨주어야 한다.

  import cats._
  import cats.data._
  import cats.implicits._
  import cats.effect._

  type Config = String
  type Result = String

  def getConfig: IO[Config] = ???

  def serviceCall(c: Config): IO[Result] = ???

  // 이방법은 configuration을 어플리케이션의 top레벨부터 전달하는 방식으로 가장 쉬운 받식앋.
  // 하지만 꽤 지저분한 방식이다.
  // ReaderT를 사용하면 어떻게 되나?, ReaderT는 우리에게 ask 함수를 준다. 이함수는 E에 대한 읽기전용 환경값을 준다.
  def ask[F[_]: Applicative, E]: ReaderT[F, E, E] = ???

  def readerProgram : ReaderT[IO, Config, Result] = for {
    config <- ReaderT.ask[IO, Config]
    result <- ReaderT.liftF(serviceCall(config))
  } yield config

  def main2: IO[Result] = getConfig.flatMap(readerProgram.run)

  // StateT
  // ReaderT와 같이 StateT는 environment로 부터 읽을수 있게 해준다.
  // 그러나 ReaderT와 다른게 enviroment에 write도 가능하다.
  // 이름에 표현되듯이 상태를 가질수 있는 기능이 있다.
  // 외부에 접근하고 mutable한 상태를 관리하는 프로그램을 만든다.
  // 이것은 매우 강력하지만 보호없이 사용이 되면 절차지향프로그맹에서 발생하는 유사한 현상들이 발생할수 있다.
  // 그러나 StateT의 보호하며 사용되면 mutable상태가 필요한곳에 아주 좋은 툴이 될것이다.

  type Env = String
  type Request = String
  type Response = String

  def initializeEnv: Env = ???

  def request(r: Request, env: Env): IO[Response] = ???

  def updateEnv(r: Response, env: Env): Env = ???

  def req1: Request = ???
  def req2: Request = ???
  def req3: Request = ???
  def req4: Request = ???

  def requestWithState(r: Request): StateT[IO, Env, Response] = for {
    env <- StateT.get[IO, Env]
    res <- StateT.liftF(request(r, env))
    _ <- StateT.modify[IO, Response](updateEnv(res, _))
  } yield res


  def stateProgram: StateT[IO, Env, Response] = for {
    resp1 <- requestWithState(req1)
    resp2 <- requestWithState(req2)
    resp3 <- requestWithState(req3)
    resp4 <- requestWithState(req4)
  } yield resp4


  def main3: IO[(Env, Response)] = stateProgram.run(initializeEnv)



  // Monad Transformers encode som notion of effect
  // EitherT는 short-circuiting 에러를 effect를 인코드한다.
  // ReaderT는 environment로부터 값을 읽어오는 effect를 인코드
  // StateT는 순수한 로컬 가변 상태에 대한 effect를 인코드

  // 이런 monad transformer는 effect를 data structure로 encode한다.
  // 같은 결과를 typeclass로도 얻을수 있다.

  // 이때까지 봐왔던 ReaderT.ask의 함수는 무엇으로 대신할수 있나?
  // cats-mtl의 답은 ApplicativeAsk 이다.
  // ReaderT를 typeclass로 encode했다고 생각하면 된다.

  trait ApplicativeAsk0[F[_], E] {
    val applicative: Applicative[F]
    def ask: F[E]
  }

  // Effect type classes
  // cats-mtl은 대부분의 공통의 effect에 대해서 typeclass를 제공해준다.
  // 그리고 특정 모나드 트랜스포터 스택의 수행과 상관없이 effect를 선택할수 있게 해준다.

  // 이상적으로 당신은 abstract type constructor F[_]의
  // 다른 typeclass constraint 만 사용해서 코딩을 해야 한다.
  // 그리고 마지막에 이들의 제약을 수행할수 있는 데이터 타입과 함께 실행하면 된다.
  import cats.mtl._
  import cats.mtl.implicits._

  def readerProgram2[F[_]: Monad: LiftIO](
    implicit A: ApplicativeAsk[F, Config]
  )
  : F[Result] = for {
    config <- A.ask
    res <- serviceCall(config).to[F]
  } yield res

  type ApplicativeConfig[F[_]] = ApplicativeAsk[F, Config]

  val materializedProgram = readerProgram2[ReaderT[IO, Config, ?]]
  def main4: IO[Result] = getConfig.flatMap(materializedProgram.run)

  // 괜찮아 보이긴 하는데 더 좋아진건 안보인다.
  // MTL은 하나 이상의 monad transformer를 사용해야 할때 빛이 나낟.
  // 우리의 프로그램은 error handling이 필요하다.(필요함)

  // 이것을 위해 우리는 MonadError를 사용할것이다. 이것은 mtl말고 cats-core에서 확인할수 있다.
  // 이것은 short circuiting effect에 대한 encode이고 EitherT와 공유한다.

  def validConfig(c: Config): Boolean = ???

  sealed trait AppError
  case object InvalidConfig extends AppError

  // MonadAppError를 만들어서 program에 대한 제약으로 사용하자
  type MonadAppError[F[_]] = MonadError[F, AppError]
  def program[F[_]: MonadError: ApplicativeConfig, LiftIO]: F[Result] = ???
  //

  def readerProgram3[F[_]: MonadAppError: ApplicativeConfig: LiftIO]: F[Result] = for {
    config <- ApplicativeAsk[F, Config]
        .ask.ensure(InvalidConfig)(validConfig)
    res <- serviceCall(config).to[F]
  } yield res

  // 꽤 심플하다.(글쎄다) 사실 여러개중첩하는것 보다야
  // 우리는 ReaderT, EitherT, IO의 모나드 stack을 사용할것이다.
  // unwarp하면 IO[EitherT[AppError, Reader[Config, A]]]

  // 보기좋게 Alias좀 만들어 보자
  type EitherApp[A] = EitherT[IO, AppError, A]
  type Stack[A] = ReaderT[EitherApp, Config, A]

  val materializedProgram2: Stack[Result] = readerProgram3[Stack]

  def main5: IO[Either[AppError, Result]] =
    EitherT
      .liftF(getConfig)
      .flatMap(materializedProgram2.run)
      .value

  // Adding State
  // 다음 스텝으로 각각의 request이후 여러개의 request를 보내고 싶다.
  // response을 다음 request에 활용하고 싶다.
  // StateT 예제와 유사하다.

  // StateT대신 MonadState typeclass를 사용할것이다.
  trait MonadState0[F[_], S] {
    val monad: Monad[F]

    def get: F[S]
    def set(s: S): F[Unit]
    def modify(f: S => S): F[Unit] = monad.flatMap(get)(s => set(f(s)))
  }

  // list of request를 가지고 있다.
  // 각각의 request후에는 environment를 업데이트 하는것이다.
  // 그리고 그 environment를 다음 request에 활용하고
  // 마지막으로 모든 response들을 반환한다.

  type Results = List[Response]

  def requests: List[Request] = ???

  def newServiceCall(c: Config, req: Request, e: Env): IO[Response] = ???

  // 우리는 이제 MonadState를 활용하여 새로운 함수를 만들것이다.
  // environment를 updateEnv를 활용해서 수정하는것을 추가한 것을 newSerivceCall과 함께 wrapping한다.

  type MonadStateEnv[F[_]] = MonadState[F, Env]

  def requestWithState[F[_]: MonadStateEnv: Monad: LiftIO](c: Config, req: Request): F[Response] = for {
    env <- MonadState[F, Env].get
    res <- newServiceCall(c, req, env).to[F]
    _ <- MonadState.modify[F, Env](s => updateEnv(res, s))
  } yield res

  def stateProgram1[F[_]: ApplicativeConfig: MonadAppError: MonadStateEnv: LiftIO]: F[Results] = for {
    config <- ApplicativeAsk[F, Config]
        .ask
        .ensure(InvalidConfig)(validConfig)
    responses <- requests
        .traverse(requestWithState[F](config, _))
  } yield responses


  val materializedStateProgram1 = stateProgram1[StateT[EitherT[ReaderT[IO, Config, ?], AppError, ?], Env, ?]]

  def main6: IO[Either[AppError, (Env, Results)]] =
    getConfig.flatMap { conf =>
      materializedStateProgram1
        .run(initializeEnv)
        .value
        .run(conf)
    }




}
