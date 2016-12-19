package catsnote

import cats.Id
import cats.data.Reader
import org.scalatest.{Matchers, WordSpec}

/**
  * Created by ikhoon on 20/12/2016.
  */
class ReaderSpec extends WordSpec with Matchers {

  "reader" should {

    "config" in {

      case class Item(id: Int, name: String)
      trait ItemRepository {
        def findById(id: Int): Item
        def findByName(name: String): List[Item]
      }


      trait ItemService {
        def getItem(id: Int): Reader[ItemRepository, Item] =
          Reader(_.findById(id))

        def getItemsByName(name: String): Reader[ItemRepository, List[Item]] =
          Reader(_.findByName(name))

        def getSameNameItemsById(id: Int): Reader[ItemRepository, List[Item]] =
          for {
            item <- getItem(id)
            sameNameItems <- getItemsByName(item.name)
          } yield sameNameItems
      }

      object ItemService extends ItemService
      object ItemRepositoryImpl extends ItemRepository {
        def findById(id: Int): Item = Item(id, s"$id-name")
        def findByName(name: String): List[Item] = (1 to 5).map(Item(_, name)).toList
      }
      val items: List[Item] = ItemService.getSameNameItemsById(10).run(ItemRepositoryImpl)
      items shouldBe List(Item(1, "10-name"), Item(2, "10-name"), Item(3, "10-name"), Item(4, "10-name"), Item(5, "10-name"))

    }

  }

}
