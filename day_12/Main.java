package day_12;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import java.util.stream.Collectors;


public class Main {
  static Map<Point, Character> map = new HashMap<>();
  static Point start;
  static Point end;

  public static void main(String[] args) {
    List<Point> as = new ArrayList<>();

    try (var bufferedReader = new BufferedReader(new FileReader("input.txt"))) {
      List<String> lines = bufferedReader.lines().collect(Collectors.toList());

      for (int i = 0; i < lines.size(); i++) {
        char[] chars = lines.get(i).toCharArray();

        for (int j = 0; j < chars.length; j++) {
          map.put(new Point(i, j), Character.valueOf(chars[j]));

          if (chars[j] == 'E') {
            end = new Point(i, j);
          }

          if (chars[j] == 'S') {
            start = new Point(i, j);
            as.add(start);
          }

          if (chars[j] == 'a') {
            as.add(new Point(i, j));
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    System.out.println("Min steps from S (1): " + bfs(start));
    System.out.println("Min steps from a (2): " + as.stream().mapToInt(Main::bfs).min().orElse(0));
  }

  private static char parseHeight(char height) {
    return height == 'S' ? 'a' : height == 'E' ? 'z' : height;
  }

  private static List<Point> getPossiblePositions(Point point) {
    List<Point> positions = new ArrayList<>();

    for (Point p : point.getAdjacentPoints()) {
      if (map.containsKey(p) && parseHeight(map.get(p)) <= parseHeight(map.get(point)) + 1) {
        positions.add(p);
      }
    }

    return positions;
  }

  private static int bfs(Point orig) {
    Map<Point, Integer> visited = new HashMap<>();
    Queue<Point> queue = new ArrayDeque<>();

    queue.add(orig);
    visited.put(orig, 0);

    while (!queue.isEmpty()) {
      Point node = queue.poll();

      if (node.equals(end)) {
        return visited.get(end);
      }

      for (Point next : getPossiblePositions(node)) {
        if (!visited.containsKey(next)) {
          queue.add(next);
          visited.put(next, visited.get(node) + 1);
        }
      }
    }

    return Integer.MAX_VALUE;
  }
}


record Point(int x, int y) {
  @Override
  public String toString() {
    return this.x + "," + this.y;
  }

  @Override
  public boolean equals(Object object) {
    if (object instanceof Point) {
      Point point = (Point) object;
      return this.x == point.x() && this.y == point.y();
    }

    return false;
  }

  public List<Point> getAdjacentPoints() {
    return Arrays.asList(new Point(x, y - 1), new Point(x - 1, y), new Point(x + 1, y),
        new Point(x, y + 1));
  }
}
