package io.yurick.httpclientexample

import io.micronaut.context.annotation.Value
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.client.ProxyHttpClient
import io.micronaut.http.uri.UriBuilder
import kotlinx.coroutines.reactive.awaitFirst

@Controller("/proxy")
class HttpClientExampleController(
    private val proxyClient: ProxyHttpClient,
    @Value("\${services.mock-server.url}")
    private val httpClientExampleBaseUrl: String,
) {
    @Get("/accounts/{accountId}/customers/{customerId}")
    suspend fun getHttpClientExampleProxy(
        @Header("x-foo-header") xFooHeader: String,
        @PathVariable("accountId") accountId: String,
        @PathVariable("customerId") customerId: String,
    ): HttpResponse<*>? {
        return proxyClient.proxy(
            HttpRequest.GET<Any>(
                UriBuilder
                    .of("${httpClientExampleBaseUrl}/accounts/{accountId}/customers/{customerId}")
                    .expand(mutableMapOf("accountId" to accountId, "customerId" to customerId))
            ).header("x-foo-header", xFooHeader)
        ).awaitFirst()
    }
}
