package catsnote

import cats.Id
import cats.data.{Kleisli, Reader}
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Future

/**
  * Created by ikhoon on 20/12/2016.
  */
/*
class ReaderSpec extends WordSpec with Matchers {

  "reader" should {

    // monad => list, option, reader

    // DI


//    case class Reader[Action](run: Action)

    //



    "config" in {

      case class Item(id: Int, name: String)

      trait ItemRepository {
        def findById(id: Int): Item
        def findByName(name: String): List[Item]
      }

      // Reader[주입, 반환]
      trait ProductService {
        def getItemById(id: Int): Reader[ItemRepository, Item] =
          Reader[ItemRepository, Item](
            itemRepository => itemRepository.findById(id)
          )
        def getItemsByName(name: String): Reader[ItemRepository, List[Item]] =
          Reader[ItemRepository, List[Item]](
            itemRepository => itemRepository.findByName(name)
          )

        // for comprehension, 합성

        // flatMap(A => F[B])
        def getSameNameById(id: Int): Reader[ItemRepository, List[Item]] =
          for {
            item <- getItemById(id)
            sameNameItems <- getItemsByName(item.name)
          } yield sameNameItems
      }

      object ProductService extends ProductService

      val sameNameById: Reader[ItemRepository, List[Item]] =
        ProductService.getSameNameById(10)
//      sameNameById.run()

      trait ItemService {
        def getItem(id: Int): Kleisli[Future, ItemRepository, Item] =
          Kleisli(x => Future(x.findById(id)) )

        def getItemsByName(name: String): Kleisli[Future, ItemRepository, List[Item]] =
          Kleisli(x => Future(x.findByName(name)))

        def getSameNameItemsById(id: Int): Kleisli[Future, ItemRepository, List[Item]] =
          for {
            item <- getItem(id)
            sameNameItems <- getItemsByName(item.name)
          } yield sameNameItems
      }

      object ItemService extends ItemService

      object ItemRepositoryImpl extends ItemRepository {
        def findById(id: Int): Item = Item(id, s"$id-name")
        def findByName(name: String): List[Item] =
          (1 to 5).map(Item(_, name)).toList
      }

      val items: Future[List[Item]] = ItemService
        .getSameNameItemsById(10)
        .run(ItemRepositoryImpl)

      items shouldBe List(Item(1, "10-name"), Item(2, "10-name"), Item(3, "10-name"), Item(4, "10-name"), Item(5, "10-name"))

    }

  }

}
*/
