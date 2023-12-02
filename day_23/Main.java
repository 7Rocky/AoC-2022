package day_23;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class Main {
  static List<Elf> elves = new ArrayList<>();

  public static void main(String[] args) {
    try (var bufferedReader = new BufferedReader(new FileReader("input.txt"))) {
      int y = 0;
      String line;

      while ((line = bufferedReader.readLine()) != null) {
        for (int x = 0; x < line.length(); x++) {
          if (line.charAt(x) == '#') {
            elves.add(new Elf(x, y));
          }
        }

        y++;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    int r = 0;

    do {
      final int round = r;
      elves.stream().forEach(e -> e.firstHalf(round));
      elves.stream().forEach(Elf::secondHalf);
      r++;

      if (r == 10) {
        System.out.println("Empty tiles (1): " + countTiles());
      }
    } while (!elves.stream().map(Elf::getNext).allMatch(s -> s.equals("")));

    System.out.println("Rounds (2): " + r);
  }

  public static int countTiles() {
    int maxX = Main.elves.stream().mapToInt(Elf::getX).max().orElse(0);
    int minX = Main.elves.stream().mapToInt(Elf::getX).min().orElse(0);
    int maxY = Main.elves.stream().mapToInt(Elf::getY).max().orElse(0);
    int minY = Main.elves.stream().mapToInt(Elf::getY).min().orElse(0);

    return (maxX - minX + 1) * (maxY - minY + 1) - Main.elves.size();
  }
}


class Elf {
  private int x;
  private int y;
  private String next = "";

  static char[] order = {'N', 'S', 'W', 'E'};

  public Elf(int x, int y) {
    this.setX(x);
    this.setY(y);
  }

  @Override
  public String toString() {
    return new StringBuilder().append(x).append(',').append(y).toString();
  }

  public void firstHalf(int round) {
    this.setNext("");
    int allEmpty = 0;
    Set<String> positionsSet = Main.elves.stream().map(Elf::toString).collect(Collectors.toSet());

    for (int i = round; i < round + order.length; i++) {
      final String[] targets = new String[3];

      switch (order[i % order.length]) {
        case 'N':
          targets[0] = new Elf(x - 1, y - 1).toString();
          targets[1] = new Elf(x + 0, y - 1).toString();
          targets[2] = new Elf(x + 1, y - 1).toString();
          break;
        case 'S':
          targets[0] = new Elf(x - 1, y + 1).toString();
          targets[1] = new Elf(x + 0, y + 1).toString();
          targets[2] = new Elf(x + 1, y + 1).toString();
          break;
        case 'W':
          targets[0] = new Elf(x - 1, y - 1).toString();
          targets[1] = new Elf(x - 1, y + 0).toString();
          targets[2] = new Elf(x - 1, y + 1).toString();
          break;
        case 'E':
          targets[0] = new Elf(x + 1, y - 1).toString();
          targets[1] = new Elf(x + 1, y + 0).toString();
          targets[2] = new Elf(x + 1, y + 1).toString();
          break;
      }

      if (!(positionsSet.contains(targets[0]) || positionsSet.contains(targets[1])
          || positionsSet.contains(targets[2]))) {
        if (next.equals("")) {
          this.setNext(targets[1]);
        }

        allEmpty++;
      }
    }

    if (allEmpty == 4) {
      this.setNext("");
    }
  }

  public void secondHalf() {
    if (!next.equals("")) {
      if (Main.elves.stream().map(Elf::getNext).filter(s -> s.equals(next)).count() == 1) {
        this.setX(Integer.parseInt(next.split(",")[0]));
        this.setY(Integer.parseInt(next.split(",")[1]));
      }
    }
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public String getNext() {
    return next;
  }

  public void setX(int x) {
    this.x = x;
  }

  public void setY(int y) {
    this.y = y;
  }

  public void setNext(String next) {
    this.next = next;
  }
}
