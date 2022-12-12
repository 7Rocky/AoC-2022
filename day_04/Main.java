package day_04;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

public class Main {

  public static void main(String[] args) {
    List<List<List<Integer>>> pairs = new ArrayList<>();

    try (var bufferedReader = new BufferedReader(new FileReader("input.txt"))) {
      bufferedReader.lines().forEach(s -> {
        List<List<Integer>> intervals = new ArrayList<>();

        for (String intervalString : s.split(",")) {
          List<Integer> interval = new ArrayList<>();
          String[] limits = intervalString.split("-");
          interval.add(Integer.parseInt(limits[0]));
          interval.add(Integer.parseInt(limits[1]));
          intervals.add(interval);
        }

        pairs.add(intervals);
      });
    } catch (IOException e) {
      e.printStackTrace();
    }

    int full_overlaps = 0;
    int overlaps = 0;

    for (List<List<Integer>> intervals : pairs) {
      int a1 = intervals.get(0).get(0);
      int b1 = intervals.get(0).get(1);
      int a2 = intervals.get(1).get(0);
      int b2 = intervals.get(1).get(1);

      if ((a1 <= a2 && b2 <= b1) || (a2 <= a1 && b1 <= b2)) {
        full_overlaps++;
      }

      if ((a1 <= a2 && a2 <= b1) || (a2 <= a1 && a1 <= b2) || (a1 <= b2 && b2 <= b1)
          || (a2 <= b1 && b1 <= b2)) {
        overlaps++;
      }
    }

    System.out.println("Total full overlaps (1): " + full_overlaps);
    System.out.println("Total overlaps (2): " + overlaps);
  }

}
