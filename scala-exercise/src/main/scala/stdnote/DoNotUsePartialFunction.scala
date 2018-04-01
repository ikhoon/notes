package stdnote

/**
  * Created by ikhoon on 31/03/2018.
  */
object DoNotUsePartialFunction extends App {

  {


    def factorial(n: Int): BigInt = {
      require(n >= 0, "n은 0 혹은 그보다 큰 양수이어야 합니다");
      if (n == 0) 1
      else n * factorial(n - 1)
    }


    factorial(1)
  }

  {

    def factorial(n: Int): Either[String, BigInt] = {
      if (n < 0) Left("n은 0 혹은 그보다 큰 양수이어야 합니다")
      else if (n == 0) Right(1)
      else factorial(n - 1).map(_ * n)
    }


    factorial(1)
  }

}
