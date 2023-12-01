package day_18;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {

  static int[] minout;
  static int[] maxout;


  public static void main(String[] args) {
    List<Cube> cubes = new ArrayList<>();
    Pattern pattern = Pattern.compile("(\\d+),(\\d+),(\\d+)");

    try (var bufferedReader = new BufferedReader(new FileReader("input.txt"))) {
      bufferedReader.lines().forEach(line -> {
        Matcher matcher = pattern.matcher(line);
        matcher.find();

        int x = Integer.parseInt(matcher.group(1));
        int y = Integer.parseInt(matcher.group(2));
        int z = Integer.parseInt(matcher.group(3));

        cubes.add(new Cube(x, y, z));
      });
    } catch (IOException e) {
      e.printStackTrace();
    }

    for (int i = 0; i < cubes.size(); i++) {
      for (int j = i + 1; j < cubes.size(); j++) {
        if (cubes.get(i).isAdjacent(cubes.get(j))) {
          cubes.get(i).decreaseVisibleFaces();
          cubes.get(j).decreaseVisibleFaces();
        }
      }
    }

    int surface = cubes.stream().mapToInt(Cube::getVisibleFaces).sum();
    System.out.println("Surface area of your scanned (1): " + surface);

    minout = new int[] {cubes.stream().mapToInt(Cube::getX).min().orElse(0) - 1,
        cubes.stream().mapToInt(Cube::getY).min().orElse(0) - 1,
        cubes.stream().mapToInt(Cube::getZ).min().orElse(0) - 1};

    maxout = new int[] {cubes.stream().mapToInt(Cube::getX).max().orElse(0) + 1,
        cubes.stream().mapToInt(Cube::getY).max().orElse(0) + 1,
        cubes.stream().mapToInt(Cube::getZ).max().orElse(0) + 1};


    int exteriorSurface = 0;

    Set<Cube> seen = new HashSet<>();
    Queue<Cube> queue = new ArrayDeque<>();

    queue.add(new Cube(maxout[0], maxout[1], maxout[2]));

    while (!queue.isEmpty()) {
      Cube cube = queue.poll();

      if (cubes.contains(cube)) {
        exteriorSurface++;
        continue;
      }

      if (!seen.contains(cube)) {
        seen.add(cube);

        for (Cube c : cube.getNeighbors()) {
          if (Main.isInSpace(c)) {
            queue.add(c);
          }
        }
      }
    }

    System.out.println("Exterior surface area of your scanned (2): " + exteriorSurface);
  }

  public static boolean isInSpace(Cube cube) {
    int[] xyz = new int[] {cube.getX(), cube.getY(), cube.getZ()};

    for (int i = 0; i < 3; i++) {
      if (!(minout[i] <= xyz[i] && xyz[i] <= maxout[i])) {
        return false;
      }
    }

    return true;
  }
}


class Cube {
  private int x;
  private int y;
  private int z;
  private int visibleFaces = 6;

  public Cube(int x, int y, int z) {
    this.setX(x);
    this.setY(y);
    this.setZ(z);
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Cube) {
      Cube c = (Cube) o;
      return x == c.getX() && y == c.getY() && z == c.getZ();
    }

    return false;
  }

  @Override
  public int hashCode() {
    return 100 * x + 10 * y + z;
  }

  public int distance(Cube c) {
    return Math.abs(x - c.getX()) + Math.abs(y - c.getY()) + Math.abs(z - c.getZ());
  }

  public boolean isAdjacent(Cube c) {
    return this.distance(c) == 1;
  }

  public void decreaseVisibleFaces() {
    visibleFaces--;
  }

  public List<Cube> getNeighbors() {
    List<Cube> neighbors = new ArrayList<>();

    neighbors.add(new Cube(x - 1, y, z));
    neighbors.add(new Cube(x + 1, y, z));
    neighbors.add(new Cube(x, y - 1, z));
    neighbors.add(new Cube(x, y + 1, z));
    neighbors.add(new Cube(x, y, z - 1));
    neighbors.add(new Cube(x, y, z + 1));

    return neighbors;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public int getZ() {
    return z;
  }

  public int getVisibleFaces() {
    return visibleFaces;
  }

  public void setX(int x) {
    this.x = x;
  }

  public void setY(int y) {
    this.y = y;
  }

  public void setZ(int z) {
    this.z = z;
  }
}
