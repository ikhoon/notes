package shapenote

import shapeless.{:+:, CNil, Poly1}

/**
  * Created by ikhoon on 2016. 8. 15..
  */

object coproduct {
  type ISB = Int :+: String :+: Boolean :+: CNil
}

object sizeM extends Poly1 {
  implicit def caseInt = at[Int](i => (i, i))
  implicit def caseString = at[String](s => (s, s.length))
  implicit def caseBoolean = at[Boolean](b => (b, 1))
}

