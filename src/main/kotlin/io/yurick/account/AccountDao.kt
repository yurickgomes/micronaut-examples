package io.yurick.account

import mu.KotlinLogging
import reactor.core.publisher.Mono
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import javax.inject.Singleton

@Singleton
class AccountDao(
    dynamoClient: DynamoDbEnhancedAsyncClient,
) {
    private val accountTable = dynamoClient.table("Account", tableSchema)

    fun save(accountCache: AccountCache): Mono<Void> =
        Mono.fromFuture(accountTable.putItem(accountCache))
            .doOnError {
                logger.error(it) { "Error saving account to DynamoDb" }
                throw AccountCacheException(it)
            }

    fun getAccountById(accountId: String): Mono<AccountCache> {
        val key = Key.builder().partitionValue(accountId).build()

        return Mono.fromFuture(accountTable.getItem(key))
            .doOnError {
                logger.error(it) { "Error retrieving account from DynamoDb" }
                throw AccountCacheException(it)
            }
    }

    companion object {
        private val tableSchema = TableSchema.fromBean(AccountCache::class.java)
        private val logger = KotlinLogging.logger { }
    }
}
