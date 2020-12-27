package dsl

/**
 * html {
 *   head {
 *    title { +"hello" }
 *   }
 * }
 */

interface Element {
  fun render(builder: StringBuilder, indent: String)
}

class TextElement(val text: String) : Element {
  override fun render(builder: StringBuilder, indent: String) {
    builder.append("$indent$text\n")
  }
}

@DslMarker
annotation class HtmlTagMarker

@HtmlTagMarker
abstract class Tag(val name: String) : Element {
  val children = mutableListOf<Element>()
  val attributes = hashMapOf<String, String>()

  protected fun <T : Element> initTag(tag: T, init: T.() -> Unit): T {
    tag.init()
    children.add(tag)
    return tag
  }

  override fun render(builder: StringBuilder, indent: String) {
    builder.append("$indent<$name${renderAttributes()}>\n")
    for (child in children) {
      child.render(builder, "$indent  ")
    }
    builder.append("$indent</$name>\n")
  }

  private fun renderAttributes(): String {
    val builder = StringBuilder()
    for ((attr, value) in attributes) {
      builder.append(" $attr=$value")
    }
    return builder.toString()
  }

  override fun toString(): String {
    val builder = StringBuilder()
    render(builder, "")
    return builder.toString()
  }
}

abstract class TagWithText(name: String) : Tag(name) {
  operator fun String.unaryPlus() {
    children.add(TextElement(this))
  }
}

class Html : TagWithText("html") {
  fun head(init: Head.() -> Unit): Head {
    return initTag(Head(), init)
  }

  fun body(init: Body.() -> Unit): Body {
    return initTag(Body(), init)
  }
}

class Head : TagWithText("head") {
  fun title(init: Title.() -> Unit): Unit {
    initTag(Title(), init)
  }
}

class Title : TagWithText("title")


abstract class BodyTag(name: String) : TagWithText(name) {
  fun b(init: B.() -> Unit): B = initTag(B(), init)
  fun p(init: P.() -> Unit): P = initTag(P(), init)
  fun h1(init: H1.() -> Unit): H1 = initTag(H1(), init)
  fun a(href: String, init: A.() -> Unit): A {
    val a = initTag(A(), init)
    a.href = href
    return a
  }
}

class Body : BodyTag("body")
class B : BodyTag("b")
class P : BodyTag("p")
class H1 : BodyTag("h1")

class A : BodyTag("a") {
  var href: String
    get() = attributes["href"]!!
    set(value) {
      attributes["href"] = value
    }
}

fun html(init: Html.() -> Unit): Html {
  val html = Html()
  html.init()
  return html
}

fun main() {
  val html =
    html {
      head {
      }
    }
  val builder = StringBuilder()
  html.render(builder, "")
  println(builder.toString())
}

