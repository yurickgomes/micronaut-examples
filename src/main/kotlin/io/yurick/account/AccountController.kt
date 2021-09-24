package io.yurick.account

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import kotlinx.coroutines.reactor.awaitSingleOrNull
import java.util.UUID

@Controller("/accounts")
class AccountController(
    private val accountService: AccountService,
    private val accountDao: AccountDao,
) {
    @Get("/{accountId}")
    suspend fun fetchAccountById(
        @PathVariable("accountId") accountId: UUID,
    ): Account? {
        val cachedAccount = accountDao.getAccountById(accountId.toString()).awaitSingleOrNull()
            ?: return accountService.fetchAccount(accountId)?.also {
                accountDao.save(it.toAccountCache())
            }

        return cachedAccount.toAccountModel()
    }
}
