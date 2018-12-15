package stdnote

/**
 * Created by ikhoon on 2018-12-04.
 */
case class Foo(s:String)
object Foo {
  import cats.data.Validated
  import cats.syntax.validated._

  private def hasIsoLengthCats(s: String): Validated[String,String] =
    if (s.length == 2) s.valid else s"$s must have length 2".invalid

  private def belongsToIsoOfficialCats(s: String): Validated[String,String] =
    if (s.startsWith("f")) s.valid else s"$s must start with 'f'".invalid

  def validated(s:String): Validated[String,String]={
    val x=(
      hasIsoLengthCats(s),
      belongsToIsoOfficialCats(s)
    )

    import cats.syntax.apply._
    import cats.instances.string._

    x.mapN((s,_) => s)
  }
}