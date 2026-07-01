package eu.bilik.aoc25.day10;

import eu.bilik.aoc25.common.Context;
import eu.bilik.aoc25.common.Day;
import eu.bilik.aoc25.common.Runner;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Stream;

public class Solution implements Day {

    @Override
    public Object part1(Context context) {
        return context.lines()
                .map(Machine::parse)
                .mapToInt(Solution::minPresses)
                .sum();
    }

    static int minPresses(Machine machine) {
        int goal = machine.lightsGoal.bits();
        boolean[] seenBitMasks = new boolean[1 << machine.numLights];
        seenBitMasks[0] = true;

        Queue<int[]> queue = new ArrayDeque<>(); // [state, presses]
        queue.add(new int[]{0, 0});

        while (!queue.isEmpty()) {
            var current = queue.remove();
            int state = current[0];
            int presses = current[1];

            for (ButtonWiring button : machine.buttonWirings) {
                int next = state ^ button.bitMask();
                if (next == goal) {
                    return presses + 1;
                }
                if (!seenBitMasks[next]) {
                    seenBitMasks[next] = true;
                    queue.add(new int[]{next, presses + 1});
                }
            }
        }

        throw new RuntimeException("No combination of buttons reaches the goal");
    }

    @Override
    public Object part2(Context context) {
        return "TODO";
    }

    public static void main(String[] args) {
        Runner.run(10, new Solution());
    }
}

final class Machine {
    final Lights lightsGoal;
    final int numLights;
    final List<ButtonWiring> buttonWirings;
    final int[] joltageGoal; // not used in part 1

    Machine(Lights lightsGoal, int numLights, List<ButtonWiring> buttonWirings, int[] joltageGoal) {
        this.lightsGoal = lightsGoal;
        this.numLights = numLights;
        this.buttonWirings = buttonWirings;
        this.joltageGoal = joltageGoal;
    }

    static Machine parse(String line) {
        String[] parts = line.split(" ");
        Lights lightsGoal = Lights.parse(parts[0]);
        int numLights = parts[0].length() - 2; // strip the surrounding brackets

        List<ButtonWiring> buttonWirings = new ArrayList<>(parts.length - 1);
        for (int i = 1; i < parts.length - 1; i++) {
            buttonWirings.add(ButtonWiring.parse(parts[i]));
        }

        var joltagePart = parts[parts.length - 1];
        int[] joltageRequirements = parseIntegers(joltagePart.substring(1, joltagePart.length() - 1));

        return new Machine(lightsGoal, numLights, buttonWirings, joltageRequirements);
    }

    private static int[] parseIntegers(String s) {
        return Stream.of(s.split(",")).mapToInt(Integer::parseInt).toArray();
    }
}

record ButtonWiring(int bitMask, int[] indices) {
    static ButtonWiring parse(String s) {
        int bitMask = 0;

        String[] splits = s.substring(1, s.length() - 1).split(",");
        int[] indices = new int[splits.length];

        for (int i = 0; i < splits.length; i++) {
            indices[i] = Integer.parseInt(splits[i]);
            bitMask |= 1 << indices[i];
        }

        return new ButtonWiring(bitMask, indices);
    }
}

record Lights(int bits) {
    Lights() {
        this(0);
    }

    static Lights parse(String s) {
        int bits = 0;
        // s looks like "[.##.]"; light j is the character at index j + 1.
        for (int j = 0; j < s.length() - 2; j++) {
            if (s.charAt(j + 1) == '#') {
                bits |= 1 << j;
            }
        }
        return new Lights(bits);
    }

    Lights toggle(ButtonWiring wiring) {
        return new Lights(bits ^ wiring.bitMask());
    }
}
