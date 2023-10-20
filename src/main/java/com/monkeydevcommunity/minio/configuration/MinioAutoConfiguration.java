package com.monkeydevcommunity.minio.configuration;

import com.monkeydevcommunity.minio.exception.BucketCreationException;
import com.monkeydevcommunity.minio.exception.InvalidMinioConfigurationException;
import com.monkeydevcommunity.minio.service.MinioService;
import com.monkeydevcommunity.minio.service.MinioServiceImpl;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * Minio auto configuration class
 *
 * @author quentin
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
@ConditionalOnBean(MinioClient.class)
@EnableConfigurationProperties(MinioConfigurationProperties.class)
public class MinioAutoConfiguration {

    private final MinioConfigurationProperties properties;

    @Bean
    public MinioService minioService(MinioClient minioClient) {
        return new MinioServiceImpl(minioClient);
    }

    @Bean
    public MinioClient minioClient() throws InvalidMinioConfigurationException, BucketCreationException{
        // Client builder
        final MinioClient.Builder minioClientBuilder = MinioClient.builder()
                .endpoint(this.properties.getUrl())
                .credentials(this.properties.getAccessKey(), this.properties.getSecretKey());

        // Set Client with proxy if the proxy is enabled
        if (this.properties.isEnableProxy()) {
            minioClientBuilder.httpClient(this.getHttpClientWithProxy());
        }

        final MinioClient client = minioClientBuilder.build();

        // Set Client timeout
        client.setTimeout(
                this.properties.getConnectTimeout().toMillis(),
                this.properties.getWriteTimeout().toMillis(),
                this.properties.getReadTimeout().toMillis()
        );

        // If auto bucket creation is enable create them
        if (this.properties.isAutoCreateBucket()) {
            this.properties.getBuckets().forEach(bucket -> this.createBucket(client, bucket));
        }

        return client;
    }

    /**
     * Create a bucket
     *
     * @param client: the {@link MinioClient}
     * @param bucket: the name of the bucket to create
     * @throws BucketCreationException if something goes wrong during the bucket creation
     */
    private void createBucket(MinioClient client, String bucket) throws BucketCreationException {
        try {
            // Check that auto creation is enable and bucket doesn't exists
            if (this.properties.isAutoCreateBucket() && !client.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucket)
                    .build())) {
                // Create the Bucket
                client.makeBucket(MakeBucketArgs.builder()
                        .bucket(bucket)
                        .build());
            }
        } catch (Exception ex) {
            throw new BucketCreationException(bucket, ex);
        }
    }

    /**
     * Get an HTTP client with a proxy configured
     *
     * @return an {@link OkHttpClient}
     * @throws InvalidMinioConfigurationException if something is wrong in the configuration
     */
    private OkHttpClient getHttpClientWithProxy() throws InvalidMinioConfigurationException{
        try {
            final String host = this.properties.getProxyHost();
            final int port = Integer.parseInt(this.properties.getProxyHost());
            final Proxy.Type type = Proxy.Type.valueOf(this.properties.getProxyType());

            return new OkHttpClient.Builder()
                    .proxy(new Proxy(type, new InetSocketAddress(host, port)))
                    .build();
        } catch (NumberFormatException portException) {
            throw new InvalidMinioConfigurationException("proxyPort", "valid port (ex: 9000)");
        } catch (IllegalArgumentException typeException) {
            throw new InvalidMinioConfigurationException("proxyType", "HTTP | HTTPS");
        }
    }
}
