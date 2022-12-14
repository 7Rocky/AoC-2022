package day_10;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import java.util.stream.Collectors;


public class Main {
  static List<Integer[]> rope = new ArrayList<>();

  public static void main(String[] args) {
    List<String> instructions = null;

    try (var bufferedReader = new BufferedReader(new FileReader("input.txt"))) {
      instructions = bufferedReader.lines().collect(Collectors.toList());
    } catch (IOException e) {
      e.printStackTrace();
    }

    int x = 1;
    int signalStrengths = 0;
    int cycle = 0;
    Deque<Integer> toComplete = new ArrayDeque<>();
    List<List<Boolean>> cycles = new ArrayList<>();

    for (int j = 0; j < 6; j++) {
      List<Boolean> row = new ArrayList<>();

      for (int k = 0; k < 40; k++) {
        row.add(false);
      }

      cycles.add(row);
    }

    while (cycle < instructions.size() || !toComplete.isEmpty()) {
      if (cycle < instructions.size()) {
        toComplete.push(0);

        if (instructions.get(cycle).startsWith("addx")) {
          toComplete.push(Integer.parseInt(instructions.get(cycle).substring(5)));
        }
      }

      if (x - 1 <= cycle % 40 && cycle % 40 <= x + 1) {
        cycles.get(cycle / 40).set(cycle % 40, true);
      }

      if ((cycle + 1) % 40 == 20 && cycle < 220) {
        signalStrengths += (cycle + 1) * x;
      }

      x += toComplete.pollLast();
      cycle++;
    }

    System.out.println("Signal strengths (1): " + signalStrengths);
    System.out.println("Word (2):");

    for (List<Boolean> row : cycles) {
      for (boolean b : row) {
        System.out.print(b ? "#" : ".");
      }

      System.out.println();
    }
  }
}
