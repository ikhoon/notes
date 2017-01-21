package catsnote

import org.scalatest.{Matchers, WordSpec}

/**
  * Created by ikhoon on 2016. 10. 3..
  */
class FreeMonadSpec extends WordSpec with Matchers {

  "free monad" should {

    "kv store" in {
      kv.result shouldBe Some(14)
      kv.result shouldBe Some(14)
    }
  }

}
