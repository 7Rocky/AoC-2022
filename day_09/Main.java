package day_09;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import java.util.stream.Collectors;


public class Main {
  static List<Integer[]> rope = new ArrayList<>();

  public static void main(String[] args) {
    List<String> movements = null;

    try (var bufferedReader = new BufferedReader(new FileReader("input.txt"))) {
      movements = bufferedReader.lines().collect(Collectors.toList());
    } catch (IOException e) {
      e.printStackTrace();
    }

    Integer[] head = new Integer[] {0, 0};
    Integer[] tail = new Integer[] {0, 0};
    Set<String> visited = new TreeSet<>();

    for (String movement : movements) {
      int steps = Integer.parseInt(movement.split(" ")[1]);
      char direction = movement.charAt(0);

      while (steps-- != 0) {
        Integer[] oldHead = new Integer[] {head[0], head[1]};

        switch (direction) {
          case 'U':
            head[0]++;
            break;
          case 'R':
            head[1]++;
            break;
          case 'D':
            head[0]--;
            break;
          case 'L':
            head[1]--;
            break;
        }

        updateTail(head, tail, oldHead);
        visited.add(tail[0] + "," + tail[1]);
      }
    }

    System.out.println("Visited points by T (1): " + visited.size());
    visited.clear();

    for (int i = 0; i < 10; i++) {
      rope.add(new Integer[] {0, 0});
    }

    for (String movement : movements) {
      int steps = Integer.parseInt(movement.split(" ")[1]);
      char direction = movement.charAt(0);

      while (steps-- != 0) {
        Integer[] oldHead = rope.get(0).clone();

        switch (direction) {
          case 'U':
            rope.set(0, new Integer[] {oldHead[0] + 1, oldHead[1]});
            break;
          case 'R':
            rope.set(0, new Integer[] {oldHead[0], oldHead[1] + 1});
            break;
          case 'D':
            rope.set(0, new Integer[] {oldHead[0] - 1, oldHead[1]});
            break;
          case 'L':
            rope.set(0, new Integer[] {oldHead[0], oldHead[1] - 1});
            break;
        }

        updateRope(oldHead);
        visited.add(rope.get(rope.size() - 1)[0] + "," + rope.get(rope.size() - 1)[1]);
      }
    }

    System.out.println("Visited points by 9 (2): " + visited.size());
  }

  private static void updateTail(Integer[] head, Integer[] tail, Integer[] oldHead) {
    if (Math.abs(head[0] - tail[0]) <= 1 && Math.abs(head[1] - tail[1]) <= 1) {
      return;
    }

    Integer[] offset = new Integer[] {head[0] - oldHead[0], head[1] - oldHead[1]};

    if ((tail[0] == oldHead[0]) || (tail[1] == oldHead[1])) {
      tail[0] += offset[0];
      tail[1] += offset[1];
    } else {
      Integer[] distance = new Integer[] {head[0] - tail[0], head[1] - tail[1]};
      tail[0] += (int) Math.signum(distance[0]) * Math.min(Math.abs(distance[0]), 1);
      tail[1] += (int) Math.signum(distance[1]) * Math.min(Math.abs(distance[1]), 1);
    }
  }

  private static void updateRope(Integer[] oldKnot) {
    for (int i = 1; i < rope.size(); i++) {
      Integer[] nextOldKnot = rope.get(i).clone();
      updateTail(rope.get(i - 1), rope.get(i), oldKnot);
      oldKnot = nextOldKnot.clone();
    }
  }
}
