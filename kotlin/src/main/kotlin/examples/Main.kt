package examples

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

fun main() {
}

suspend fun foo() {
    val res =
        CoroutineScope(Dispatchers.IO).async {
            delay(100)
            10
        }
    res.await()

    val res2 = withContext(Dispatchers.IO) {
        delay(100)
        10
    }
}
