package day_24;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import java.util.stream.Collectors;

public class Main {

  static int maxX = 0;
  static int maxY = 0;
  static List<Blizzard> blizzards = new ArrayList<>();
  static List<Wall> walls = new ArrayList<>();

  public static void main(String[] args) {
    try (var bufferedReader = new BufferedReader(new FileReader("input.txt"))) {
      parseMap(bufferedReader.lines().collect(Collectors.toList()));
    } catch (IOException e) {
      e.printStackTrace();
    }

    Point orig = new Point(1, 0);
    Point dest = new Point(maxX - 2, maxY - 1);

    int s = breadthFirstSearch(orig, dest);

    System.out.println("Fewest number of minutes (1): " + s);
    System.out.println("Fewest number of minutes (2): "
        + (s + breadthFirstSearch(dest, orig) + breadthFirstSearch(orig, dest)));
  }

  public static void parseMap(List<String> lines) {
    maxX = lines.get(0).length();
    maxY = lines.size();

    for (int j = 0; j < lines.size(); j++) {
      for (int i = 0; i < lines.get(j).length(); i++) {
        if (lines.get(j).charAt(i) == '#') {
          walls.add(new Wall(i, j));
        } else if (lines.get(j).charAt(i) == '<') {
          blizzards.add(new Blizzard(i, j, Dir.LEFT));
        } else if (lines.get(j).charAt(i) == '>') {
          blizzards.add(new Blizzard(i, j, Dir.RIGHT));
        } else if (lines.get(j).charAt(i) == '^') {
          blizzards.add(new Blizzard(i, j, Dir.UP));
        } else if (lines.get(j).charAt(i) == 'v') {
          blizzards.add(new Blizzard(i, j, Dir.DOWN));
        }
      }
    }
  }

  public static int breadthFirstSearch(Point orig, Point dest) {
    int minutes = 0;
    Set<Point> queue = new HashSet<>();

    queue.add(orig);

    while (!queue.isEmpty()) {
      if (queue.contains(dest) && !blizzards.contains(dest)) {
        break;
      }

      blizzards.stream().forEach(Blizzard::move);

      Set<Point> q = new HashSet<>();

      for (var node : queue) {
        q.addAll(Main.getPossiblePositions(node));
      }

      queue = q;
      minutes++;
    }

    return minutes;
  }

  private static List<Point> getPossiblePositions(Point point) {
    List<Point> positions = new ArrayList<>();

    for (Point p : Main.getAdjacentPoints(point)) {
      if (!walls.contains(p) && !blizzards.contains(p) && 0 <= p.getX() && p.getX() < Main.maxX
          && 0 <= p.getY() && p.getY() < maxY) {
        positions.add(p);
      }
    }

    return positions;
  }

  public static List<Point> getAdjacentPoints(Point point) {
    int x = point.getX();
    int y = point.getY();

    return Arrays.asList(new Point(x, y), new Point(x, y - 1), new Point(x - 1, y),
        new Point(x + 1, y), new Point(x, y + 1));
  }

}


class Point {

  private int x;
  private int y;

  public Point(int x, int y) {
    this.setX(x);
    this.setY(y);
  }

  @Override
  public String toString() {
    return this.x + "," + this.y;
  }

  @Override
  public boolean equals(Object object) {
    if (object instanceof Point) {
      Point point = (Point) object;
      return this.x == point.getX() && this.y == point.getY();
    }

    return false;
  }

  @Override
  public int hashCode() {
    return this.toString().hashCode();
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public void setX(int x) {
    this.x = x;
  }

  public void setY(int y) {
    this.y = y;
  }

}


enum Dir {
  LEFT, RIGHT, UP, DOWN
}


class Blizzard extends Point {

  private Dir dir;

  public Blizzard(int x, int y, Dir dir) {
    super(x, y);
    this.setDir(dir);
  }

  @Override
  public String toString() {
    return super.toString() + "," + this.getDir();
  }

  @Override
  public boolean equals(Object object) {
    if (object instanceof Blizzard) {
      Blizzard blizzard = (Blizzard) object;
      return this.getX() == blizzard.getX() && this.getX() == blizzard.getY()
          && this.getDir() == blizzard.getDir();
    } else if (object instanceof Point) {
      Point point = (Point) object;
      return this.getX() == point.getX() && this.getX() == point.getY();
    }

    return false;
  }

  public void move() {
    switch (dir) {
      case Dir.LEFT -> this.setX(this.getX() > 1 ? this.getX() - 1 : Main.maxX - 2);
      case Dir.RIGHT -> this.setX(this.getX() < Main.maxX - 2 ? this.getX() + 1 : 1);
      case Dir.UP -> this.setY(this.getY() > 1 ? this.getY() - 1 : Main.maxY - 2);
      case Dir.DOWN -> this.setY(this.getY() < Main.maxY - 2 ? this.getY() + 1 : 1);
    }
  }

  public Dir getDir() {
    return this.dir;
  }

  public void setDir(Dir dir) {
    this.dir = dir;
  }

}


class Wall extends Point {

  public Wall(int x, int y) {
    super(x, y);
  }

}
