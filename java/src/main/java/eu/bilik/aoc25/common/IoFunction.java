package eu.bilik.aoc25.common;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Function;

@FunctionalInterface
public interface IoFunction<T, R> {
    R apply(T t) throws IOException;

    static <T, R> Function<T, R> wrap(IoFunction<T, R> function) {
        return (t) -> {
            try {
                return function.apply(t);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        };
    }
}
