package com.monkeydevcommunity.minio.exception;

import java.text.MessageFormat;

/**
 * The Exception thrown when a bucket cannot b e created
 *
 * @author quentin
 */
public class BucketCreationException extends RuntimeException {
    private static final String MESSAGE = "Fail to create bucket {0}";

    /**
     * Constructor
     *
     * @param bucketName: the name of the bucket that cannot be created
     * @param cause:      the cause of the failure
     */
    public BucketCreationException(String bucketName, Throwable cause) {
        super(MessageFormat.format(MESSAGE, bucketName), cause);
    }
}
