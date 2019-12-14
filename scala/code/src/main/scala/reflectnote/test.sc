
val ru = scala.reflect.runtime.universe
val m = ru.runtimeMirror(getClass.getClassLoader)

class C { def x = 2 }

val im = m.reflect(new C)

val methodX = ru.typeOf[C].decl(ru.TermName("x")).asMethod

val mm = im.reflectMethod(methodX)

mm()

class DD { val x = 2; var y = 3 }

val fieldX = ru.typeOf[DD].decl(ru.TermName("x")).asTerm.accessed.asTerm
val imdd = m.reflect(new DD)
val fmx = imdd.reflectField(fieldX)
fmx.get
fmx.set(3)
fmx.get

val fieldY = ru.typeOf[DD].decl(ru.TermName("x")).asTerm.accessed.asTerm
val fmy = imdd.reflectField(fieldY)
fmy.get
fmy.set(4)
fmy.get
