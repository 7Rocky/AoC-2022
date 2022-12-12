package day_06;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public class Main {

  public static void main(String[] args) {
    String buffer = "";

    try (var bufferedReader = new BufferedReader(new FileReader("input.txt"))) {
      buffer = bufferedReader.readLine();
    } catch (IOException e) {
      e.printStackTrace();
    }

    System.out.println("Number of characters processed (1): " + findMarker(buffer, 4));
    System.out.println("Number of characters processed (2): " + findMarker(buffer, 14));
  }

  private static int findMarker(String buffer, int size) {
    Set<String> marker = new TreeSet<>();

    for (int i = size; i < buffer.length(); i++) {
      marker.clear();
      marker.addAll(Arrays.asList(buffer.substring(i - size, i).split("")));

      if (marker.size() == size) {
        return i;
      }
    }

    return -1;
  }

}
