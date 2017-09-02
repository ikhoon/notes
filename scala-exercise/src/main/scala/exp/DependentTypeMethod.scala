package exp
import exp.TaggedType.Pagable.{Page, Size}
import exp.domain._
import shapeless.tag
import shapeless.tag.@@

/**
  * Created by ikhoon on 23/08/2017.
  */

object domain {
  case class Item(brandId: Int)

  val items: List[Item] = List.empty

}
object TaggedType {


trait PageTag

trait SizeTag

object Pagable {
  type Page = Int @@ PageTag
  type Size = Int @@ SizeTag
}

trait ItemRepository {

  def findItemsByBrandId(brandId: Int, page: Page, size: Size): List[Item] =
  items
    .filter(_.brandId == brandId)
    .drop(page * size)
    .take(size)


}

  def main(args: Array[String]): Unit = {
    val page: Page = tag[PageTag][Int](1)

    val size: Size = tag[SizeTag][Int](20)

    val itemRepository = new ItemRepository {}

    implicitly[<:<[Page, Int]]
    println(page.getClass)
    itemRepository.findItemsByBrandId(1, page, size)
    ()
 }
}


object notyped {

trait ItemRepository {

def findItemsByBrandId(brandId: Int, page: Int, size: Int): List[Item] =
  items
    .filter(_.brandId == brandId)
    .drop(page * size)
    .take(size)
}
val page = 1
val size = 20

val itemRepository = new ItemRepository {}
itemRepository.findItemsByBrandId(1, size, page)
}

object customtyped {

  case class Page(value: Int) extends AnyVal
  case class Size(value: Int) extends AnyVal
  trait ItemRepository {

def findItemsByBrandId(brandId: Int, page: Page, size: Size): List[Item] =
  items
    .filter(_.brandId == brandId)
    .drop(page.value * size.value)
    .take(size.value)
  }

  val itemRepository = new ItemRepository {}
  val page = Page(1)
  val size = Size(20)
//  itemRepository.findItemsByBrandId(1, size, page)

}