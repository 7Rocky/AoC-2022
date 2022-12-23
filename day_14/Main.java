package day_14;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.Map;
import java.util.TreeMap;

public class Main {
  static Map<Integer, Map<Integer, Character>> map = new TreeMap<>();
  static int[] borders = new int[] {Integer.MAX_VALUE, 0, 0, 0};

  public static void main(String[] args) {
    initMap(false);
    setMap();

    while (sand(false));

    System.out.println("Units of sand (1): " + countSand());

    map.clear();

    initMap(true);
    setMap();

    Map<Integer, Character> row = new TreeMap<>();

    for (int i = borders[0]; i <= borders[1]; i++) {
      row.put(i, '#');
    }

    map.put(borders[3], row);
    map.get(0).put(500, '.');

    long oldUnits = countSand();
    long units = oldUnits;

    while (sand(true)) {
      units = countSand();

      if (units == oldUnits) {
        break;
      }
    }

    System.out.println("Units of sand (2): " + units);
  }

  private static long countSand() {
    return map.values().stream().mapToLong(r -> r.values().stream().filter(c -> c == 'o').count())
        .sum();
  }

  private static boolean sand(boolean toEnd) {
    return sand(500, 0, toEnd);
  }

  private static boolean sand(int x, int y, boolean toEnd) {
    while (map.get(y + 1).get(x) == '.') {
      y++;

      if (map.get(y + 1) == null) {
        return false;
      }
    }

    switch (map.get(y + 1).get(x)) {
      case '#':
        if (map.get(y + 1).get(x - 1) == '.') {
          return sand(x - 1, y, toEnd);
        } else if (map.get(y + 1).get(x + 1) == '.') {
          return sand(x + 1, y, toEnd);
        }

        map.get(y).put(x, 'o');
        return true;
      case 'o':
        if (!toEnd && x == 500 && y == 0) {
          return false;
        }

        if ((map.get(y + 1).get(x - 1) == 'o' || map.get(y + 1).get(x - 1) == '#')
            && (map.get(y + 1).get(x + 1) == 'o' || map.get(y + 1).get(x + 1) == '#')) {
          map.get(y).put(x, 'o');
          return true;
        }

        if (map.get(y + 1).get(x - 1) == '.') {
          return sand(x - 1, y, toEnd);
        }

        if ((map.get(y + 1).get(x - 1) == 'o' || map.get(y + 1).get(x - 1) == '#')
            && map.get(y + 1).get(x + 1) == '.') {
          return sand(x + 1, y, toEnd);
        }
    }

    return false;
  }

  private static void initMap(boolean infiniteGround) {
    try (var bufferedReader = new BufferedReader(new FileReader("input.txt"))) {
      String line = bufferedReader.readLine();

      while (line != null) {
        updateBorders(line.split(" -> "));
        line = bufferedReader.readLine();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    borders[0] -= infiniteGround ? 200 : 1;
    borders[1] += infiniteGround ? 200 : 1;
    borders[3]++;

    for (int j = borders[2]; j <= borders[3]; j++) {
      Map<Integer, Character> row = new TreeMap<>();

      for (int i = borders[0]; i <= borders[1]; i++) {
        row.put(i, '.');
      }

      map.put(j, row);
    }

    map.get(0).put(500, '+');
  }

  private static void updateBorders(String[] coordinates) {
    for (String coordinate : coordinates) {
      int x = Integer.parseInt(coordinate.split(",")[0]);
      int y = Integer.parseInt(coordinate.split(",")[1]);

      if (borders[0] > x) {
        borders[0] = x;
      }

      if (borders[1] < x) {
        borders[1] = x;
      }

      if (borders[3] < y) {
        borders[3] = y;
      }
    }
  }

  private static void setMap() {
    try (var bufferedReader = new BufferedReader(new FileReader("input.txt"))) {
      String line = bufferedReader.readLine();

      while (line != null) {
        setClays(line.split(" -> "));
        line = bufferedReader.readLine();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void setClays(String[] coordinates) {
    int oldX = Integer.parseInt(coordinates[0].split(",")[0]);
    int oldY = Integer.parseInt(coordinates[0].split(",")[1]);

    int x;
    int y;

    for (int n = 1; n < coordinates.length; n++) {
      x = Integer.parseInt(coordinates[n].split(",")[0]);
      y = Integer.parseInt(coordinates[n].split(",")[1]);

      if (oldX == x) {
        for (int j = Math.min(oldY, y); j <= Math.max(oldY, y); j++) {
          map.get(j).put(x, '#');
        }
      } else {
        for (int i = Math.min(oldX, x); i <= Math.max(oldX, x); i++) {
          map.get(y).put(i, '#');
        }
      }

      oldX = x;
      oldY = y;
    }
  }
}
