package io.yurick.account

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
import io.micronaut.reactor.http.client.ReactorHttpClient
import kotlinx.coroutines.reactive.awaitFirst
import mu.KotlinLogging
import java.util.UUID
import javax.inject.Singleton

@Singleton
class AccountService(
    @Client("\${services.account.url}")
    private val reactorHttpClient: ReactorHttpClient,
) {
    suspend fun fetchAccount(accountId: UUID): Account? {
        logger.info { "Start fetch account by id $accountId" }
        val uri = UriBuilder.of("/accounts/{accountId}")
            .expand(mutableMapOf("accountId" to accountId))
        val req = HttpRequest.GET<Account>(uri)

        return reactorHttpClient.retrieve(req, Argument.of(Account::class.java), Argument.of(Map::class.java))
            .awaitFirst()
    }

    companion object {
        private val logger = KotlinLogging.logger { }
    }
}
