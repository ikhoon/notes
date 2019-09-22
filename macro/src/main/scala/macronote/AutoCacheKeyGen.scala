package macronote

import scala.reflect.macros.blackbox

/**
  * Created by Liam.M on 2018. 07. 31..
  */
object AutoCacheKeyGen {

  def memoize[V](f: => V): V = macro AutoCacheKeyGenImpl.memoizeImpl[V]
}

object InmemoryCache {
  var cache = Map.empty[String, Any]

  def getOrUpdate[V](k: String, f: => V): V = {
    cache.get(k) match {
      case Some(v) => v.asInstanceOf[V]
      case None =>
        val value = f
        cache += k -> value
        value
    }
  }

}

class AutoCacheKeyGenImpl(val c: blackbox.Context) {
  import c.universe._
  def memoizeImpl[V: c.WeakTypeTag](f: c.Tree): c.Tree = {
    val classSymbol = getClassSymbol()
    val className = getFullClassName(classSymbol)
    val methodName = ""
    val methodParamss = ""
    val key = s"$className-$methodName-$methodParamss"
    q"""
      _root_.macronote.InmemoryCache.getOrUpdate($key, $f)
     """
  }

  private def getClassSymbol(): c.Symbol = {
    def getClassSymbolRecursively(sym: Symbol): Symbol = {
      if (sym == null)
        c.abort(c.enclosingPosition, "Encountered a null symbol while searching for enclosing class")
      else if (sym.isClass || sym.isModule)
        sym
      else
        getClassSymbolRecursively(sym.owner)
    }

    getClassSymbolRecursively(c.internal.enclosingOwner)
  }
  private def getFullClassName(classSymbol: c.Symbol): c.Tree = {
    val className = classSymbol.fullName
    // return a Tree
    q"$className"
  }
}
