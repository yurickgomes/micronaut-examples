package io.yurick.account

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey
import java.util.UUID

@DynamoDbBean
data class AccountCache(
    @get:DynamoDbPartitionKey
    var id: String? = null,
    var name: String? = null,
    var expireAt: Long = System.currentTimeMillis() / 1000L + TTL_DAYS,
) {
    companion object {
        private const val TTL_DAYS = 3 * 24 * 60 * 60
    }
}

data class Account(
    val id: UUID,
    val name: String,
)

fun AccountCache.toAccountModel() = Account(
    id = UUID.fromString(id!!),
    name = name!!,
)

fun Account.toAccountCache() = AccountCache(
    id = id.toString(),
    name = name
)
