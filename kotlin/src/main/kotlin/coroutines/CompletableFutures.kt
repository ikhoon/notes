package coroutines

import java.util.concurrent.CompletableFuture
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.await
import kotlinx.coroutines.launch

suspend fun awaitFutures(): Int {
    val a = CompletableFuture.completedFuture(1)
    val b = CompletableFuture.completedFuture(2)
    return a.await() + b.await()
}

fun main() {
    val job = GlobalScope.launch {
        awaitFutures()
    }
}
