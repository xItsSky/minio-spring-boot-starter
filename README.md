# Minio Spring boot starter

## Usage

* Add dependency to your pom:
    ```
    <dependency>
        <groupId>com.monkeydevcommunity</groupId>
        <artifactId>minio-spring-boot-starter</artifactId>
        <version>${minio.version}</version>
    </dependency>
    ```
* Setup your configuration:
    ```yml
  spring:
      minio:
        proxy-type: <HTTP|HTTPS> # only if you are using a proxy
        proxy-host: <YOUR_PROXY_HOST> # only if you are using a proxy
        proxy-port: <YOUR_PROXY_PORT> # only if you are using a proxy
        url: <YOUR_MINIO_CONFIGURATION>
        access-key: <YOUR_MINIO_ACCESS_KEY>
        secret-key: <YOUR_MINIO_SECRET_KEY>
        default-bucket: <YOUR_MINIO_DEFAULT_BUCKET>
        connect-timeout: <YOUR_MINIO_CONNECTION_TIMEOUT>
        write-timeout: <YOUR_MINIO_WRITE_TIMEOUT>
        read-timeout: <YOUR_MINIO_READ_TIMEOUT>
        auto-create-bucket: <false|true> # True by default
        buckets:
          - name: mybucket1
            versioning: <true|false> # False by default
            objectLocking: <true|false> # required versioning to be enabled. False by default
            retention: # required versioning and objectLocking to be enabled
              enabled: <true|false> # False by default
              mode: <COMPLIANCE|GOVERNANCE> # only if enabled=true
              duration: 
                unit: <DAYS|YEARS> # only if enabled=true
                value: 30 # only if enabled=true
            policies: 
              - '{"Statement": [{"Effect": "Allow", "Action": ["s3:GetObject", "s3:GetBucketLocation"], "Resource": ["arn:aws:s3:::*"]}]}'
    ```