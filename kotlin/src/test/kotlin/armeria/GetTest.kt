package armeria

import com.linecorp.armeria.client.WebClient
import com.linecorp.armeria.common.HttpResponse
import com.linecorp.armeria.common.HttpStatus
import com.linecorp.armeria.server.ServerBuilder
import com.linecorp.armeria.testing.junit.server.ServerExtension
import kotlinx.coroutines.delay
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension


class GetTest {
  @Test
  fun simple() {
    val client = WebClient.of(server.httpUri())
    val result = client.get("/hello").aggregate().join()
    assertThat(result.status()).isEqualTo(HttpStatus.OK)
  }

  @Test
  fun suspendService() {
    val client = WebClient.of(server.httpUri())
    val result = client.get("/suspend").aggregate().join()
    assertThat(result.contentUtf8()).isEqualTo("world")
  }



  companion object {
    val okService = get("/hello") { ctx, req ->
      HttpResponse.of(HttpStatus.OK)
    }

    private suspend fun slow(): String {
      delay(100)
      return "world"
    }

    val delayService = get("/suspend") { ctx, req ->
      slow()
    }

    @JvmField
    @RegisterExtension
    val server = object : ServerExtension() {
      override fun configure(sb: ServerBuilder) {
        sb.service(okService)
        sb.service(delayService)
      }
    }
  }
}