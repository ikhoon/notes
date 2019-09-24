// Reference : https://docs.scala-lang.org/overviews/reflection/environment-universes-mirrors.html

val ru = scala.reflect.runtime.universe

val m: ru.Mirror = ru.runtimeMirror(getClass.getClassLoader)

// # InstanceMirror는 method나 field들을 호출하기위한
// invoker를 생성하기 위해서 사용된다.

class C { def x = 2 }
val cc = new C

// im(instance mirror)
val im = m.reflect(new C)

// # MethodMirror는 instance의 method를 호출하기 위해서 사용된다.
// 스칼라는 오직 instance 함수만 존재한다.
// object의 함수는 object instance의 함수이다.
val methodX = ru.typeOf[C].decl(ru.TermName("x")).asMethod

// 이제 instance mirror에서 method x를 호출하여 보자
// MethodMirror
val mm = im.reflectMethod(methodX)

// 오 값이 나온다.
mm()


// # FieldMirror 는 instance의 필드를 가져오는데 사용이 된다.
// 함수와 마찬가지로 스칼라는 instance의 함수만 존재한다.
class D { val x = 2; var y = 3 }
val imd = m.reflect(new D)
val fieldX = ru.typeOf[D].decl(ru.TermName("x")).asTerm.accessed.asTerm

// FieldMirror
val fmX = imd.reflectField(fieldX)
fmX.get
fmX.set(3)
fmX.get

val fieldY = ru.typeOf[D].decl(ru.TermName("y")).asTerm.accessed.asTerm
val fmY = imd.reflectField(fieldY)
fmY.get
fmY.set(4)
fmY.get


// # ClassMirror은 생성자를 호출 mirror를 만들기 위해서 사용된다.
// static class를 위한 entry point는 `val cm1 = ru.reflectClass(<class symbol>)` 이고
// inner class를 위한 entry point는 `val cm2 = im.reflectClass(<class symbol>)` 이다

case class E(x: Int)
val classE = ru.typeOf[E].typeSymbol.asClass

// ClassMirror
val cm = m.reflectClass(classE)
val ctorE = ru.typeOf[E].decl(ru.termNames.CONSTRUCTOR).asMethod
val ctorm = cm.reflectConstructor(ctorE)

ctorm(10)


// # ModuleMirror는 싱글톤 객체의 인스턴스에 접근하기 위해서 사용된다.
// static object를 위해서 `val mm1 = ru.reflectModule(<module symbol>)`
// inner object를 위해서 `val mm2 = im.reflectModule(<module symbol>)`
// 을 사용한다.


object F { def x = 20000 }

val objectF = ru.typeOf[F.type].termSymbol.asModule

// ModuleMirror
val mmF = m.reflectModule(objectF)
val obj = mmF.instance


val methodXInObject = ru.typeOf[F.type].decl(ru.TermName("x")).asMethod
val imF = m.reflect(F)
val methodInvokerOjbectF = imF.reflectMethod(methodXInObject)
methodInvokerOjbectF()
