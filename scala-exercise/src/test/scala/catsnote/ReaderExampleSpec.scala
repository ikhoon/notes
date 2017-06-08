package catsnote


/**
  * Created by ikhoon on 2017. 6. 8..
  */
class ReaderExampleSpec {

case class Reader[R, A](run: R => A) {
  def map[B](f: A => B): Reader[R, B] =
    Reader(r => f(run(r)))

  def flatMap[B](f: A => Reader[R, B]): Reader[R, B] =
    Reader(r => f(run(r)).run(r))
}


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



}
