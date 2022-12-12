package day_05;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

  public static void main(String[] args) {
    Map<Integer, Stack<Character>> stacks1 = new TreeMap<>();
    Map<Integer, Stack<Character>> stacks2 = new TreeMap<>();
    List<List<Integer>> movements = new ArrayList<>();
    Pattern pattern = Pattern.compile("move (\\d+) from (\\d) to (\\d)");

    try (var bufferedReader = new BufferedReader(new FileReader("input.txt"))) {
      bufferedReader.lines().forEach(line -> {
        if (!line.isEmpty() && line.charAt(1) != '1' && line.charAt(1) != 'o') {
          for (int i = 1; i < line.length(); i += 4) {
            if (line.charAt(i) != ' ') {
              if (!stacks1.containsKey((i / 4) + 1)) {
                stacks1.put((i / 4) + 1, new Stack<Character>());
                stacks2.put((i / 4) + 1, new Stack<Character>());
              }

              stacks1.get((i / 4) + 1).add(0, line.charAt(i));
              stacks2.get((i / 4) + 1).add(0, line.charAt(i));
            }
          }
        } else if (line.startsWith("move")) {
          Matcher matcher = pattern.matcher(line);
          matcher.find();

          int amount = Integer.parseInt(matcher.group(1));
          int stackOrigin = Integer.parseInt(matcher.group(2));
          int stackDestiny = Integer.parseInt(matcher.group(3));

          movements.add(Arrays.asList(amount, stackOrigin, stackDestiny));
        }
      });
    } catch (IOException e) {
      e.printStackTrace();
    }


    for (List<Integer> movement : movements) {
      int amount = movement.get(0);
      int stackOrigin = movement.get(1);
      int stackDestiny = movement.get(2);
      List<Character> substack = new ArrayList<>();

      for (int i = 0; i < amount; i++) {
        stacks1.get(stackDestiny).push(stacks1.get(stackOrigin).pop());
        substack.add(stacks2.get(stackOrigin).pop());
      }

      for (int i = 0; i < amount; i++) {
        stacks2.get(stackDestiny).push(substack.get(amount - i - 1));
      }
    }

    System.out.println("Top stacks (1): " + getTopStacks(stacks1));
    System.out.println("Top stacks (2): " + getTopStacks(stacks2));
  }

  private static String getTopStacks(Map<Integer, Stack<Character>> stacks) {
    StringBuilder sB = new StringBuilder();

    stacks.keySet().stream().forEach(id -> sB.append(stacks.get(id).lastElement()));

    return sB.toString();
  }
}
