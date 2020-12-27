package armeria

import com.linecorp.armeria.common.HttpData
import com.linecorp.armeria.common.HttpHeaders
import com.linecorp.armeria.common.HttpRequest
import com.linecorp.armeria.common.HttpResponse
import com.linecorp.armeria.common.ResponseHeaders
import com.linecorp.armeria.common.ResponseHeadersBuilder
import com.linecorp.armeria.internal.shaded.guava.collect.ImmutableList
import com.linecorp.armeria.server.HttpServiceWithRoutes
import com.linecorp.armeria.server.Route
import com.linecorp.armeria.server.ServiceRequestContext
import com.linecorp.armeria.server.annotation.ByteArrayResponseConverterFunction
import com.linecorp.armeria.server.annotation.FallthroughException
import com.linecorp.armeria.server.annotation.JacksonResponseConverterFunction
import com.linecorp.armeria.server.annotation.ResponseConverterFunction
import com.linecorp.armeria.server.annotation.StringResponseConverterFunction
import io.micrometer.core.ipc.http.HttpSender
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.future.future
import java.util.concurrent.CompletableFuture

/**
 * 1. Get with path
 *
 * val routeHandler = get("/hello") { (ctx, req) ->
 *     return HttpResponse.of(HttpStatus.OK)
 * }
 *
 * 2. Return suspend function
 *
 * suspend fun foo(): String {
 *   delay(100)
 *   return "world"
 * }
 *
 * val routeHandler = get("/hello") { (ctx, req) ->
 *     return foo()
 * }
 *
 */


val jacksonConverter = JacksonResponseConverterFunction()

val defaultResponseConverters: CompositeResponseConverterFunction =
    CompositeResponseConverterFunction(
        ImmutableList.of(
            JacksonResponseConverterFunction(),
            StringResponseConverterFunction(),
            ByteArrayResponseConverterFunction()
        )
    )


fun get1(path: String, block: (ServiceRequestContext, HttpRequest) -> HttpResponse): HttpServiceWithRoutes {
    val route = Route
        .builder()
        .path(path)
        .build()
    return object : HttpServiceWithRoutes {
        override fun routes(): Set<Route> = setOf(route)
        override fun serve(ctx: ServiceRequestContext, req: HttpRequest): HttpResponse = block(ctx, req)
    }
}

fun get(path: String, block: suspend (ServiceRequestContext, HttpRequest) -> Any): HttpServiceWithRoutes {
    val route = Route
        .builder()
        .path(path)
        .build()
    return object : HttpServiceWithRoutes {
        override fun routes(): Set<Route> = setOf(route)
        override fun serve(ctx: ServiceRequestContext, req: HttpRequest): HttpResponse {
            val future: CompletableFuture<HttpResponse> =
                GlobalScope.future<HttpResponse>(ctx.contextAwareEventLoop().asCoroutineDispatcher()) {
                    val result = block(ctx, req)
                    if (result is HttpResponse) {
                        return@future result
                    }
                    defaultResponseConverters.convertResponse(ctx, ResponseHeaders.of(200), result, HttpHeaders.of())
                }
            return HttpResponse.from(future)
        }
    }
}

/**
 * A [ResponseConverterFunction] which wraps a list of [ResponseConverterFunction]s.
 */
class CompositeResponseConverterFunction(
    private val functions: List<ResponseConverterFunction>
) : ResponseConverterFunction {

    @Throws(Exception::class)
    override fun convertResponse(
        ctx: ServiceRequestContext, headers: ResponseHeaders?,
        result: Any?, trailers: HttpHeaders
    ): HttpResponse {
        if (result is HttpResponse) {
            return result
        }
        ctx.push().use { ignored ->
            for (func in functions) {
                try {
                    return func.convertResponse(ctx, headers, result, trailers)
                } catch (ignore: FallthroughException) {
                    // Do nothing.
                } catch (e: Exception) {
                    throw IllegalStateException(
                        "Response converter " + func.javaClass.name +
                            " cannot convert a result to HttpResponse: " + result, e
                    )
                }
            }
        }
        // There is no response converter which is able to convert 'null' result to a response.
        // In this case, a response with the specified HTTP headers would be sent.
        // If you want to force to send '204 No Content' for this case, add
        // 'NullToNoContentResponseConverterFunction' to the list of response converters.
        if (result == null) {
            return HttpResponse.of(headers, HttpData.empty(), trailers)
        }
        throw IllegalStateException(
            "No response converter exists for a result: " + result.javaClass.name
        )
    }
}

fun addNegotiatedResponseMediaType(ctx: ServiceRequestContext, headers: HttpHeaders): ResponseHeadersBuilder {
    val negotiatedResponseMediaType = ctx.negotiatedResponseMediaType()
    return if (negotiatedResponseMediaType == null || headers.contentType() != null) {
        // Do not overwrite 'content-type'.
        ResponseHeaders.builder()
            .add(headers)
    } else ResponseHeaders.builder()
        .add(headers)
        .contentType(negotiatedResponseMediaType)
}
