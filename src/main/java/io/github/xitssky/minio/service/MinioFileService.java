package io.github.xitssky.minio.service;

import io.github.xitssky.minio.exception.MinioRequestException;
import io.minio.StatObjectResponse;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

/**
 * The Minio File Service
 *
 * @author quentin
 */
public interface MinioFileService {
    StatObjectResponse getFileInformation(String filename, String bucket) throws MinioRequestException;

    /**
     * Download a file from Minio
     *
     * @param filename: the filename
     * @param bucket:   the name of the bucket on which upload the file
     * @return the file as {@link InputStream}
     * @throws MinioRequestException if download request fail
     */
    InputStream download(String filename, String bucket) throws MinioRequestException;

    /**
     * Download a file from Minio
     *
     * @param path:     the path of the file to create
     * @param filename: the filename
     * @param bucket:   the name of the bucket on which upload the file
     * @return the {@link File}
     * @throws MinioRequestException if download request fail
     */
    File download(String path, String filename, String bucket) throws MinioRequestException;

    /**
     * Upload a file to Minio
     *
     * @param file:     the {@link File} to upload
     * @param filename: the filename
     * @param bucket:   the name of the bucket on which upload the file
     * @param metadata: the user metadata as {@link Map} of {@link String}
     * @throws MinioRequestException if upload request fail
     */
    void upload(File file, String filename, String bucket, Map<String, String> metadata) throws MinioRequestException;

    /**
     * Upload a file to Minio
     *
     * @param file:     the {@link File} to upload
     * @param filename: the filename
     * @param bucket:   the name of the bucket on which upload the file
     * @throws MinioRequestException if upload request fail
     */
    void upload(File file, String filename, String bucket) throws MinioRequestException;

    /**
     * Remove a file from Minio
     *
     * @param filename: the filename
     * @param bucket:   the name of the bucket on which upload the file
     * @throws MinioRequestException if deletion request fail
     */
    void remove(String filename, String bucket) throws MinioRequestException;
}
