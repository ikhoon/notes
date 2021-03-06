import com.google.common.net.HostAndPort

final case class TalkId(toLong: Long)

// 80%
// boxing
val talkId: TalkId = TalkId(10)

// unboxing
val talkId1: Long = 10

// 90%
// shapeless tagged type : boxing x
//
HostAndPort.fromString("[1.1.1.1]")

