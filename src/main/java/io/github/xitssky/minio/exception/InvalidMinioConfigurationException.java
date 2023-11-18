package io.github.xitssky.minio.exception;

import java.text.MessageFormat;

/**
 * The Exception thrown when a bucket cannot b e created
 *
 * @author quentin
 */
public class InvalidMinioConfigurationException extends RuntimeException {
    private static final String MESSAGE = "Invalid configuration parameter {0}. Expected {1}";

    /**
     * Constructor
     *
     * @param parameterName: the name of the invalid parameter
     * @param expected:      what is expected for this parameter
     */
    public InvalidMinioConfigurationException(String parameterName, String expected) {
        super(MessageFormat.format(MESSAGE, parameterName, expected));
    }
}
