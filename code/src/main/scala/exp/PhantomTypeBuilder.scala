package exp

/**
  * Created by ikhoon on 24/08/2017.
  */
object PhantomTypeBuilder {

  trait Bool
  trait True extends Bool
  trait False extends Bool

  sealed trait BuildState {
    type Price <: Bool
    type Name <: Bool
  }

  case class Item(price: Int, name: String, tag: Option[String])

  class Builder[B <: BuildState] { self =>

    val price: Option[Int] = None
    val name: Option[String] = None
    val tag: Option[String] = None

    def withPrice(p: Int) = {
      new Builder[B { type Price = True }] {
        override val name = self.name
        override val tag = self.tag
        override val price = Some(p)
      }
    }

    def withName(n: String) = {
      new Builder[B { type Name = True }] {
        override val name = Some(n)
        override val tag = self.tag
        override val price = self.price
      }
    }

    def withTag(t: String) = {
      new Builder[B] {
        override val name = self.name
        override val tag = Some(t)
        override val price = self.price
      }
    }

    def build(implicit ev1: B#Name =:= True, ev2: B#Price =:= True): Item = {
      Item(price.get, name.get, tag)
    }
  }

  object Builder {
    def apply() = new Builder[BuildState { type Price = False; type Name = False}]
  }

//  Builder().build
//  Builder().withPrice(4500).build

  def main(args: Array[String]): Unit = {
    println(Builder()
      .withPrice(4500)
      .withName("맥도날드")
      .build)

    println(Builder()
      .withPrice(2500)
      .withName("맥도날드")
      .withTag("반값할인")
      .build)

  }


  // that one compile
}