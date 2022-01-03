package shapenote

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import shapeless.{Generic, HList}

/**
  * Created by ikhoon on 2016. 8. 14..
  */
class AritySpec extends AnyWordSpec with Matchers {

  "Facility for abstracting over arity" should {

    "arity" in {
      import shapeless.syntax.std.function._
      import shapeless.ops.function._

      def applyProduct[P <: Product, F, L <: HList, R](
        p: P
      )(f: F)(implicit gen: Generic.Aux[P, L], fp: FnToProduct.Aux[F, L => R]) =
        f.toProduct(gen.to(p))

      applyProduct(1, 2)((_: Int) + (_: Int)) shouldBe 3
      applyProduct(1, 2, 3)((_: Int) * (_: Int) * (_: Int)) shouldBe 6
    }
  }

}
