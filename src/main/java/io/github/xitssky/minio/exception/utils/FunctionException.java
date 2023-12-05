package io.github.xitssky.minio.exception.utils;

import java.util.function.Function;

/**
 * A {@link Function} that can throw exception
 *
 * @param <T>: the input type of the {@link Function}
 * @param <R>: the output type of the {@link Function}
 * @param <E> the exception type that the {@link Function} can throw
 * @author quentin
 */
public interface FunctionException<T, R, E extends Exception> {

    /**
     * Apply the function to the {@link T} input
     *
     * @param input: the input of the {@link Function}
     * @return the {@link R} output of the {@link Function}
     * @throws E the exception that the {@link Function} can throw
     */
    R apply(T input) throws E;
}
