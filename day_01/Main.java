package day_01;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.TreeSet;

public class Main {

  public static void main(String[] args) {
    TreeSet<Integer> calories = new TreeSet<>();
    int calorie = 0;

    try (var bufferedReader = new BufferedReader(new FileReader("input.txt"))) {
      String line = bufferedReader.readLine();

      while (line != null) {
        if (line.isEmpty()) {
          calories.add(calorie);
          calorie = 0;
        } else {
          calorie += Integer.parseInt(line);
        }

        line = bufferedReader.readLine();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    System.out.print("Most calories (1): ");
    System.out.println(calories.last());

    System.out.print("Sum of three most calories (2): ");
    System.out.println(calories.descendingSet().stream().limit(3).reduce(Integer::sum).get());
  }

}
