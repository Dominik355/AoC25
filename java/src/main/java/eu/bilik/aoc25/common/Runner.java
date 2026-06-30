package eu.bilik.aoc25.common;

public final class Runner {

    private Runner() {
    }

    public static void run(int day, Day solution) {
        var context = new Context(day);

        long t0 = System.nanoTime();
        Object part1 = solution.part1(context);
        long t1 = System.nanoTime();
        Object part2 = solution.part2(context);
        long t2 = System.nanoTime();

        System.out.printf("Day %02d%n", day);
        System.out.printf("  Part 1: %-20s (%.3f ms)%n", part1, (t1 - t0) / 1e6);
        System.out.printf("  Part 2: %-20s (%.3f ms)%n", part2, (t2 - t1) / 1e6);
    }
}
