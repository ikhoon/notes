package macros

/**
  * Created by ikhoon on 06/09/2017.
  * 참조: http://www.cakesolutions.net/teamblogs/scalameta-tut-cache
  */


object cache {
  trait SyncCache[K, V] {
    def get(k: K): Option[V]

    def put(k: K, v: V): Unit
  }

  // TODO FIXME
//  val cacheBackEnd = new SyncCache[Int, Int]


//  @cache(cacheBackEnd)
}
