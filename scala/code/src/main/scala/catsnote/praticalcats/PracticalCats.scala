package catsnote.praticalcats

import cats.data.OptionT
import cats.{Monoid, SemigroupK}

import scala.concurrent.Future

/**
  * Created by ikhoon on 17/03/2018.
  */
object PracticalCats {

  // scala night 발표 준비
  // cats best practice? cats가 어떻게 프로그래밍할때 도움을 줄수 있는지 알아보자.
  // 우선 cats소개, category theory 기반에 fp라이브러리?
  // 다양한 함수를 통해서 코드를 쉽게 개발할 수 있게 도와 준다.
  //

  // https://virtuslab.com/blog/cats-library/

  // cats가 왜필요한가?
  // category theory가 왜 필요한가?

  // foldLeft를 만들어보자
  // foldLeft하는 함수를 고려해보자
  // step 1
  def sum1(xs: List[Int]): Int =
    xs.foldLeft(0)(_ + _)

  // 코드의 재활용!
  // step 2
  def sum2[A](xs: List[A])(zero: A, adder: (A, A) => A): A =
    xs.foldLeft(zero)(adder(_, _))

  // 단점
  // 일일이 adder와 zero값을 넘겨 줘야 한다.
  // 숫자를 더하는 방법은 정해져있다.
  // 특정 타입에 대하여 더하는 값은 정해져있으면 그것을 재활용하고 싶다.
  import cats.syntax.all._
  def sum3[A: Monoid](xs: List[A]): A = {
    xs.foldLeft(Monoid.empty[A])(_ |+| _)
  }

  // functor는 왜필요한가?
  // 자바의 CompletableFuture 비교
  // map, flatMap

  // 행위와 상태를 예측하면 그에 대한 API는 항상 같은걸 쓸수 있다.

  // 에러 처리

  // monoid
  // 1. empty연산자 활용 에러 핸들링
  def onFailureReturn[A: Monoid]: PartialFunction[Throwable, A] = {
    case e: Throwable =>
      e.printStackTrace()
      Monoid.empty[A]
  }

  def getCacheOrDB[A](cache: => Future[Option[A]], db: Future[Option[A]])(
    implicit F: SemigroupK[OptionT[Future, *]]
  ): Future[Option[A]] = {
    val value = SemigroupK[OptionT[Future, *]].combineK(OptionT(cache), OptionT(db))
    value.value
  }

  // implicits을 import하기 귀찮다면?
  // 모아서 하자
  /**
  object implicits
    extends implicits
      with Modules
      with SlickOps
      with TimeConverter
      with ObservableConverter
      with TaskConverter
      with AllSyntax
      with AllInstances
      with HttpClientOps
      with ColumnMappings
      with CollectionOps
      with monix.cats.MonixToCatsConversions
      with Operator
      with IterableConverter
    */

  //

  // 최대한

  // 베이직
  // Functor 언제 필요한가?
  // 1. 대상 F[A], 함수:  A => B 함수가 있다면?
  // map
  // 예제
  // Future로 표현
  // Option도 parseError

  // Monad 언제 필요한가?
  // 2. 대상 F[A], 함수: A => F[B]가 있다면?
  // flatMap
  // 예제

  // 조금더 딥하게
  // Traverse 언제 필요한가?
  // 적용하는 함수의 리턴값이 다른 Context로 감싸져 있을때
  // 4. 대상 F[A], 함수 A => G[B]
  // traverse
  // 결과 G[F[B]]
  // 예제 Future[List[Int]]
  //

  // 5. SemigroupK는 언제 필요한가?
  // <*>
  // 이기고 싶을때
  // firstwin
  // cache failover

  // 6. Applicative는 언제 필요한가?
  // 동시에 실행하고 싶을때

  // 모나드 트랜스 포머 언제 필요한가?
  // 3. 당신의 코드에 map(map(map))) 있다면
  // functor compose를 고민해보라
  // 제너럴한 fold를 하고 싶다면 semigroup을 활용하라.
  // foldLeft
  // 모나드 transformer를 만들때는 return 타입에 집중하라.
  // 리턴타입에 맞추어서 lift하라

  // 7. dependency injection 이 필요하면 kleisli나 reader를 고려해보라

  //

}
