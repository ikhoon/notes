package reflectnote

/**
  * Created by ikhoon on 28/08/2018.
  */
class CC { def x = 2 }

class DD { val x = 2; var y = 3 }

object ReflectionApp {

  def main(args: Array[String]): Unit = {

    // method reflect
    // MethodMirror
    val ru = scala.reflect.runtime.universe
    val m = ru.runtimeMirror(getClass.getClassLoader)

    val im = m.reflect(new CC)

    val methodX = ru.typeOf[CC].decl(ru.TermName("x")).asMethod

    val mm = im.reflectMethod(methodX)

    val xx = mm().asInstanceOf[Int]
    println(xx)

    // field reflect
    // FieldMirror
    val fieldX = ru.typeOf[DD].decl(ru.TermName("x")).asTerm.accessed.asTerm
    val imdd = m.reflect(new DD)
    val imx = imdd.reflectField(fieldX)

  }

}
