package io.yurick.configuration

import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Value
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import java.net.URI
import javax.inject.Singleton

@Factory
class DynamoDbConfiguration(
    @Value("\${aws.region}")
    private val awsRegion: String,
    @Value("\${aws.accessKeyId}")
    private val accessKeyId: String,
    @Value("\${aws.secretKey}")
    private val secretKey: String,
    @Value("\${aws.endpointOverride}")
    private val endpointOverride: String,
) {
    @Bean
    @Singleton
    fun dynamoDbAsyncClient() = DynamoDbAsyncClient.builder()
        .region(Region.of(awsRegion))
        .endpointOverride(URI.create(endpointOverride)) // dynamodb or localstack local only
        .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKeyId, secretKey)))
        .build()

    @Bean
    @Singleton
    fun dynamoDbEnhancedAsyncClient() =
        DynamoDbEnhancedAsyncClient.builder().dynamoDbClient(dynamoDbAsyncClient()).build()

}
