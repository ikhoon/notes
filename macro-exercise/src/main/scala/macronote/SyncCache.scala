package macronote

/**
  * Created by Liam.M on 2018. 06. 27..
  */
trait SyncCache[K, V] {
  def get(k: K): Option[V]

  def put(k: K, v: V): Unit
}

