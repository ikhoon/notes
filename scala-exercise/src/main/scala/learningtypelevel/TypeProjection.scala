package learningtypelevel

/**
  * Created by ikhoon on 2016. 9. 1..
  */

object TypeProjection {

  sealed abstract class StSource[A] {
    type S

    def init: S

    def emit(s: S): (A, S)
  }

  object StSource {
    type Aux[A, S0] = StSource[A] {type S = S0}

    def apply[A, S0](i: S0)(f: S0 => (A, S0)): Aux[A, S0] =
      new StSource[A] {
        type S = S0

        def init = i

        def emit(s: S0): (A, S0) = f(s)
      }

    //  def runStSource[A](ss: StSource[A], s: ??) = ss.emit(s) // ??에 무엇이 들어가야 할까?

    //  def runStSource1[A](ss: StSource[A], s: StSource[A]#S) : (A, StSource[A]#S) = ss.emit(s)
    // Error:(24, 13) type mismatch;
    // found   : s.type (with underlying type StSource[A]#S)
    // required: ss.S
    // 컴파일 안됨

    // 두개의 타입간에 연관 관계가 없다.


    // type member를 method의 type parameter로 lifting해보자.
    def runStSource2[A, S](ss: StSource.Aux[A, S], s: S) = ss.emit(s)

    // 잘된다.


  }

}
