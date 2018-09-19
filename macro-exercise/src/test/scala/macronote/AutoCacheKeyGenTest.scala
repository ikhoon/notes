package macronote

import org.scalatest.FunSuite

/**
  * Created by Liam.M on 2018. 07. 31..
  */
class AutoCacheKeyGenTest extends FunSuite {

  import AutoCacheKeyGen._
  def foo(x: Int) = memoize {
    println(s"cache missed $x")
    x
  }

  test("memoize foo") {
    println(foo(10))
    println(foo(10))
    println(foo(10))
    println(foo(10))
    println(foo(10))
  }
}
