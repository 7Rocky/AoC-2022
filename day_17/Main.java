package day_17;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;


public class Main {

  static String moves;
  static List<Rock> rocks = new ArrayList<>();
  static int spawned = 0;

  public static void main(String[] args) {
    try (var bufferedReader = new BufferedReader(new FileReader("input.txt"))) {
      moves = bufferedReader.readLine();
    } catch (IOException e) {
      e.printStackTrace();
    }

    Rock current = spawn();
    rocks.add(current);

    int i = 0;
    int prev = -1;
    int currentHeight = 0;
    List<Integer> diffs = new ArrayList<>();

    while (true) {
      char c = moves.charAt(i % moves.length());
      i++;

      if (current.isBlocked()) {
        if (spawned == 2022) {
          System.out.println("Height with 2022 rocks (1): " + findHeight());
        }

        if (spawned >= 2022) {
          int height = findHeight();

          if (prev != -1) {
            diffs.add(height - prev);
          } else {
            currentHeight = height;
          }

          prev = height;
        }

        if (diffs.size() > 200 && findCycle(diffs)) {
          int cycleLength = (spawned - 2022) / 2;
          int cycleSum = diffs.subList(0, cycleLength).stream().mapToInt(Integer::valueOf).sum();
          long result = currentHeight + (1000000000000L - 2022) / cycleLength * cycleSum
              + diffs.subList(0, (int) ((1000000000000L - 2022) % cycleLength)).stream()
                  .mapToLong(Long::valueOf).sum();
          System.out.println("Height with 1000000000000 rocks (2): " + result);
          break;
        }

        current = spawn();
        current.move(0, findHeight());
        rocks.add(current);
      }

      if (c == '>') {
        current.right();
      } else {
        current.left();
      }

      current.down();
    }
  }

  public static boolean findCycle(List<Integer> l) {
    return l.subList(0, l.size() / 2).equals(l.subList(l.size() / 2, l.size()));
  }

  public static int findHeight() {
    return 1 + rocks.stream()
        .mapToInt(r -> r.coordinates.stream().mapToInt(c -> c[1]).max().orElse(0)).max().orElse(0);
  }

  public static Rock spawn() {
    Rock rock = null;

    switch (spawned % 5) {
      case 0:
        rock = new Minus(spawned);
        break;
      case 1:
        rock = new Plus(spawned);
        break;
      case 2:
        rock = new Angle(spawned);
        break;
      case 3:
        rock = new Line(spawned);
        break;
      case 4:
        rock = new Square(spawned);
        break;
    }

    spawned++;
    return rock;
  }
}


abstract class Rock {
  protected List<int[]> coordinates = new ArrayList<>();
  private boolean blocked = false;
  private int index;

  public Rock(int index) {
    this.setIndex(index);
  }

  public boolean hits(Rock rock) {
    for (int[] r : rock.coordinates) {
      for (int[] c : coordinates) {
        if (r[0] == c[0] && r[1] == c[1]) {
          return true;
        }
      }
    }

    return false;
  }

  public void move(int x, int y) {
    for (int i = 0; i < coordinates.size(); i++) {
      coordinates.get(i)[0] += x;
      coordinates.get(i)[1] += y;
    }
  }

  public void down() {
    if (!blocked) {
      for (int i = 0; i < coordinates.size(); i++) {
        coordinates.get(i)[1]--;
      }

      if (coordinates.stream().anyMatch(c -> c[1] == -1)
          || Main.rocks.stream().filter(r -> r.getIndex() != index).anyMatch(this::hits)) {
        for (int i = 0; i < coordinates.size(); i++) {
          coordinates.get(i)[1]++;
        }

        this.setBlocked(true);
      }
    }
  }

  public void right() {
    if (!blocked) {
      for (int i = 0; i < coordinates.size(); i++) {
        coordinates.get(i)[0]++;
      }

      if (coordinates.stream().anyMatch(c -> c[0] == 7)
          || Main.rocks.stream().filter(r -> r.getIndex() != index).anyMatch(this::hits)) {
        for (int i = 0; i < coordinates.size(); i++) {
          coordinates.get(i)[0]--;
        }
      }
    }
  }

  public void left() {
    if (!blocked) {
      for (int i = 0; i < coordinates.size(); i++) {
        coordinates.get(i)[0]--;
      }

      if (coordinates.stream().anyMatch(c -> c[0] == -1)
          || Main.rocks.stream().filter(r -> r.getIndex() != index).anyMatch(this::hits)) {
        for (int i = 0; i < coordinates.size(); i++) {
          coordinates.get(i)[0]++;
        }
      }
    }
  }

  public void setBlocked(boolean blocked) {
    this.blocked = blocked;
  }

  public boolean isBlocked() {
    return blocked;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public int getIndex() {
    return index;
  }
}


class Minus extends Rock {
  public Minus(int index) {
    super(index);
    coordinates.add(new int[] {2, 3});
    coordinates.add(new int[] {3, 3});
    coordinates.add(new int[] {4, 3});
    coordinates.add(new int[] {5, 3});
  }
}


class Plus extends Rock {
  public Plus(int index) {
    super(index);
    coordinates.add(new int[] {2, 4});
    coordinates.add(new int[] {3, 4});
    coordinates.add(new int[] {4, 4});
    coordinates.add(new int[] {3, 3});
    coordinates.add(new int[] {3, 5});
  }
}


class Angle extends Rock {
  public Angle(int index) {
    super(index);
    coordinates.add(new int[] {2, 3});
    coordinates.add(new int[] {3, 3});
    coordinates.add(new int[] {4, 3});
    coordinates.add(new int[] {4, 4});
    coordinates.add(new int[] {4, 5});
  }
}


class Line extends Rock {
  public Line(int index) {
    super(index);
    coordinates.add(new int[] {2, 3});
    coordinates.add(new int[] {2, 4});
    coordinates.add(new int[] {2, 5});
    coordinates.add(new int[] {2, 6});
  }
}


class Square extends Rock {
  public Square(int index) {
    super(index);
    coordinates.add(new int[] {2, 3});
    coordinates.add(new int[] {3, 3});
    coordinates.add(new int[] {2, 4});
    coordinates.add(new int[] {3, 4});
  }
}
