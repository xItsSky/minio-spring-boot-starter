package io.github.xitssky.minio.configuration;

import io.github.xitssky.minio.configuration.properties.MinioBucket;
import io.github.xitssky.minio.configuration.properties.MinioBucketRetentionDuration;
import io.github.xitssky.minio.configuration.properties.MinioConfigurationProperties;
import io.github.xitssky.minio.exception.BucketCreationException;
import io.github.xitssky.minio.exception.InvalidMinioConfigurationException;
import io.github.xitssky.minio.exception.MinioRequestException;
import io.github.xitssky.minio.exception.utils.LambdaExceptionUtils;
import io.github.xitssky.minio.service.MinioService;
import io.minio.*;
import io.minio.messages.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;

/**
 * Minio auto configuration class
 *
 * @author quentin
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(MinioConfigurationProperties.class)
public class MinioAutoConfiguration {

    private final MinioConfigurationProperties properties;

    @Bean
    public MinioService minioService(MinioClient minioClient) {
        return new MinioService(minioClient);
    }

    @Bean
    public MinioClient minioClient() throws InvalidMinioConfigurationException, BucketCreationException {
        // Client builder
        final MinioClient.Builder minioClientBuilder = MinioClient.builder()
                .endpoint(this.properties.getUrl())
                .credentials(this.properties.getAccessKey(), this.properties.getSecretKey());

        // Set Client with proxy if the proxy is enabled
        if (this.properties.getProxyHost() != null && !this.properties.getProxyHost().isBlank()
                && this.properties.getProxyPort() != null && !this.properties.getProxyPort().isBlank()) {
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
     * Get an HTTP client with a proxy configured
     *
     * @return an {@link OkHttpClient}
     * @throws InvalidMinioConfigurationException if something is wrong in the configuration
     */
    private OkHttpClient getHttpClientWithProxy() throws InvalidMinioConfigurationException {
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

    /**
     * Create a bucket
     *
     * @param client:              the {@link MinioClient}
     * @param bucketConfiguration: the {@link MinioBucket} containing bucket configuration
     * @throws BucketCreationException if something goes wrong during the bucket creation
     */
    private void createBucket(MinioClient client, MinioBucket bucketConfiguration) throws BucketCreationException {
        try {
            if (this.properties.isAutoCreateBucket() && !this.bucketExists(bucketConfiguration.getName(), client)) {
                // Bucket creation
                client.makeBucket(MakeBucketArgs.builder()
                        .bucket(bucketConfiguration.getName())
                        .objectLock(this.getObjectLock(bucketConfiguration))
                        .build());

                // Versioning configuration
                final VersioningConfiguration versioningConfiguration = this.getVersioningConfiguration(bucketConfiguration);
                if (versioningConfiguration != null) {
                    client.setBucketVersioning(SetBucketVersioningArgs.builder()
                            .bucket(bucketConfiguration.getName())
                            .config(versioningConfiguration)
                            .build());
                }

                // Object Locking configuration
                final ObjectLockConfiguration objectLockConfiguration = this.getObjectLockConfiguration(bucketConfiguration);
                if (objectLockConfiguration != null) {
                    client.setObjectLockConfiguration(SetObjectLockConfigurationArgs.builder()
                            .bucket(bucketConfiguration.getName())
                            .config(objectLockConfiguration)
                            .build());
                }

                // Bucket policies configuration
                final List<String> policiesConfiguration = bucketConfiguration.getPolicies();
                policiesConfiguration.forEach(LambdaExceptionUtils.handleConsumerException(policyConfiguration -> client.setBucketPolicy(SetBucketPolicyArgs.builder()
                        .bucket(bucketConfiguration.getName())
                        .config(policyConfiguration)
                        .build()), Exception.class));
            }
        } catch (Exception ex) {
            throw new BucketCreationException(bucketConfiguration.getName(), ex);
        }
    }

    /**
     * Check whether a bucket exists or not
     *
     * @param bucketName: the name of the bucket to check
     * @param client:     the {@link MinioClient}
     * @return the result as {@link Boolean}
     */
    private boolean bucketExists(String bucketName, MinioClient client) {
        try {
            return client.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception ex) {
            throw new MinioRequestException("BucketExists", ex);
        }
    }

    /**
     * Check if Versioning is enabled
     *
     * @param bucketConfiguration: the {@link MinioBucket} configuration
     * @return a {@link Boolean}
     */
    private VersioningConfiguration getVersioningConfiguration(MinioBucket bucketConfiguration) {
        return bucketConfiguration.isVersioning() || this.getObjectLock(bucketConfiguration) ?
                new VersioningConfiguration(VersioningConfiguration.Status.ENABLED, false) :
                null;
    }

    /**
     * Check if Object locking is enabled
     *
     * @param bucketConfiguration: the {@link MinioBucket} configuration
     * @return a {@link Boolean}
     */
    private boolean getObjectLock(MinioBucket bucketConfiguration) {
        return bucketConfiguration.isObjectLocking() || bucketConfiguration.getRetention().isEnabled();
    }

    /**
     * Check if Versioning is enabled
     *
     * @param bucketConfiguration: the {@link MinioBucket} configuration
     * @return a {@link Boolean}
     */
    private ObjectLockConfiguration getObjectLockConfiguration(MinioBucket bucketConfiguration) {
        final boolean enabled = bucketConfiguration.getRetention().isEnabled();

        if(enabled) {
            final RetentionMode mode = this.getRetentionMode(bucketConfiguration.getRetention().getMode());
            final RetentionDuration duration = this.getMinioRetentionDuration(bucketConfiguration);
            return mode != null && duration != null ? new ObjectLockConfiguration(mode, duration) : null;
        }
        return null;
    }

    /**
     * Get the {@link RetentionDuration}
     *
     * @param bucketConfiguration: the {@link MinioBucket} configuration
     * @return the {@link RetentionDuration}
     */
    private RetentionDuration getMinioRetentionDuration(MinioBucket bucketConfiguration) {
        MinioBucketRetentionDuration duration = bucketConfiguration.getRetention().getDuration();

        if(duration != null) {
            return switch (bucketConfiguration.getRetention().getDuration().getUnit()) {
                case "YEARS" -> new RetentionDurationYears(duration.getValue());
                case "DAYS" -> new RetentionDurationDays(duration.getValue());
                default -> null;
            };
        }

        return null;
    }

    /**
     * Get the {@link RetentionMode}
     *
     * @param mode: the retention mode as {@link String}
     * @return the {@link RetentionMode}
     */
    private RetentionMode getRetentionMode(String mode) {
        if(mode == null) {
            return null;
        }

        return switch (mode) {
            case "GOVERNANCE" -> RetentionMode.GOVERNANCE;
            case "COMPLIANCE" -> RetentionMode.COMPLIANCE;
            default -> null;
        };
    }
}
