package macronote

import org.scalatest.{FunSuite, Matchers}

/**
  * Created by Liam.M on 2018. 07. 31..
  */
class CacheAutoKeyTest extends FunSuite with Matchers {

  import CacheAutoKey._
  def getWithCache(id: Int): String = memoize {
    println(s"cache miss with id = $id")
    s"hello $id"
  }

  test("auto key") {
    getWithCache(10)
    getWithCache(10)
    getWithCache(10)
    getWithCache(10)
  }

}
