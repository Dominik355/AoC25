package eu.bilik.aoc25.day06;

import eu.bilik.aoc25.common.Context;
import eu.bilik.aoc25.common.Day;
import eu.bilik.aoc25.common.Runner;

import java.util.Arrays;
import java.util.List;
import java.util.function.LongConsumer;

public class Solution implements Day {

    @Override
    public Object part1(Context context) {
        List<String> allLines = context.readAllLines();

        List<LongResultingConsumer> signs = Arrays.stream(allLines.removeLast().strip().split("\\s+"))
                .map((s) -> switch (s) {
                    case "+" -> new Adder();
                    case "*" -> new Multiplier();
                    default -> throw new IllegalArgumentException("Unknown sign: " + s);
                })
                .toList()
                .reversed();

        // we could also just use split method, but lets try to be faster than that
        for (var line : allLines) {
            int cursor = 0;
            int i = line.length() - 1;

            while (i >= 0) {
                // skipping whitespace
                while (i >= 0 && line.charAt(i) <= ' ') i--;

                if (i < 0) break;

                int count = 0;
                long val = 0;
                while (i >= 0 && line.charAt(i) > ' ') {
                    val += (line.charAt(i) - '0') * ((long) Math.pow(10, count));
                    count++;
                    i--;
                }

                signs.get(cursor).accept(val);
                cursor++;
            }
        }

        return signs.stream()
                .mapToLong(LongResultingConsumer::result)
                .sum();
    }

    @Override
    public Object part2(Context context) {
        List<String> allLines = context.readAllLines();

        List<LongResultingConsumer> signs = Arrays.stream(allLines.removeLast().strip().split("\\s+"))
                .map((s) -> switch (s) {
                    case "+" -> new Adder();
                    case "*" -> new Multiplier();
                    default -> throw new IllegalArgumentException("Unknown sign: " + s);
                })
                .toList();

        int columnCount = allLines.getFirst().length();
        int problemCursor = 0;
        for (int i = 0; i < columnCount; i++) {
            long columnNumber = 0;
            for (int row = 0; row < allLines.size(); row++) {
                char maybeDigit = allLines.get(row).charAt(i);
                if (maybeDigit != ' ') {
                    columnNumber = columnNumber * 10 + (maybeDigit - '0');
                }
            }

            if (columnNumber != 0) {
                System.out.println("Column " + i + " = " + columnNumber + ", problemCursor = " + problemCursor);
                signs.get(problemCursor).accept(columnNumber);
            } else {
                // we can do this safely because problems are separated with single whitespace
                problemCursor++;
            }
        }

        return signs.stream()
                .mapToLong(LongResultingConsumer::result)
                .sum();
    }

    public static void main(String[] args) {
        Runner.run(6, new Solution());
    }
}

interface LongResultingConsumer extends LongConsumer {
    long result();
}

class Adder implements LongResultingConsumer {
    private long value = 0;

    @Override
    public void accept(long value) {
        this.value += value;
    }

    @Override
    public long result() {
        return value;
    }
}

class Multiplier implements LongResultingConsumer {
    private long value = 0;

    @Override
    public void accept(long value) {
        if (value > 0) {
            if (this.value == 0) {
                this.value = value;
            } else {
                this.value *= value;
            }
        }
    }

    @Override
    public long result() {
        return value;
    }
}


class LongVector {
    private long[] data;
    private int size;

    LongVector(int size) {
        this.data = new long[size];
    }

    void add(int i, long val) {
        ensure(i);
        data[i] += val;
    }

    private void ensure(int index) {
        if (index >= data.length) {
            int newCap = Math.max(data.length * 2, index + 1);
            long[] n = new long[newCap];
            System.arraycopy(data, 0, n, 0, data.length);
            data = n;
        }
        if (index >= size) {
            size = index + 1;
        }
    }

    long sum() {
        return Arrays.stream(data, 0, size).sum();
    }
}