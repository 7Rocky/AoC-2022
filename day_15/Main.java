package day_15;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

  public static void main(String[] args) {
    List<Sensor> sensors = new ArrayList<>();
    Pattern pattern = Pattern
        .compile("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)");
    int[] borders = {Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE};

    try (var bufferedReader = new BufferedReader(new FileReader("input.txt"))) {
      bufferedReader.lines().forEach(line -> {
        Matcher matcher = pattern.matcher(line);
        matcher.find();

        int sX = Integer.parseInt(matcher.group(1));
        int sY = Integer.parseInt(matcher.group(2));
        int bX = Integer.parseInt(matcher.group(3));
        int bY = Integer.parseInt(matcher.group(4));

        borders[0] = Math.max(sX, borders[0]);
        borders[0] = Math.max(bX, borders[0]);
        borders[1] = Math.min(sX, borders[1]);
        borders[1] = Math.min(bX, borders[1]);
        borders[2] = Math.max(sY, borders[2]);
        borders[2] = Math.max(bY, borders[2]);
        borders[3] = Math.min(sY, borders[3]);
        borders[3] = Math.min(bY, borders[3]);

        sensors.add(new Sensor(sX, sY, bX, bY));
      });
    } catch (IOException e) {
      e.printStackTrace();
    }

    int count = 0;
    boolean visible = false;
    int row = 2000000;

    borders[0] += 10000000;
    borders[1] -= 10000000;
    borders[2] += 10000000;
    borders[3] -= 10000000;

    for (int i = borders[1]; i < borders[0]; i++) {
      for (Sensor sensor : sensors) {
        if (i == sensor.getBX() && row == sensor.getBY()) {
          visible = false;
          break;
        }

        if (sensor.isVisible(i, row)) {
          visible = true;
          break;
        }
      }

      count += visible ? 1 : 0;
      visible = false;
    }

    System.out.println("No beacon positions (1): " + count);

    visible = false;

    for (int y = 0; y < 4000000; y++) {
      for (int x = 0; x < 4000000; x++) {
        for (Sensor sensor : sensors) {
          if (sensor.isVisible(x, y)) {
            visible = true;
            x = sensor.getLastVisible(x, y);
            break;
          }
        }

        if (!visible) {
          System.out.println("Tuning frequency (2): " + (4000000L * (long) x + (long) y));
        }

        visible = false;
      }
    }
  }
}


class Sensor {
  private int sX;
  private int sY;
  private int bX;
  private int bY;
  private int distance;

  public Sensor(int sX, int sY, int bX, int bY) {
    this.sX = sX;
    this.sY = sY;
    this.bX = bX;
    this.bY = bY;
    this.distance = getDistance(bX, bY);
  }

  public boolean isVisible(int x, int y) {
    return getDistance(x, y) <= distance;
  }

  public int getLastVisible(int x, int y) {
    if (x > sX) {
      return x + this.distance - getDistance(x, y);
    }

    return x + 2 * (sX - x);
  }

  private int getDistance(int x, int y) {
    return Math.abs(sX - x) + Math.abs(sY - y);
  }

  public int getBX() {
    return bX;
  }

  public int getBY() {
    return bY;
  }
}
