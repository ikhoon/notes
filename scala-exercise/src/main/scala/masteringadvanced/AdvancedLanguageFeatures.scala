package masteringadvanced

/**
  * Mastering Advanced Scala
  * Chapter.1 - Advanced Language Features
  */
object AdvancedLanguageFeatures {

  // implicit parameters & conversions 은 생략


  // Type erasure and type tags
  object TypeErasureAndTypeTags {
    // JVM 은 컴파일 시점에 타입 정보 지우고 런타임에 알수가 없다.
    // 이것이 type erasure 이고 이것은 예상치 못한 에러를 발생 시키기도 한다.


    // 이코드는 되지만
    def createArray[T](length: Int, element: T) = new Array[Long](length)
    // 아래코드는 컴파일이 안된다.
//    def createArray[T](length: Int, element: T) = new Array[T](length)

    // error: cannot find class tag for element type T
    // 이런 에러의 발생과 함께
    // 자세한 내용은 http://docs.scala-lang.org/sips/completed/scala-2-8-arrays.html 여기에 있음




  }



}
