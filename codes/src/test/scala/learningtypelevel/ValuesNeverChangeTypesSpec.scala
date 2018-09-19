package learningtypelevel

import learningtypelevel.TypeProjection.StSource
import org.scalatest.{Matchers, WordSpec}

/**
  * Created by ikhoon on 2016. 9. 5..
  */
class ValuesNeverChangeTypesSpec extends WordSpec with Matchers {

  "values and types" should {

    "assignment rewrites existentials" in {

      var mxs: StSource[String] = StSource(0)(x => (x.toString, x + 1))

      // 여기서는 `S`가 Int이다.
      val s1: StSource[String]#S = mxs.init

      // 그리고 mxs를 바꾼다.
      mxs = StSource("ab")(x => (x, x.reverse))

      // 이순간에는 `S`는 String디.
      // 컴파일 되는것 처럼 보이나 컴파일 되지 않는다.

      assertDoesNotCompile("mxs.emit(s1)")  // 컴파일 안됨.

//      Error:(21, 16) type mismatch;
//      found   : s1.type (with underlying type StSource[String]#S)
//      required: _2.S where val _2: StSource[String]
//      mxs.emit(s1)

    }

    "making variable read-only matters" in {
      val imxs: StSource[String] = StSource(0)(x => (x.toString, x + 1))
      // 위의 mutable 버전과 타입의 모양이 다르다. path dependent type이다.
      val s1: imxs.S = imxs.init
      // imxs가 바뀔수 없으니 문제가 되지 않는다.
      // S가 여전히 `Int`이다.
      imxs.emit(s1)
    }

  }

}
