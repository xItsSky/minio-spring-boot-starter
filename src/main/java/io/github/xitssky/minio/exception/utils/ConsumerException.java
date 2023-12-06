package io.github.xitssky.minio.exception.utils;

import java.util.function.Consumer;

/**
 * A {@link Consumer} that can throw exception
 *
 * @param <T>: the input type of the {@link Consumer}
 * @param <E>: the exception type that the {@link Consumer} can throw
 * @author quentin
 */
public interface ConsumerException<T, E extends Exception> {

    /**
     * Apply the function to the {@link T} input
     *
     * @param input: the input of the {@link Consumer}
     * @throws E the exception that the {@link Consumer} can throw
     */
    void apply(T input) throws E;
}
