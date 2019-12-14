package learningtypelevel

/**
  * Created by ikhoon on 2016. 9. 5..
  */

//
object ValuesNeverChangeTypes {

  final case class OneThing[T](var value: T)

  // 컴파일 되지 않는다.
//  def replaceWithInt(ote: OneThing[_]) : Unit = ote.value = 42
  trait Ref[T] {
    def value: T
    def update(t: T): Unit
  }


  def copyToZeroNP(xs: => PList[_]): Unit = ???
  def copyToZeroNT[T](xs: => PList[T]): Unit = ???
  def time: PList[_] = {
    val t = System.currentTimeMillis()
    if(t % 2 == 0) PCons("even", PNil())
    else PCons(66, PNil())
  }

  copyToZeroNP(time)
  copyToZeroNT(time)
}
