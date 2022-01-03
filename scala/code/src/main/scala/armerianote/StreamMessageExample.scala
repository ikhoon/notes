package armerianote

import com.linecorp.armeria.common.stream.StreamMessage

object StreamMessageExample {
  def main(args: Array[String]): Unit = {
    val ints = Array(1, 2, 3)
    val streamOfArrayInt: StreamMessage[Array[Int]] = StreamMessage.of(ints)
    val streamOfInt: StreamMessage[Int] = StreamMessage.of(ints: _*)
    println(streamOfInt)
  }
}
