package patternnote

// scratch =>

// v1

object MyFilter {

  def main(args: Array[String]): Unit = {

    trait MyService[A, B] { def apply(a: A): B }
    trait MyFilter[A, B] { self =>
      def apply(service: MyService[A, B]): MyService[A, B]
      def andThen(next: MyFilter[A, B]): MyFilter[A, B] = {
        new MyFilter[A, B]{
          def apply(service: MyService[A, B]): MyService[A, B] = new MyService[A, B] {
            override def apply(a: A): B = next(self(service))(a)
          }
        }
      }
    }
    val filterA = new MyFilter[Int, String] {
      def apply(service: MyService[Int, String]): MyService[Int, String] = new MyService[Int, String] {
        def apply(a: Int): String = {
          println(s"before fitlerA $a"); val res = service(a); println(s"after fitlerA $a"); res
        }
      }
    }
    val filterB = new MyFilter[Int, String] {
      def apply(service: MyService[Int, String]): MyService[Int, String] = new MyService[Int, String] {
        def apply(a: Int): String = {
          println(s"before filterB $a"); val res = service(a); println(s"after filterB $a"); res
        }
      }
    }
    val filterAB = filterA.andThen(filterB)
    val service = new MyService[Int, String] { def apply(a: Int): String = a.toString }
    filterAB(service)(10)
  }
}


object MyFilter2 {
  def main(args: Array[String]): Unit = {
    trait MyService[A, B] {
      def apply(a: A): B
    }
    trait MyFilter[A, B] { self =>
      def apply(a: A, service: MyService[A, B]): B

      def andThen(next: MyFilter[A, B]): MyFilter[A, B] = {
        new MyFilter[A, B] {
          override def apply(a: A, service: MyService[A, B]): B = {
            next(a, new MyService[A, B] {
              override def apply(aa: A): B = self(aa, service)
            })

            self.andThen(next)(a, service)
          }
        }
      }
    }
  }
}