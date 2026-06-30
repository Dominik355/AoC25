package eu.bilik.aoc25.common;

import java.util.List;
import java.util.stream.Stream;

public record Context(int day) {
    public List<String> readAllLines() {
        return InputLoader.readAllLines(day);

    }

    public Stream<String> lines() {
        return InputLoader.lines(day);
    }
}