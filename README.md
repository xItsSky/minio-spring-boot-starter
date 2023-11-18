# Minio Spring boot starter

## Usage

* Add dependency to your pom:
    ```
    <dependency>
        <groupId>com.monkeydevcommunity</groupId>
        <artifactId>minio-spring-boot-starter</artifactId>
        <version>1.0.0</version>
    </dependency>
    ```
* Setup your configuration:
  * without proxy
    ```yml
     spring:
      minio:
        url: <YOUR_MINIO_CONFIGURATION>
        access-key: <YOUR_MINIO_ACCESS_KEY>
        secret-key: <YOUR_MINIO_SECRET_KEY>
        default-bucket: <YOUR_MINIO_DEFAULT_BUCKET>
        connect-timeout: <YOUR_MINIO_CONNECTION_TIMEOUT>
        write-timeout: <YOUR_MINIO_WRITE_TIMEOUT>
        read-timeout: <YOUR_MINIO_READ_TIMEOUT>
        auto-create-bucket: <false|true> # True by default
        buckets:
          - mybucket1
          - mybucket2
      ```
    * with proxy
      ```yml
      spring:
        minio:
        enable-proxy: <true|false>
        proxy-type: <HTTP|HTTPS>
        proxy-host: <YOUR_PROXY_HOST>
        proxy-port: <YOUR_PROXY_PORT>
        url: <YOUR_MINIO_CONFIGURATION>
        access-key: <YOUR_MINIO_ACCESS_KEY>
        secret-key: <YOUR_MINIO_SECRET_KEY>
        default-bucket: <YOUR_MINIO_DEFAULT_BUCKET>
        connect-timeout: <YOUR_MINIO_CONNECTION_TIMEOUT>
        write-timeout: <YOUR_MINIO_WRITE_TIMEOUT>
        read-timeout: <YOUR_MINIO_READ_TIMEOUT>
        auto-create-bucket: <false|true> # True by default
        buckets:
          - mybucket1
          - mybucket2
      ```

