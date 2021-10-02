package io.yurick.configuration

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Produces
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.http.server.exceptions.ExceptionHandler
import io.micronaut.http.server.exceptions.response.ErrorContext
import io.micronaut.http.server.exceptions.response.ErrorResponseProcessor
import javax.inject.Singleton

@Produces
@Singleton
class HttpClientResponseHandler(
    private val responseProcessor: ErrorResponseProcessor<*>,
) : ExceptionHandler<HttpClientResponseException, HttpResponse<*>> {
    override fun handle(request: HttpRequest<*>, exception: HttpClientResponseException): HttpResponse<*> {
        val response = HttpResponse.status<Any>(exception.status)
        val body = exception.response.getBody(Map::class.java)

        return if (body.isPresent) {
            response.body(body.get())
        } else {
            responseProcessor.processResponse(
                ErrorContext.builder(request)
                    .cause(exception)
                    .errorMessage(exception.message.orEmpty())
                    .build(),
                response,
            )
        }
    }
}
