package macronote

import scala.reflect.macros.blackbox

/**
  * Created by Liam.M on 2018. 07. 27..
  */

object MemoryCache {
  var cache = Map.empty[String, Any]
  def getOrElse[V](key: String, value: => V): V = {
    cache.get(key) match {
      case Some(v) => v.asInstanceOf[V]
      case None =>
        cache += key -> value
        value
    }
  }
}
object MethodNameToString {
  def toString(fullClassName: String, methodName: String, params: IndexedSeq[IndexedSeq[Any]]) = {
    val paramStr = params.map(_.mkString(",")).mkString(",")
    s"$fullClassName+$methodName+$paramStr"
  }
}

object CacheAutoKey {
  def memoize[V](f: => V): V =
    macro MacroCacheAutoKey.memoizeImpl[V]
}

class MacroCacheAutoKey(val c: blackbox.Context) {
  import c.universe._

  def memoizeImpl[V: c.WeakTypeTag](f: c.Tree): c.Tree = {
    val enclosingMethodSymbol = getMethodSymbol()
    val classSymbol = getClassSymbol()

    val classNameTree = getFullClassName(classSymbol)
    val methodNameTree = getMethodName(enclosingMethodSymbol)
    val methodParamssSymbols = c.internal.enclosingOwner.info.paramLists
    val methodParamssTree = paramListsToTree(methodParamssSymbols)
    q"""_root_.macronote.MemoryCache.getOrElse(_root_.macronote.MethodNameToString.toString($classNameTree, $methodNameTree, $methodParamssTree), $f)
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

  /**
    * Get the symbol of the method that encloses the macro,
    * or abort the compilation if we can't find one.
    */
  private def getMethodSymbol(): c.Symbol = {

    def getMethodSymbolRecursively(sym: Symbol): Symbol = {
      if (sym == null || sym == NoSymbol || sym.owner == sym)
        c.abort(
          c.enclosingPosition,
          "This memoize block does not appear to be inside a method. " +
            "Memoize blocks must be placed inside methods, so that a cache key can be generated."
        )
      else if (sym.isMethod)
        sym
      else
        getMethodSymbolRecursively(sym.owner)
    }

    getMethodSymbolRecursively(c.internal.enclosingOwner)
  }
  /**
    * Convert the given class symbol to a tree representing the fully qualified class name.
    *
    * @param classSymbol should be either a ClassSymbol or a ModuleSymbol
    */
  private def getFullClassName(classSymbol: c.Symbol): c.Tree = {
    val className = classSymbol.fullName
    // return a Tree
    q"$className"
  }

  /**
    * Convert the given method symbol to a tree representing the method name.
    */
  private def getMethodName(methodSymbol: c.Symbol): c.Tree = {
    val methodName = methodSymbol.asMethod.name.toString
    // return a Tree
    q"$methodName"
  }

  private def paramListsToTree(symbolss: List[List[c.Symbol]]): c.Tree = {
    val identss: List[List[Ident]] = symbolss.map(ss =>
      ss.collect {
        case s => Ident(s.name)
      })
    listToTree(identss.map(is => listToTree(is)))
  }

  /**
    * Convert a List[Tree] to a Tree representing `ArrayBuffer`
    */
  private def listToTree(ts: List[c.Tree]): c.Tree = {
    q"_root_.scala.collection.mutable.ArrayBuffer(..$ts)"
  }
}
