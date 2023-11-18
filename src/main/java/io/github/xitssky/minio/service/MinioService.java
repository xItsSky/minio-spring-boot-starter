package io.github.xitssky.minio.service;

import io.github.xitssky.minio.exception.MinioRequestException;
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
 * The {@link MinioService} use to interact with Minio
 *
 * @author quentin
 */
@Slf4j
@RequiredArgsConstructor
public class MinioService implements MinioBucketService, MinioFileService {
    private final MinioClient minioClient;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Bucket> findAllBuckets() throws MinioRequestException {
        try {
            return this.minioClient.listBuckets();
        } catch (Exception ex) {
            throw new MinioRequestException("findAllBuckets", ex);
        }
    }

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

    @Override
    public Bucket findBucket(String bucket) throws MinioRequestException {
        return this.findAllBuckets().stream()
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
            return this.findBucket(bucket);
        } catch (Exception ex) {
            throw new MinioRequestException("CreateBucket", ex);
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

    @Override
    public StatObjectResponse getFileInformation(String filename, String bucket) throws MinioRequestException {
        try {
            return this.minioClient.statObject(StatObjectArgs.builder()
                    .bucket(bucket)
                    .object(filename)
                    .build());
        } catch (Exception ex) {
            throw new MinioRequestException("GetFileInformation", ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream download(String filename, String bucket) throws MinioRequestException {
        try {
            return this.minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(filename)
                    .build());
        } catch (Exception ex) {
            throw new MinioRequestException("Download", ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File download(String path, String filename, String bucket) throws MinioRequestException {
        try (final InputStream inputStream = this.download(filename, bucket)) {
            final File file = new File(path);
            final FileOutputStream outputStream = new FileOutputStream(file, false);
            FileCopyUtils.copy(inputStream, outputStream);
            return file;
        } catch (IOException ex) {
            throw new MinioRequestException("Download", ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void upload(File file, String filename, String bucket, Map<String, String> metadata) throws MinioRequestException {
        this.upload(file.toPath(), filename, bucket, metadata);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void upload(File file, String filename, String bucket) throws MinioRequestException {
        this.upload(file.toPath(), filename, bucket, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(String filename, String bucket) throws MinioRequestException {
        try {
            this.minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucket)
                    .object(filename)
                    .build());
        } catch (Exception ex) {
            throw new MinioRequestException("RemoveFile", ex);
        }
    }

    /**
     * Upload an object to Minio
     *
     * @param path:     the path of the object to upload
     * @param filename: the name of the file to upload
     * @param bucket:   the bucket on where upload the object
     * @param metadata: the optional metadata to add to the file
     * @throws MinioRequestException if the upload fail
     */
    private void upload(Path path, String filename, String bucket, Map<String, String> metadata) throws MinioRequestException {
        try {
            UploadObjectArgs.Builder builder = UploadObjectArgs.builder()
                    .bucket(bucket)
                    .object(filename)
                    .filename(path.toString());

            // Set user metadata if provided
            Optional.ofNullable(metadata).ifPresent(builder::userMetadata);

            this.minioClient.uploadObject(builder.build());
        } catch (Exception ex) {
            throw new MinioRequestException("uploadFile", ex);
        }
    }
}
