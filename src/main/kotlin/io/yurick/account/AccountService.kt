package io.yurick.account

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
import kotlinx.coroutines.reactive.awaitFirst
import mu.KotlinLogging
import java.util.UUID
import javax.inject.Singleton

@Singleton
class AccountService(
    @Client("\${services.account.url}")
    private val rxHttpClient: RxHttpClient,
) {
    suspend fun fetchAccount(accountId: UUID): Account? {
        logger.info { "Start fetch account by id $accountId" }
        val uri = UriBuilder.of("/accounts/{accountId}")
            .expand(mutableMapOf("accountId" to accountId))
        val req = HttpRequest.GET<Account>(uri)

        return rxHttpClient.exchange(req, Account::class.java).awaitFirst().body()
    }

    companion object {
        private val logger = KotlinLogging.logger { }
    }
}
