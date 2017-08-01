package freestylenote

/**
  * Created by ikhoon on 01/08/2017.
  * http://frees.io/docs/ 에 나와있는 quick start에 대해서 알아본다.
  */

// freestyle은 귀찮은 코드(boilerplate free)를 작성하지 않고 algebra를 정의하고
// sequential 하거나 parallel하게 진행하수 있도록 한다.
// 그리고 기계적으로 implicit이 필요한건 modular 프로그램화 하게 한다.

import freestyle._
import freestyle.implicits._


object quickstart {
// algebra를 선언해보자
// 앞에 @free라고 쓴걸 주의 깊게 보자
// FS는 빨간불이 난다. 하지만 신경쓰지 말자
// 하지만 신경이 쓰인다 아놔 ㅡㅡ;;;
@free trait Validation {
  def minSize(s: String, n: Int): FS[Boolean]
  def hasNumber(s: String): FS[Boolean]
}


@free trait Interaction {
  def tell(msg: String): FS[Unit]
  def ask(prompt: String): FS[String]
}

// vim 으로 바꿔서 코딩
// Free algebra는 module로 합쳐질수 있다.
// 기존의 free monad를 coproduct로 합치는것과 같은 형국이다.

@module trait Application {
  val validation: Validation
  val interaction: Interaction 
}


// 벌써 프로그램!!
def program[F[_]](implicit A: Application[F]) = {
  import A._
  import cats.implicits._

  for {
    userInput <- interaction.ask("Give me something with at least 3 chars and a number on  it")
    valid  <- (validation.minSize(userInput, 3)  |@|  validation.hasNumber(userInput)).map(_ && _).freeS
    _  <-  if(valid)
              interaction.tell("awesomesauce!")
            else 
              interaction.tell(s"$userInput is not valid")
  } yield ()
}

// interpreter

}
