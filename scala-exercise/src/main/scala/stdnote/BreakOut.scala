package stdnote

/**
  * Created by ikhoon on 04/12/2017.
  * https://scala-lang.org/blog/2017/05/30/tribulations-canbuildfrom.html
  * 여기에 나온 breakout에 대해서 정리해봄
  */
object BreakOut {

  def main(args: Array[String]): Unit = {
    val xs: List[Int] = 1 :: 2  :: 3 :: Nil
    // 아래 코드는 컴파일이 안된다.
//    val xsWithSquares : Map[Int, Int] = xs.map(x => (x, x * x))
    // 결과 타입은 List[(Int, Int)] 인데 변수의 타입은 Map[Int, Int]이다.

    // 이를 해결하기 위해서는 명시적으로 toMap함수를 사용할수 있다.
    val xsWithSquares : Map[Int, Int] = xs.map(x => (x, x * x)).toMap
    // 하지만 이건 효율적이지 않다.
    // List 객체를 만들고 Map개체를 생성하기 때문에 중간 객체가 생기고 최적화 되지 않는다.




    // 성능도 잡고 타입도 바로 잡아주는 방법은 breakOut을 사용하면 된다.
    val xsWithSquares2 : Map[Int, Int] = xs.map(x => (x, x * x))(collection.breakOut)
    // 기본적인 CanBuildFrom에서 CanBuildFrom[-From, -Elem, +To] 3개의 타입을 가지고 implicit을 찾았다.
    // List => Map으로 갈수 있는 CanbuildFrom이 없었던걸로 추측

    // breakOut을 주입하면 From을 지워버리고 Elem과 To만 사용해서 CanBuildFrom을 찾고 한번에 빌드할수가 있다.


  }
}
