package day_03;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.TreeSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Main {

  public static void main(String[] args) {
    Collection<Integer> priorities = new ArrayList<>();
    List<String> rucksacks = new ArrayList<>();

    try (var bufferedReader = new BufferedReader(new FileReader("input.txt"))) {
      bufferedReader.lines().forEach(s -> {
        String[] compartments = {s.substring(0, s.length() / 2), s.substring(s.length() / 2)};
        rucksacks.add(s);

        Set<String> compartment0 = new TreeSet<>();
        Set<String> compartment1 = new TreeSet<>();

        compartment0.addAll(Arrays.asList(compartments[0].split("")));
        compartment1.addAll(Arrays.asList(compartments[1].split("")));

        compartment0.removeIf(c -> !compartment1.contains(c));
        priorities.add(getPriority((String) compartment0.toArray()[0]));
      });
    } catch (IOException e) {
      e.printStackTrace();
    }

    System.out.println("Total priority (1): " + priorities.stream().reduce(Integer::sum).orElse(0));

    priorities.clear();

    for (int i = 0; i < rucksacks.size(); i += 3) {
      Set<String> rucksack0 = new TreeSet<>();
      Set<String> rucksack1 = new TreeSet<>();
      Set<String> rucksack2 = new TreeSet<>();

      rucksack0.addAll(Arrays.asList(rucksacks.get(i + 0).split("")));
      rucksack1.addAll(Arrays.asList(rucksacks.get(i + 1).split("")));
      rucksack2.addAll(Arrays.asList(rucksacks.get(i + 2).split("")));

      rucksack0.removeIf(c -> !rucksack1.contains(c) || !rucksack2.contains(c));
      priorities.add(getPriority((String) rucksack0.toArray()[0]));
    }

    System.out.println("Total priority (2): " + priorities.stream().reduce(Integer::sum).orElse(0));
  }

  private static int getPriority(String item) {
    int code = item.codePointAt(0);

    if (code >= 'A' && code <= 'Z') {
      return code - 'A' + 27;
    } else {
      return code - 'a' + 1;
    }
  }

}
