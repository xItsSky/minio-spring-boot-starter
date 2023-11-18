package io.github.xitssky.minio.service;

import io.github.xitssky.minio.exception.MinioRequestException;
import io.minio.messages.Bucket;

import java.util.List;

/**
 * The Minio Bucket Service
 *
 * @author quentin
 */
public interface MinioBucketService {
    /**
     * Get all buckets available on Minio
     *
     * @return a {@link List} of {@link Bucket}
     * @throws MinioRequestException if GetAllBuckets request fail
     */
    List<Bucket> findAllBuckets() throws MinioRequestException;

    /**
     * Is bucket exists on Minio
     *
     * @param bucket: the bucket name
     * @return the {@link Boolean}
     * @throws MinioRequestException if BucketExists request fail
     */
    boolean bucketExists(String bucket) throws MinioRequestException;

    /**
     * Get a bucket from Minio
     *
     * @param bucket: the bucket name
     * @return the {@link Bucket}
     * @throws MinioRequestException if GetBucket request fail
     */
    Bucket findBucket(String bucket) throws MinioRequestException;

    /**
     * Create a Bucket
     *
     * @param bucket: the name of the bucket to create
     * @return the created {@link Bucket}
     * @throws MinioRequestException if MakeBucket request fail
     */
    Bucket createBucket(String bucket) throws MinioRequestException;

    /**
     * Remove a Bucket
     *
     * @param bucket: the name of the bucket to remove
     * @throws MinioRequestException if MakeBucket request fail
     */
    void removeBucket(String bucket) throws MinioRequestException;
}
