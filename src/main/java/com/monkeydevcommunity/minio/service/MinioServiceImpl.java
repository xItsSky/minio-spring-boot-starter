package com.monkeydevcommunity.minio.service;

import com.monkeydevcommunity.minio.exception.MinioRequestException;
import io.minio.*;
import io.minio.messages.Bucket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The {@link MinioService} implementation
 * Œ
 *
 * @author quentin
 */
@Slf4j
@RequiredArgsConstructor
public class MinioServiceImpl implements MinioService {
    private final MinioClient minioClient;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean bucketExists(String bucket) throws MinioRequestException {
        try {
            return this.minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucket)
                    .build());
        } catch (Exception ex) {
            throw new MinioRequestException("GetAllBuckets", ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Bucket> getAllBuckets() throws MinioRequestException {
        try {
            return this.minioClient.listBuckets();
        } catch (Exception ex) {
            throw new MinioRequestException("GetAllBuckets", ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Bucket getBucket(String bucket) throws MinioRequestException {
        return this.getAllBuckets().stream()
                .filter(b -> b.name().equals(bucket))
                .findAny()
                .orElseThrow(() -> new MinioRequestException("GetBucket"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Bucket createBucket(String bucket) throws MinioRequestException {
        try {
            this.minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucket)
                    .build());
            return this.getBucket(bucket);
        } catch (Exception ex) {
            throw new MinioRequestException("MakeBucket", ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeBucket(String bucket) throws MinioRequestException {
        try {
            this.minioClient.removeBucket(RemoveBucketArgs.builder()
                    .bucket(bucket)
                    .build());
        } catch (Exception ex) {
            throw new MinioRequestException("RemoveBucket", ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream downloadFileAsInputStream(String filename, String bucket) throws MinioRequestException {
        try {
            return this.minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(filename)
                    .build());
        } catch (Exception ex) {
            throw new MinioRequestException("GetObject", ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File downloadFile(String path, String filename, String bucket) throws MinioRequestException {
        try (final InputStream inputStream = this.downloadFileAsInputStream(filename, bucket)) {
            // File Creation
            final File file = new File(path);

            // File feeding with the InputStream
            final FileOutputStream outputStream = new FileOutputStream(file, false);
            FileCopyUtils.copy(inputStream, outputStream);
            return file;
        } catch (IOException ex) {
            throw new MinioRequestException("GetFileObject", ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectWriteResponse uploadFile(Path path, String filename, String bucket, Map<String, String> metadata) throws MinioRequestException {
        try {
            UploadObjectArgs.Builder builder = UploadObjectArgs.builder()
                    .bucket(bucket)
                    .object(filename)
                    .filename(path.toString());

            // Set user metadata if provided
            Optional.ofNullable(metadata).ifPresent(builder::userMetadata);

            return this.minioClient.uploadObject(builder.build());
        } catch (Exception ex) {
            throw new MinioRequestException("UploadObject", ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectWriteResponse uploadFile(Path path, String filename, String bucket) throws MinioRequestException {
        return this.uploadFile(path, filename, bucket, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectWriteResponse uploadFile(File file, String filename, String bucket, Map<String, String> metadata) throws MinioRequestException {
        return this.uploadFile(file.toPath(), filename, bucket, metadata);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectWriteResponse uploadFile(File file, String filename, String bucket) throws MinioRequestException {
        return this.uploadFile(file, filename, bucket, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeFile(String filename, String bucket) throws MinioRequestException {
        try {
            this.minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucket)
                    .object(filename)
                    .build());
        } catch (Exception ex) {
            throw new MinioRequestException("RemoveObject", ex);
        }
    }
}
