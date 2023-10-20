package com.monkeydevcommunity.minio.service;

import com.monkeydevcommunity.minio.exception.MinioRequestException;
import io.minio.ObjectWriteResponse;
import io.minio.messages.Bucket;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * The Minio Service
 *
 * @author quentin
 */
public interface MinioService {
    /**
     * Is bucket exists on Minio
     *
     * @param bucket: the bucket name
     * @return the {@link Boolean}
     * @throws MinioRequestException if BucketExists request fail
     */
    boolean bucketExists(String bucket) throws MinioRequestException;

    /**
     * Get all buckets available on Minio
     *
     * @return a {@link List} of {@link Bucket}
     * @throws MinioRequestException if GetAllBuckets request fail
     */
    List<Bucket> getAllBuckets() throws MinioRequestException;

    /**
     * Get a bucket from Minio
     *
     * @param bucket: the bucket name
     * @return the {@link Bucket}
     * @throws MinioRequestException if GetBucket request fail
     */
    Bucket getBucket(String bucket) throws MinioRequestException;

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

    /**
     * Download a file from Minio as {@link InputStream}
     *
     * @param filename:   the filename
     * @param bucket: the name of the bucket on which upload the file
     * @return {@link InputStream}
     * @throws MinioRequestException if GetObject request fail
     */
    InputStream downloadFileAsInputStream(String filename, String bucket) throws MinioRequestException;

    /**
     * Download a file from Minio
     *
     * @param path:       the path of the file to create as {@link String}
     * @param filename:   the filename
     * @param bucket: the name of the bucket on which upload the file
     * @return {@link File}
     * @throws MinioRequestException if GetObject request fail
     */
    File downloadFile(String path, String filename, String bucket) throws MinioRequestException;

    /**
     * Upload a file to Minio
     *
     * @param path:     the {@link Path} of the file to upload
     * @param filename: the filename
     * @param bucket:   the name of the bucket on which upload the file
     * @param metadata: the user metadata as {@link Map} of {@link String}
     * @return an {@link ObjectWriteResponse}
     * @throws MinioRequestException if UploadObject request fail
     */
    ObjectWriteResponse uploadFile(Path path, String filename, String bucket, Map<String, String> metadata) throws MinioRequestException;

    /**
     * Upload a file to Minio
     *
     * @param path:     the {@link Path} of the file to upload
     * @param filename: the filename
     * @param bucket:   the name of the bucket on which upload the file
     * @return an {@link ObjectWriteResponse}
     * @throws MinioRequestException if UploadObject request fail
     */
    ObjectWriteResponse uploadFile(Path path, String filename, String bucket) throws MinioRequestException;

    /**
     * Upload a file to Minio
     *
     * @param file:     the {@link File} to upload
     * @param filename: the filename
     * @param bucket:   the name of the bucket on which upload the file
     * @param metadata: the user metadata as {@link Map} of {@link String}
     * @return an {@link ObjectWriteResponse}
     * @throws MinioRequestException if UploadObject request fail
     */
    ObjectWriteResponse uploadFile(File file, String filename, String bucket, Map<String, String> metadata) throws MinioRequestException;

    /**
     * Upload a file to Minio
     *
     * @param file:     the {@link File} to upload
     * @param filename: the filename
     * @param bucket:   the name of the bucket on which upload the file
     * @return an {@link ObjectWriteResponse}
     * @throws MinioRequestException if UploadObject request fail
     */
    ObjectWriteResponse uploadFile(File file, String filename, String bucket) throws MinioRequestException;

    /**
     * Delete a file from Minio
     *
     * @param filename: the filename
     * @param bucket:   the name of the bucket on which upload the file
     * @throws MinioRequestException if RemoveObject request fail
     */
    void removeFile(String filename, String bucket) throws MinioRequestException;
}
