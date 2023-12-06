package io.github.xitssky.minio.exception.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The LambdaException utility class
 *
 * @author quentin
 */
@Slf4j
@UtilityClass
public class LambdaExceptionUtils {

    /**
     * Wrapper to easily manage exception in {@link Function}
     *
     * @param function:       the {@link Function} to run
     * @param exceptionClass: the Exception {@link Class} that the function can throw
     * @param <T>:            the input of the {@link Function}
     * @param <R>:            the output of the {@link Function}
     * @param <E>:            the exception that the {@link Function} can throw
     * @return the {@link Function}
     */
    public static <T, R, E extends Exception> Function<T, R> handleFunctionException(FunctionException<T, R, E> function, Class<E> exceptionClass) {
        return (T input) -> {
            try {
                return (R) function.apply(input);
            } catch (Exception functionException) {
                final E exception = exceptionClass.cast(functionException);
                throw new RuntimeException(exception.getMessage(), exception.getCause());
            }
        };
    }

    /**
     * Wrapper to easily manage exception in {@link Consumer}
     *
     * @param consumer:       the {@link Consumer} to run
     * @param exceptionClass: the Exception {@link Class} that the function can throw
     * @param <T>:            the input of the {@link Consumer}
     * @param <E>:            the exception that the {@link Consumer} can throw
     * @return the {@link Consumer}
     */
    public static <T, E extends Exception> Consumer<T> handleConsumerException(ConsumerException<T, E> consumer, Class<E> exceptionClass) {
        return (T input) -> {
            try {
                consumer.apply(input);
            } catch (Exception consumerException) {
                final E exception = exceptionClass.cast(consumerException);
                throw new RuntimeException(exception.getMessage(), exception.getCause());
            }
        };
    }
}
