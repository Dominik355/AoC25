package eu.bilik.aoc25.common;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Loads puzzle input from {@code inputs/dayNN.txt} (relative to the project root).
 */
public final class InputLoader {

    private InputLoader() {
    }

    public static Path pathFor(int day) {
        return Path.of("inputs", String.format("day%02d.txt", day));
    }

    public static List<String> readAllLines(int day) {
        return read_day(day, Files::readAllLines).orElseGet(List::of);

    }

    public static Stream<String> lines(int day) {
        return read_day(day, Files::lines).orElseGet(Stream::empty);
    }

    private static <T> Optional<T> read_day(int day, IoFunction<Path, T> func) {
        var path = pathFor(day);
        if (!Files.exists(path)) {
            System.err.printf("[warn] no input file at %s — running with empty input%n", path);
            return Optional.empty();
        }
        return Optional.of(IoFunction.wrap(func).apply(path));
    }
}
