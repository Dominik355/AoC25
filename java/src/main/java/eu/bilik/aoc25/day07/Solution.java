package eu.bilik.aoc25.day07;

import eu.bilik.aoc25.common.Context;
import eu.bilik.aoc25.common.Day;
import eu.bilik.aoc25.common.Runner;

import java.util.Arrays;
import java.util.stream.Gatherer;

public class Solution implements Day {

    @Override
    public Object part1(Context context) {
        var lines = context.readAllLines();

        long[] initial_tachyons = findInitialTachyon(lines.getFirst());

        return lines.subList(1, lines.size()).stream()
                .gather( // ofund this new API, lets try it
                        Gatherer.<String, SplitResult, Integer>ofSequential(
                                () -> new SplitResult(initial_tachyons, 0),
                                Gatherer.Integrator.of((state, line, downstream) -> {
                                    var r = split(line, state.tachyons);
                                    state.tachyons = r.tachyons;
                                    state.splits += r.splits;
                                    downstream.push(r.splits);
                                    return true;
                                }),
                                Gatherer.defaultFinisher())
                ).reduce(0, Integer::sum);
    }

    @Override
    public Object part2(Context context) {
        var lines = context.readAllLines();

        long[] initial_tachyons = findInitialTachyon(lines.getFirst());

        return lines.subList(1, lines.size()).stream()
                .gather( // ofund this new API, lets try it
                        Gatherer.<String, SplitResult, SplitResult>ofSequential(
                                () -> new SplitResult(initial_tachyons, 0),
                                Gatherer.Integrator.of((state, line, downstream) -> {
                                    var r = split(line, state.tachyons);
                                    state.tachyons = r.tachyons;
                                    state.splits += r.splits;
                                    return true;
                                }),
                                (state, downstream) -> downstream.push(state))
                ).findFirst()
                .map(state -> Arrays.stream(state.tachyons).sum())
                .orElse(0L);
    }

    private static SplitResult split(String line, long[] tachyons) {
        long[] newTachyons = new long[tachyons.length];
        int splitCount = 0;
        for (int i = 0; i < tachyons.length; i++) {
            if (tachyons[i] > 0) {
                if (line.charAt(i) == '^') {
                    newTachyons[i - 1] += tachyons[i];
                    newTachyons[i + 1] += tachyons[i];
                    splitCount++;
                } else {
                    newTachyons[i] += tachyons[i];
                }
            }
        }
        return new SplitResult(newTachyons, splitCount);
    }

    private static long[] findInitialTachyon(String line) {
        int width = line.length();
        long[] tachyons = new long[width];
        tachyons[line.indexOf('S')] = 1;
        return tachyons;
    }

    public static void main(String[] args) {
        Runner.run(7, new Solution());
    }
}


final class SplitResult {
    long[] tachyons;
    int splits;

    SplitResult(long[] tachyons, int splits) {
        this.tachyons = tachyons;
        this.splits = splits;
    }
}
