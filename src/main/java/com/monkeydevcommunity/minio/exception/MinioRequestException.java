package com.monkeydevcommunity.minio.exception;

import java.text.MessageFormat;

/**
 * The Exception thrown when a Minio request fail
 *
 * @author quentin
 */
public class MinioRequestException extends RuntimeException {
    private static final String MESSAGE = "Fail to execute {0} request on Minio server";

    /**
     * Constructor
     *
     * @param request: the Minio request that fail
     * @param cause:   the cause of the failure
     */
    public MinioRequestException(String request, Throwable cause) {
        super(MessageFormat.format(MESSAGE, request), cause);
    }

    /**
     * Constructor
     *
     * @param request: the Minio request that fail
     */
    public MinioRequestException(String request) {
        super(MessageFormat.format(MESSAGE, request));
    }
}
