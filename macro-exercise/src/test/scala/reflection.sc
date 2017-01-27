import scala.reflect.runtime.universe._

val tree = Apply(
  Select(
    Ident(TermName("x")),
    TermName("$plus")),
  List(Literal(Constant(2)))
)


val (fun, arg) = tree match {
  case Apply(fn, a :: Nil) => (fn, a)
}

// left hand side matching 좀더 쉽게
val Apply(fun1, arg1 :: Nil) = tree


// tree 일반적으로 꽤 복잡하다.
val tree1 = Apply(
  Select(
    Apply(
      Select(
        Ident(TermName("x")),
        TermName("$plus")
      ),
      List(Literal(Constant(2)))
    ),
    TermName("$plus")
  ),
  List(Literal(Constant(3)))
)

val Apply(fun2, arg2:: Nil) = tree1

// tree를 탐색하고 싶을때 일반적인 pattern 매칭으로는 쉽지 않다.
// Traverser를 이용해보자

object traverser extends Traverser {
  var applies = List[Apply]()

  override def traverse(tree: Tree): Unit = tree match {
    case app @ Apply(f, args) =>
      applies = app :: applies
      super.traverse(f)
      super.traverseTrees(args)
    case _ => super.traverse(tree)
  }
}

traverser.traverse(tree1)
traverser.applies


// tree 만들기 - reify
val tree2 = reify(println(2)).tree
showRaw(tree2)

// 트리 합치기 - splicing tree
val x = reify(2)

reify(println(x.splice))


val fun3 = reify(println)

// 아래껀 안됨요.
//reify(fun3.splice(2))

// tree 만들기 - toolbox의 parse를 이용하는 방법
import scala.tools.reflect.ToolBox

val tb = runtimeMirror(getClass.getClassLoader).mkToolBox()

showRaw(tb.parse("println(2)"))

// macro를 사용하면 Toolbox.parse를 사용하지 않는다.

import scala.language.experimental.macros

def impl(c: scala.reflect.macros.Context) =
  c.Expr[Unit](c.parse("println(2)"))


//def test = macro impl
//
//test

val tree3 = reify { "test".length }.tree

val tb2 = runtimeMirror(getClass.getClassLoader).mkToolBox()

val ttree3 = tb2.typecheck(tree3)

ttree3.tpe

ttree3.symbol

// 매뉴얼로 tree 만들기 - 다른것이 안될때 써라, 어쩔수 없을때

val tree4 = Apply(Ident(TermName("println")), List(Literal(Constant(2))))

