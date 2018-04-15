package catsnote.praticalcats

import cats.effect._
import cats.implicits._
import cats.mtl.implicits._
/**
  * Created by ikhoon on 05/04/2018.
  */
object TraverseFilterExample extends App {


  val itemIds: List[Int] = (1 to 10).toList

  // Given
  // 주어진 상품 ID들 중에 아직 유효한 상품ID만 필터링하고 싶다.
  // 상품의 상태는 비동기로 가져와야 한다.
  // 아뿔사 map으로 돌렸더니 ID 정보가 없다.
  val step0: List[IO[Boolean]] = itemIds.map(isActiveItem[IO])

  // ID정보는 다시 가져왔다.
  val step1: List[IO[(Int, Boolean)]] = itemIds.map(itemId =>
    isActiveItem[IO](itemId).tupleLeft(itemId)
  )
  // 그러면 가져온 상품상태중 true인것을 찾아야한다.
  // IO가 안에 있으니 불편하다 밖으로 꺼내자
  val step2: IO[List[(Int, Boolean)]] = step1.sequence

  // 그다음에 상태가 active인것만 추리자.
  val step3: IO[List[(Int, Boolean)]] = step2.map(xs => xs.filter(_._2))

  // 그다음에 다시 filter와 같은 효과를 주기 위해서 부가적인 정보인 Boolean은 제거하자
  val step4: IO[List[Int]] = step3.map(_.map(_._1))


  println(step4.unsafeRunSync())
  val activeItem: IO[List[Int]] = itemIds.filterA(isActiveItem[IO])




  case class Item(id: Int, name: String)
  def isActiveItem[F[_]](itemId: Int)(implicit F: Effect[F]): F[Boolean] =
    F.async(cb =>
      if(itemId % 5 == 0) cb(Right(false))
      else cb(Right(true))
    )


  println(activeItem.unsafeRunSync())

}
