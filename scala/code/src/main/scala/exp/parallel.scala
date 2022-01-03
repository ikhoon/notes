package exp

import java.text.SimpleDateFormat
import java.util.Date

import cats.Monad
import util.log

import scala.concurrent.{Await, Awaitable, Future}
import scala.concurrent.duration._
import model._
import cats.implicits._
import cats._

/**
  * Created by ikhoon on 19/08/2017.
  */
object util {
  val formatter = new SimpleDateFormat("HH:mm:ss")
  def log(str: => String): Unit =
    println(s"${formatter.format(new Date())} - $str")

}
object model {
  case class Item(id: Int, catalogId: Int, brandId: Int)
  type Catalog = String
  type Brand = String
  type Wish = String
  type Category = String
  type Detail = String
  type Certification = String
}

sealed trait AsyncApiComponent[AsyncIO[+ _]] {

  import model._

  def itemRepository: ItemRepository
  def catalogRepository: CatalogRepository
  def brandRepository: BrandRepository
  def itemWishCountRepository: ItemWishCountRepository
  def categoryRepository: CategoryRepository
  def itemDetailRepository: ItemDetailRepository
  def itemCertificationRepository: ItemCertificationRepository

  trait ItemRepository {
    def findById(id: Int): AsyncIO[Item]
  }

  trait CatalogRepository {
    def findById(id: Int): AsyncIO[Catalog]
  }

  trait BrandRepository {
    def findById(id: Int): AsyncIO[Brand]
  }

  trait ItemWishCountRepository {
    def findByItemId(id: Int): AsyncIO[Wish]
  }

  trait CategoryRepository {
    def findOneByBrandId(id: Int): AsyncIO[Category]
  }

  trait ItemDetailRepository {
    def findByItemId(id: Int): AsyncIO[Detail]
  }

  trait ItemCertificationRepository {
    def findByItemId(id: Int): AsyncIO[Certification]
  }
}

object ScalaFutureApiComponent extends AsyncApiComponent[Future] {

  import scala.concurrent.ExecutionContext.Implicits.global
  def itemRepository: ItemRepository = itemRepositoryScalaFuture
  def catalogRepository: CatalogRepository = catalogRepositoryScalaFuture
  def brandRepository: BrandRepository = brandRepositoryScalaFuture
  def itemWishCountRepository: ItemWishCountRepository = itemWishCountRepositoryScalaFuture
  def categoryRepository: CategoryRepository = categoryRepositoryScalaFuture
  def itemDetailRepository: ItemDetailRepository = itemDetailRepositoryScalaFuture
  def itemCertificationRepository: ItemCertificationRepository = itemCertificationRepositoryScalaFuture

  object itemRepositoryScalaFuture extends ItemRepository {
    def findById(id: Int): Future[Item] = Future {
      Thread.sleep(1000)
      log(s"item-$id")
      Item(id, 1000, 100000)
    }
  }

  object catalogRepositoryScalaFuture extends CatalogRepository {
    def findById(id: Int): Future[Catalog] = Future {
      Thread.sleep(1000)
      log(s"catalog-$id")
      s"catalog-$id"
    }
  }

  object brandRepositoryScalaFuture extends BrandRepository {
    def findById(id: Int): Future[Brand] = Future {
      Thread.sleep(1000)
      log(s"brand-$id")
      s"brand-$id"
    }
  }

  object itemWishCountRepositoryScalaFuture extends ItemWishCountRepository {
    def findByItemId(id: Int): Future[Wish] = Future {
      Thread.sleep(1000)
      log(s"wish-$id")
      s"wish-$id"
    }
  }

  object categoryRepositoryScalaFuture extends CategoryRepository {
    def findOneByBrandId(id: Int): Future[Category] = Future {
      Thread.sleep(1000)
      log(s"category-$id")
      s"category-$id"
    }
  }

  object itemDetailRepositoryScalaFuture extends ItemDetailRepository {
    def findByItemId(id: Int): Future[Detail] = Future {
      Thread.sleep(1000)
      log(s"detail-$id")
      s"detail-$id"
    }
  }

  object itemCertificationRepositoryScalaFuture extends ItemCertificationRepository {
    def findByItemId(id: Int): Future[Certification] = Future {
      Thread.sleep(1000)
      log(s"certification-$id")
      s"certification-$id"
    }
  }

}

//object MonixTaskApiComponent extends AsyncApiComponent[Task] {
//
//  def itemRepository: ItemRepository = itemRepositoryMonixTask
//  def catalogRepository: CatalogRepository = catalogRepositoryMonixTask
//  def brandRepository: BrandRepository = brandRepositoryMonixTask
//  def itemWishCountRepository: ItemWishCountRepository = itemWishCountRepositoryMonixTask
//  def categoryRepository: CategoryRepository = categoryRepositoryMonixTask
//  def itemDetailRepository: ItemDetailRepository = itemDetailRepositoryMonixTask
//  def itemCertificationRepository: ItemCertificationRepository = itemCertificationRepositoryMonixTask
//
//  object itemRepositoryMonixTask extends ItemRepository {
//    def findById(id: Int): Task[Item] = Task {
//      Thread.sleep(1000)
//      log(s"item-$id")
//      Item(id, 1000, 100000)
//    }
//  }
//
//  object catalogRepositoryMonixTask extends CatalogRepository {
//    def findById(id: Int): Task[Catalog] = Task {
//      Thread.sleep(1000)
//      log(s"catalog-$id")
//      s"catalog-$id"
//    }
//  }
//
//  object brandRepositoryMonixTask extends BrandRepository {
//    def findById(id: Int): Task[Brand] = Task {
//      Thread.sleep(1000)
//      log(s"brand-$id")
//      s"brand-$id"
//    }
//  }
//
//  object itemWishCountRepositoryMonixTask extends ItemWishCountRepository {
//    def findByItemId(id: Int): Task[Wish] = Task {
//      Thread.sleep(1000)
//      log(s"wish-$id")
//      s"wish-$id"
//    }
//  }
//
//  object categoryRepositoryMonixTask extends CategoryRepository {
//    def findOneByBrandId(id: Int): Task[Category] = Task {
//      Thread.sleep(1000)
//      log(s"category-$id")
//      s"category-$id"
//    }
//  }
//
//  object itemDetailRepositoryMonixTask extends ItemDetailRepository {
//    def findByItemId(id: Int): Task[Detail] = Task {
//      Thread.sleep(1000)
//      log(s"detail-$id")
//      s"detail-$id"
//    }
//  }
//
//  object itemCertificationRepositoryMonixTask extends ItemCertificationRepository {
//    def findByItemId(id: Int): Task[Certification] = Task {
//      Thread.sleep(1000)
//      log(s"certificatTaskn-$id")
//      s"certificatTaskn-$id"
//    }
//  }
//
//}

object experiment {

//  import monix.execution.Scheduler.Implicits.global
//  def main(args: Array[String]): Unit = {
//    println("scala future monad with eager evaluation")
//    awaitTime { getProduct(ScalaFutureApiComponent, 10) }
////    println(scalaFutureResult)
//
//    println
//    println("monix task monad with lazy evaluation")
//    awaitTime { getProduct(MonixTaskApiComponent, 10).runToFuture}
////    println(catsEffectResult)
//
//    println
//    println("scala future applicative with eager evaluation")
//    awaitTime { getProductAp(ScalaFutureApiComponent, 10) }
//    //    println(scalaFutureResult)
//
//    println
//    println("monix task applicative with lazy evaluation")
//    awaitTime { getProductAp(MonixTaskApiComponent, 10).runToFuture}
//    //    println(catsEffectResult)
//  }

  def awaitTime[R](block: => Awaitable[R]): R = {
    val start = System.nanoTime()
    val result = Await.result(block, 1 minutes)
    val elapsed = System.nanoTime() - start
    println("elapsed : " + elapsed / (1000 * 1000 * 1000.0) + " sec")
    result
  }
  def getProduct[F[+ _]: Monad](api: AsyncApiComponent[F], itemId: Int): F[List[String]] = {
    import api._
    val itemFuture = itemRepository.findById(itemId)
    itemFuture.flatMap { item =>
      val catalogFuture = catalogRepository.findById(item.catalogId)
      val brandFuture = brandRepository.findById(item.brandId)
      val wishFuture = itemWishCountRepository.findByItemId(item.id)
      val categoryFuture = categoryRepository.findOneByBrandId(item.brandId)
      val itemDetailFuture = itemDetailRepository.findByItemId(item.id)
      val itemCertificationFuture = itemCertificationRepository.findByItemId(item.id)
      for {
        catalog <- catalogFuture
        brand <- brandFuture
        wishCount <- wishFuture
        category <- categoryFuture
        detail <- itemDetailFuture
        certifications <- itemCertificationFuture
      } yield List(brand, catalog, wishCount, category, detail, certifications)
    }
  }

  def getProductAp[F[+ _]: Monad: Applicative](api: AsyncApiComponent[F], itemId: Int): F[List[String]] = {
    import api._
    itemRepository.findById(itemId).flatMap { item =>
      (
        catalogRepository.findById(item.catalogId),
        brandRepository.findById(item.brandId),
        itemWishCountRepository.findByItemId(item.id),
        categoryRepository.findOneByBrandId(item.brandId),
        itemDetailRepository.findByItemId(item.id),
        itemCertificationRepository.findByItemId(item.id)
      ).mapN {
        case (catalog, brand, wish, category, detail, cert) =>
          List(brand, catalog, wish, category, detail, cert)
      }
    }
  }
}
