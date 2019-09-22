package macronote






import scala.meta._

/*
class cache[K, V](backend: SyncCache[K, V]) extends scala.annotation.StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    defn match {
      // 함수에서만 매크로가 동작하게 하자.
      case defn: Defn.Def =>
        this match {
            // Quasiquote in action, 이건 스칼라 AST와 패턴 매칭을 하는 방법이다.
            // - `new` 이건 객체의 instance와 매칭
            // - `$_` 매크로 이름과 매칭
            // - `[..$tpr]`은 타입 param을 tpr에 바인딩한다.
            // - `$(backendParam)` instantitation의 하나의 argument에 매칭된다.
            //                     multiple argument나 curry된 argument로 하려면 다르게 되어야 한다.
          case q"new $_[..$tpr]($backendParam)" =>
            // 원하는 코드를 캡쳐한다.
            val body: Term = CacheMacroImpl.expand(tpr, backendParam, defn)

            // 함수의 body만 바꾸기를 원하면 이렇게 하면 된다.
            defn.copy(body = body)
          case x =>
            abort(s"Unrecognized pattern $x")
        }

      case _ =>
        abort("This annotation only works on `def`")
    }
  }
}

object CacheMacroImpl {

  def expand(fnTypeParams: Seq[Type], cacheExpr: Term.Param, annotatedDef: Defn.Def): Term = {
    val cache: Term.Name = Term.Name(cacheExpr.syntax)
    annotatedDef match {
      case q"..$_ def $methodName[..$tps](..$nonCurriedParams): $rtType = $expr" =>

        if (nonCurriedParams.size == 1) {
          val paramAsArg = Term.Name(nonCurriedParams.head.name.value)
          q"""
            val result: ${rtType} = $cache.get($paramAsArg) match {
              case Some(v) => v
              case None =>
                val value = ${expr}
                $cache.put($paramAsArg, value)
                value
            }
            result
           """
        } else {
          val paramAsArg = nonCurriedParams.map(p => Term.Name(p.name.value))
          q"""
            val result: ${rtType} = $cache.get((..$paramAsArg)) match {
              case Some(v) => v
              case None =>
                val value = ${expr}
                $cache.put((..$paramAsArg), value)
                value
            }
            result
           """
        }
      case other => abort(s"Expected non-curried method, got $other")
    }
  }
}

*/
