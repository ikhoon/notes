package macros

/**
  * Created by ikhoon on 06/09/2017.
  * 참조: http://www.cakesolutions.net/teamblogs/scalameta-tut-cache
  */


object NaiveImplementation {
  class CacheBackEnd[K, V] {
    private var map = Map.empty[K, V]     // ignore the fact it is not thread safe

    // `compute` is a function, only evaluated in case of cache miss
    def getOrElse(k: K, compute: K => V): V = {
      map.get(k) match {
        case Some(v) => v       // cache hit
        case None =>
          val v = compute(k)
          map = map + (k -> v)
          v
      }
    }
  }

  def cache[K, V](fn: K => V)(cacheStorage: CacheBackEnd[K, V]): K => V = {
    (k: K) =>
      cacheStorage.getOrElse(k, fn)
  }

  def fib(i: Int): Int = i match {
    case 0 => 0
    case 1 => 1
    case n => fib(n - 2) + fib(n - 1)
  }

  val cacheBackend = new CacheBackEnd[Int, Int]

  def cachedFib(i: Int): Int = cache(fib)(cacheBackend)(i)

  // alternatively, we can inline the logic
  def cachedFib2(i: Int): Int = cache[Int, Int] {
    case 0 => 0
    case 1 => 1
    case n => fib(n - 2) + fib(n - 1)
  }(cacheBackend)(i)
}

object MacroCache {
  trait SyncCache[K, V] {
    def get(k: K): Option[V]

    def put(k: K, v: V): Unit
  }

  // TODO FIXME
  val cacheBackEnd = new SyncCache[Int, Int]


  @cache(cacheBackEnd)
}
