package io.github.xitssky.minio.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.List;

/**
 * Minio configuration properties class
 *
 * @author quentin
 */
@Getter
@Setter
@ConfigurationProperties("spring.minio")
public class MinioConfigurationProperties {
    /**
     * The Minio url
     */
    private String url;

    /**
     * Is Proxy enabled
     */
    private boolean enableProxy = false;

    /**
     * The proxy type (to provide only if the proxy is enabled)
     */
    private String proxyType = "HTTP";

    /**
     * The proxy host (to provide only if the proxy is enabled)
     */
    private String proxyHost = null;

    /**
     * The proxy port (to provide only if the proxy is enabled)
     */
    private String proxyPort = null;

    /**
     * The Minio access key
     */
    private String accessKey;

    /**
     * The Minio secret key
     */
    private String secretKey;

    /**
     * The connection timeout
     */
    private Duration connectTimeout = Duration.ofSeconds(10);

    /**
     * The writing timeout
     */
    private Duration writeTimeout = Duration.ofSeconds(60);

    /**
     * The reading timeout
     */
    private Duration readTimeout = Duration.ofSeconds(10);

    /**
     * Are buckets auto created or not
     */
    private boolean autoCreateBucket = true;

    /**
     * All the buckets
     */
    private List<MinioBucket> buckets;
}
