package stdnote

/**
  * Created by ikhoon on 09/08/2018.
  */
trait Service[A, B] {
  def apply(a: A): B
}

trait FilterV1[A, B] { self =>
  def apply(svc: Service[A, B]): Service[A, B]

  // V1: 합성은 쉽다.
  def andThen(next: FilterV1[A, B]): FilterV1[A, B] = {
    new FilterV1[A, B] {
      override def apply(a: Service[A, B]): Service[A, B] = {
        next(self.apply(a))
      }
    }
  }
}

object FilterAppV1 {

  def main(args: Array[String]): Unit = {

    val serviceA = new Service[Int, String] {
      override def apply(a: Int): String = {
        println(s"in service $a")
        a.toString
      }
    }

    // V1: 하지만 구현이 조금 귀찮다.
    val filterA = new FilterV1[Int, String] {
      override def apply(svc: Service[Int, String]): Service[Int, String] =
        new Service[Int, String] {
          override def apply(a: Int): String = {
            println("before filterA")
            val res = svc(a)
            println("before filterA")
            res
          }
        }
    }

    val filterB = new FilterV1[Int, String] {
      def apply(svc: Service[Int, String]): Service[Int, String] =
        new Service[Int, String] {
          def apply(a: Int): String = {
            println("before filterB")
            val res = svc(a)
            println("before filterB")
            res
          }
        }
    }

    val filterAB = filterA.andThen(filterB)
    filterAB(serviceA)(10)
  }
}

trait FilterV2[A, B] { self =>
  def apply(a: A, service: Service[A, B]): B

  def andThen(next: FilterV2[A, B]): FilterV2[A, B] = {
    new FilterV2[A, B] {
      override def apply(a: A, service: Service[A, B]): B = {
        next(a, new Service[A, B] {
          override def apply(a: A): B = self(a, service)
        })
      }
    }
  }
}

object FilterAppV2 {
  def main(args: Array[String]): Unit = {

    val serviceA = new Service[Int, String] {
      override def apply(a: Int): String = {
        println(s"in service $a")
        a.toString
      }
    }

    val filterA = new FilterV2[Int, String] {
      def apply(a: Int, svc: Service[Int, String]): String = {
        println(s"before filterA V2 $a")
        val res = svc(a)
        println(s"after filterA V2 $a")
        res
      }
    }

    val filterB = new FilterV2[Int, String] {
      def apply(a: Int, svc: Service[Int, String]): String = {
        println(s"before filterB V2 $a")
        val res = svc(a)
        println(s"after filterB V2 $a")
        res
      }
    }

    filterA.andThen(filterB)(10, serviceA)

  }
}

trait FilterV3[A, B] { self =>
  def apply(f: A => B)(a: A): B
  def andThen(next: FilterV3[A, B]): FilterV3[A, B] = {
    new FilterV3[A, B] {
      def apply(f: A => B)(a: A): B = next(self(f))(a)
    }
  }
}

object FilterAppV3 {

  def main(args: Array[String]): Unit = {

    val service: Int => String = int => {
      println(s"in service $int");
      int.toString
    }

    val filterA = new FilterV3[Int, String] {
      def apply(svc: Int => String)(a: Int): String = {
        println(s"before filterA V3 $a")
        val res = svc(a)
        println(s"after filterA V3 $a")
        res
      }
    }

    val filterB = new FilterV3[Int, String] {
      def apply(svc: Int => String)(a: Int): String = {
        println(s"before filterB V3 $a")
        val res = svc(a)
        println(s"after filterB V3 $a")
        res
      }
    }
    val filterAB = filterA.andThen(filterB)(service) _

    filterAB(10)
  }

}
