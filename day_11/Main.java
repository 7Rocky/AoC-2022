package day_11;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.stream.Collectors;


public class Main {
  static List<Monkey> monkeys = new ArrayList<>();
  static List<Monkey> monkeysCopy = new ArrayList<>();

  public static void main(String[] args) {
    try (var bufferedReader = new BufferedReader(new FileReader("input.txt"))) {
      while (bufferedReader.readLine() != null) {
        String[] itemsString = bufferedReader.readLine().substring(18).split(", ");
        List<Long> items =
            Arrays.asList(itemsString).stream().map(Long::parseLong).collect(Collectors.toList());
        List<Long> itemsCopy =
            Arrays.asList(itemsString).stream().map(Long::parseLong).collect(Collectors.toList());
        String operation = bufferedReader.readLine().substring(19);
        long divisor = Long.parseLong(bufferedReader.readLine().substring(21));
        int testTrue = Integer.parseInt(bufferedReader.readLine().substring(29));
        int testFalse = Integer.parseInt(bufferedReader.readLine().substring(30));

        monkeys.add(new Monkey(items, operation, divisor, testTrue, testFalse));
        monkeysCopy.add(new Monkey(itemsCopy, operation, divisor, testTrue, testFalse));

        bufferedReader.readLine();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    System.out.println("Monkey business after 20 rouds (1): " + findMonkeyBusiness(20, 3));

    monkeys = monkeysCopy;
    System.out.println("Monkey business after 10000 rounds (2): " + findMonkeyBusiness(10000, 1));
  }

  private static long findMonkeyBusiness(int rounds, int worryDivider) {
    for (int round = 0; round < rounds; round++) {
      monkeys.stream().forEach(m -> m.turn(worryDivider));
    }

    return monkeys.stream().map(Monkey::getInspected).sorted((a, b) -> (int) (b - a)).limit(2)
        .reduce((a, b) -> a * b).orElse(0L);
  }
}


class Monkey {
  private List<Long> items;
  private String operation;
  private long divisor;
  private int testTrue;
  private int testFalse;
  private long inspected = 0;

  public Monkey(List<Long> items, String operation, long divisor, int testTrue, int testFalse) {
    this.items = items;
    this.operation = operation;
    this.divisor = divisor;
    this.testTrue = testTrue;
    this.testFalse = testFalse;
  }

  public void addItem(long item) {
    items.add(item);
  }

  public void turn(long worryDivider) {
    this.inspected += items.size();

    while (!items.isEmpty()) {
      long value = operate(items.remove(0)) / worryDivider;

      if (worryDivider == 1) {
        value %= 9699690;
      }

      Main.monkeys.get(test(value) ? testTrue : testFalse).addItem(value);
    }
  }

  private long operate(long a) {
    long b;

    if (operation.substring(6).equals("old")) {
      b = a;
    } else {
      b = Long.parseLong(operation.substring(6));
    }

    if (operation.contains("+")) {
      return a + b;
    } else if (operation.contains("*")) {
      return a * b;
    }

    return -1L;
  }

  private boolean test(long number) {
    return number % divisor == 0;
  }

  public long getInspected() {
    return inspected;
  }
}
