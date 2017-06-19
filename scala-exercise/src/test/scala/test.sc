import java.time.{LocalDate, LocalDateTime}
import shapeless._
import shapeless.ops.hlist._
import shapeless.record._

case class ProductPet(
                       id: Int,
                       // 추가 필드들 //
                       createdAt: LocalDateTime,
                       updatedAt: LocalDateTime,
                     )

type DbGen[Id] = Record.`'id -> Id, 'createdAt -> LocalDateTime, 'updatedAt -> LocalDateTime`.T

val lgen = LabelledGeneric[ProductPet]

val diff = Diff[lgen.Repr, DbGen[Int]]