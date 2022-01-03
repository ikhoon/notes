package learningtypelevel

import learningtypelevel.TypeProjection.StSource
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

/**
  * Created by ikhoon on 2016. 9. 4..
  */
class TypeProjectionSpec extends AnyWordSpec with Matchers {

  "type projection" should {

    "type parameter see existentially" in {
      val ss = StSource(0) { i: Int =>
        (i, i)
      }
      assert(StSource.runStSource2(ss, ss.init) == (0, 0))

    }
  }
}
