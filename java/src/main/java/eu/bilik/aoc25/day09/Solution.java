package eu.bilik.aoc25.day09;

import eu.bilik.aoc25.common.Context;
import eu.bilik.aoc25.common.Day;
import eu.bilik.aoc25.common.Runner;

public class Solution implements Day {

    @Override
    public Object part1(Context context) {
        var coordinates = context.lines()
                .map(l -> {
                    var splitted = l.split(",");
                    return new LongTuple2(Long.parseLong(splitted[0]), Long.parseLong(splitted[1]));
                }).toList();

        return coordinates.stream()
                .flatMap(left ->
                        coordinates.stream()
                                .map(right -> {
                                    var width = Math.abs(right._1() - left._1()) + 1;
                                    var height = Math.abs(right._2() - left._2()) + 1;
                                    return new LongTuple2(width, height);
                                })
                )
                .mapToLong(tuple -> tuple._1() * tuple._2())
                .max()
                .orElseThrow();
    }

    @Override
    public Object part2(Context context) {
        return "TODO";
    }

    public static void main(String[] args) {
        Runner.run(9, new Solution());
    }
}

record LongTuple2(long _1, long _2) {
}