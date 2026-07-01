package eu.bilik.aoc25.day08;

import eu.bilik.aoc25.common.Context;
import eu.bilik.aoc25.common.Day;
import eu.bilik.aoc25.common.Runner;
import org.jspecify.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Solution implements Day {

    @Override
    public Object part1(Context context) {
        var connectionCounts = 1000;
        var largestCircuits = 3;

        List<Circuit<Point3D>> circuits = context.lines()
                .map(Point3D::parse)
                .map(Circuit::new)
                .toList();

        computeAllPairs(circuits)
                .stream()
                .sorted(Comparator.comparingLong(Pair::distance))
                .limit(connectionCounts)
                .forEach(pair -> pair.first().connect(pair.second()));

        return circuits.stream()
                .collect(Collectors.groupingBy(Circuit::findRoot, Collectors.counting()))
                .values()
                .stream()
                .sorted(Comparator.reverseOrder())
                .limit(largestCircuits)
                .reduce(1L, (a, b) -> a * b);
    }

    @Override
    public Object part2(Context context) {
        return "TODO";
    }

    public static void main(String[] args) {
        Runner.run(8, new Solution());
    }

    private static List<Pair> computeAllPairs(List<Circuit<Point3D>> points) {
        int numberOfPoints = (points.size() * (points.size() - 1)) / 2;
        List<Pair> pairs = new java.util.ArrayList<>(numberOfPoints);
        for (int i = 0; i < points.size(); i++) {
            var first = points.get(i);
            for (int j = i + 1; j < points.size(); j++) {
                var second = points.get(j);
                pairs.add(new Pair(first, second, first.value.euclideanDistance2(second.value)));
            }
        }
        return pairs;
    }
}

final class Circuit<E> {
    final E value;
    @Nullable Circuit<E> parent;

    Circuit(E value) {
        this.value = value;
    }

    Circuit<E> findRoot() {
        return parent == null ? this : parent.findRoot();
    }

    void connect(Circuit<E> other) {
        var parent1 = findRoot();
        var parent2 = other.findRoot();
        if (parent1 != parent2) {
            parent1.parent = parent2;
        }
    }
}

record Pair(Circuit<Point3D> first, Circuit<Point3D> second, long distance) {
}