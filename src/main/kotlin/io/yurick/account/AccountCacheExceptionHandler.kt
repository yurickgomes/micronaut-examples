package io.yurick.account

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Produces
import io.micronaut.http.server.exceptions.ExceptionHandler
import io.micronaut.http.server.exceptions.response.ErrorContext
import io.micronaut.http.server.exceptions.response.ErrorResponseProcessor
import javax.inject.Singleton

class AccountCacheException(cause: Throwable) : RuntimeException(cause)

@Produces
@Singleton
class AccountCacheExceptionHandler(
    private val errorResponseProcessor: ErrorResponseProcessor<Any>,
) : ExceptionHandler<AccountCacheException, HttpResponse<*>> {
    override fun handle(request: HttpRequest<*>, exception: AccountCacheException): HttpResponse<*> =
        errorResponseProcessor.processResponse(
            ErrorContext.builder(request)
                .cause(exception)
                .errorMessage("Internal server error: cannot connect to dynamodb")
                .build(),
            HttpResponse.serverError<Any>()
        )
}
