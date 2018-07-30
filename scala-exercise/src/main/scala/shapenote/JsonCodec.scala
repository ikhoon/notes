package shapenote

import shapeless._

/**
  * Created by Liam.M on 2018. 06. 27..
  */
object JsonCodec extends App {

  // shapeless generic
  // objective : json encoder

  trait Encoder[A] {
    def encode(a: A): String
  }

  implicit val intEncoder = new Encoder[Int] { def encode(a: Int): String = a.toString }

  implicit val stringEncoder  = new Encoder[String] { def encode(a: String): String = a }

  implicit val booleanEncoder = new Encoder[Boolean] { def encode(a: Boolean): String = a.toString }

  case class Foo(a: Int, b: String, c: Boolean)

  // base case HNil instance
  implicit val hnilEncoder = new Encoder[HNil] {
    override def encode(a: HNil): String = ""
  }

  // HList instance
  // Int(H) :: (String :: Boolean :: HNil)(T)
  // H :: T
  // implicit derivation
  implicit def hlistEncoder[H, T <: HList](
    implicit
    hEncoder: Encoder[H],
    tEncoder: Encoder[T]
  ): Encoder[H :: T] = new Encoder[H :: T] {
    def encode(a: H :: T): String =
      hEncoder.encode(a.head) + ", " + tEncoder.encode(a.tail)
  }


  // 1 번째 : case class => HList
  implicit def genEncoder[A, H](
    implicit
    gen: Generic.Aux[A, H],
    encoder: Encoder[H]
  ): Encoder[A] = new Encoder[A] {
    def encode(a: A): String = encoder.encode(gen.to(a))
  }

  // Generic
  val fooGen = Generic[Foo]
  val value: Int :: String :: Boolean :: HNil = fooGen.to(Foo(1, "hello", false))

  println(value)
  val foo = fooGen.from(value)

  println(foo)

  println(implicitly[Encoder[Foo]].encode(foo))
  class EncoderOps[A](a: A) {
    def csv(implicit encoder: Encoder[A]): String = encoder.encode(a)
  }

  implicit def encoderSyntax[A](a: A): EncoderOps[A] =
    new EncoderOps[A](a)

  println(foo.csv)

  import scala.reflect.runtime.universe._

  println(typeOf[Foo].decls)
  // Generic : case class Foo => Int :: String :: Boolean :: HNil

  // head(Int) :: tail(String :: Boolean :: HNil)

  // head(String) :: tail(Boolean :: HNil)

  // LabeledGeneric :
  // case class Foo => (a -> Int) :: b -> String :: c -> Boolean :: HNil

  // macro => 1

  // shapeless => 1.3

  // reflect ==> 50 ~ 100


  trait Bar[In] {
    type Out
    def bar: Out
  }

  implicit val intBar = new Bar[Int] {
    override type Out = String

    override def bar: String = "hello"
  }

  implicit val strBar = new Bar[String] {
    override type Out = Boolean

    override def bar: Boolean = true
  }

  implicitly[Bar[Int]].bar

}
