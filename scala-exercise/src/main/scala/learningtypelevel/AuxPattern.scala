package learningtypelevel

/**
  * Created by Liam.M on 2018. 07. 11..
  */
object AuxPattern {
  // type class
  trait Foo[A] {
    type Out
    def foo(a: A): Out
  }

  trait Foo1[A, B] {
    def foo(a: A): B
  }


  // type member vs type parameter

  // existential type


  // implicit
  object Foo {
    // 1. aux
    type Aux[A, Out0] = Foo[A] { type Out = Out0 }

    // 2. factory
    def apply[A](implicit instance: Foo[A]) =
      instance

    // 3. instance
    implicit val fooInt = new Foo[Int] {
      type Out = String
      def foo(a: Int): String = ???
    }

    // Foo =>
    //
    // foo componion
  }

  Foo[Int]
//  Monad[Future]

  def quz[A, FOut](a: A)
    (implicit
     foo: Foo[A] { type Out = FOut},
     bar: Bar[FOut]) : bar.Out = {
    ???
  }

  def quz1[A, FOut](a: A)
     (implicit
        foo: Foo.Aux[A, FOut],
        bar: Bar[FOut]) : bar.Out = {
    ???
  }
  def quz2[A, B](a: A)(implicit foo: Foo1[A, B]): B = {
    foo.foo(a)
  }
  def quz3[A](a: A)(implicit foo: Foo[A]): foo.Out = {
    foo.foo(a)
  }

  // language 기능




  trait Bar[A] {
    type Out
    def bar(a: A): Out
  }










}
