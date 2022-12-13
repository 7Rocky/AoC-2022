package day_08;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.stream.Collectors;


public class Main {
  static List<List<Integer>> grid = new ArrayList<>();

  public static void main(String[] args) {

    try (var bufferedReader = new BufferedReader(new FileReader("input.txt"))) {
      bufferedReader.lines().forEach(line -> {
        grid.add(Arrays.asList(line.split("")).stream().map(Integer::parseInt)
            .collect(Collectors.toList()));
      });
    } catch (IOException e) {
      e.printStackTrace();
    }

    int visibles = (grid.size() + grid.get(0).size() - 2) * 2;

    for (int i = 1; i < grid.size() - 1; i++) {
      for (int j = 1; j < grid.get(i).size() - 1; j++) {
        if (isVisible(i, j, Direction.TOP) || isVisible(i, j, Direction.RIGHT)
            || isVisible(i, j, Direction.BOTTOM) || isVisible(i, j, Direction.LEFT)) {
          visibles++;
        }
      }
    }

    System.out.println("Visible trees (1): " + visibles);

    int highestScenicScore = 0;

    for (int i = 0; i < grid.size(); i++) {
      for (int j = 0; j < grid.get(i).size(); j++) {
        highestScenicScore = Math.max(highestScenicScore, getScenicScore(i, j));
      }
    }

    System.out.println("Highest scenic score (2): " + highestScenicScore);
  }

  private static boolean isVisible(int x, int y, Direction direction) {
    int height = grid.get(x).get(y);

    switch (direction) {
      case TOP:
        for (int i = 0; i < x; i++) {
          if (grid.get(i).get(y) >= height) {
            return false;
          }
        }
        break;
      case RIGHT:
        for (int j = grid.get(0).size() - 1; j > y; j--) {
          if (grid.get(x).get(j) >= height) {
            return false;
          }
        }
        break;
      case BOTTOM:
        for (int i = grid.size() - 1; i > x; i--) {
          if (grid.get(i).get(y) >= height) {
            return false;
          }
        }
        break;
      case LEFT:
        for (int j = 0; j < y; j++) {
          if (grid.get(x).get(j) >= height) {
            return false;
          }
        }
        break;
    }

    return true;
  }

  private static int getScenicScore(int x, int y) {
    int height = grid.get(x).get(y);
    int scenicScore = 1;

    for (int i = x - 1; i >= -1; i--) {
      if (i == -1) {
        scenicScore *= x;
        break;
      }

      if (grid.get(i).get(y) >= height) {
        scenicScore *= (x - i);
        break;
      }
    }

    for (int j = y + 1; j <= grid.get(0).size(); j++) {
      if (j == grid.get(0).size()) {
        scenicScore *= (grid.get(0).size() - 1 - y);
        break;
      }

      if (grid.get(x).get(j) >= height) {
        scenicScore *= (j - y);
        break;
      }
    }

    for (int i = x + 1; i <= grid.size(); i++) {
      if (i == grid.size()) {
        scenicScore *= (grid.size() - 1 - x);
        break;
      }

      if (grid.get(i).get(y) >= height) {
        scenicScore *= (i - x);
        break;
      }
    }

    for (int j = y - 1; j >= -1; j--) {
      if (j == -1) {
        scenicScore *= y;
        break;
      }

      if (grid.get(x).get(j) >= height) {
        scenicScore *= (y - j);
        break;
      }
    }

    return scenicScore;
  }
}


enum Direction {
  TOP, RIGHT, BOTTOM, LEFT
}
