package day_21;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import java.util.regex.Pattern;


public class Main {
  static Map<String, String> monkeyStrings = new HashMap<>();
  static Map<String, Long> monkeyNumbers = new HashMap<>();

  static String pattern = "(\\d+) [\\+\\-\\*/] (\\d+)";

  public static void setMonkeys() {
    monkeyStrings.clear();
    monkeyNumbers.clear();

    try (var bufferedReader = new BufferedReader(new FileReader("input.txt"))) {
      bufferedReader.lines().forEach(line -> {
        String name = line.split(": ")[0];
        String value = line.split(": ")[1];

        try {
          long number = Long.parseLong(value);
          monkeyNumbers.put(name, number);
        } catch (NumberFormatException e) {
        }

        monkeyStrings.put(name, value);
      });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    Main.setMonkeys();

    while (monkeyNumbers.size() != monkeyStrings.size()) {
      Map<String, Long> newMonkeyNumbers = new HashMap<>();

      for (Entry<String, Long> en : monkeyNumbers.entrySet()) {
        Map<String, String> newMonkeyStrings = new HashMap<>();

        newMonkeyNumbers.put(en.getKey(), en.getValue());

        for (Entry<String, String> es : monkeyStrings.entrySet()) {
          newMonkeyStrings.put(es.getKey(),
              es.getValue().replace(en.getKey(), String.valueOf(en.getValue())));

          if (Pattern.matches(pattern, newMonkeyStrings.get(es.getKey()))) {
            newMonkeyNumbers.put(es.getKey(), Main.evaluate(newMonkeyStrings.get(es.getKey())));
          }
        }

        monkeyStrings = newMonkeyStrings;
      }

      monkeyNumbers = newMonkeyNumbers;
    }

    System.out.println("root (1): " + monkeyNumbers.get("root"));

    Main.setMonkeys();

    monkeyStrings.remove("humn");
    monkeyNumbers.remove("humn");

    monkeyStrings.replace("root", monkeyStrings.get("root").replace("+", "=").replace("-", "=")
        .replace("*", "=").replace("/", "="));

    while (monkeyNumbers.size() != monkeyStrings.size()) {
      int size = monkeyNumbers.size();
      Map<String, Long> newMonkeyNumbers = new HashMap<>();

      for (Entry<String, Long> en : monkeyNumbers.entrySet()) {
        Map<String, String> newMonkeyStrings = new HashMap<>();

        newMonkeyNumbers.put(en.getKey(), en.getValue());

        for (Entry<String, String> es : monkeyStrings.entrySet()) {
          newMonkeyStrings.put(es.getKey(),
              es.getValue().replace(en.getKey(), String.valueOf(en.getValue())));

          if (Pattern.matches(pattern, newMonkeyStrings.get(es.getKey()))) {
            newMonkeyNumbers.put(es.getKey(), Main.evaluate(newMonkeyStrings.get(es.getKey())));
            newMonkeyStrings.put(es.getKey(), String.valueOf(newMonkeyNumbers.get(es.getKey())));
          }
        }

        monkeyStrings = newMonkeyStrings;
      }

      monkeyNumbers = newMonkeyNumbers;

      if (size == monkeyNumbers.size()) {
        break;
      }
    }

    Map<String, String> remainingMonkeyStrings = new HashMap<>();
    Map<String, Long> remainingMonkeyNumbers = new HashMap<>();

    for (Entry<String, String> es : monkeyStrings.entrySet()) {
      if (!monkeyNumbers.containsKey(es.getKey())) {
        remainingMonkeyStrings.put(es.getKey(), es.getValue());
      }
    }

    String target = remainingMonkeyStrings.get("root").split(" = ")[0];
    long value = Long.parseLong(remainingMonkeyStrings.get("root").split(" = ")[1]);

    remainingMonkeyNumbers.put(target, value);

    while (remainingMonkeyNumbers.size() != remainingMonkeyStrings.size()) {
      Map<String, Long> newRemainingMonkeyNumbers = new HashMap<>();

      for (Entry<String, Long> en : remainingMonkeyNumbers.entrySet()) {
        newRemainingMonkeyNumbers.put(en.getKey(), en.getValue());

        String expression = remainingMonkeyStrings.get(en.getKey());

        if (expression.contains(" ")) {
          String a = expression.split(" ")[0];
          String op = expression.split(" ")[1];
          String b = expression.split(" ")[2];

          try {
            Long.parseLong(a);

            if (op.equals("-")) {
              newRemainingMonkeyNumbers.put(b, evaluate(String.valueOf(a + " - " + en.getValue())));
            } else {
              newRemainingMonkeyNumbers.put(b,
                  evaluate(String.valueOf(en.getValue()) + " " + inverse(op) + " " + a));
            }
          } catch (NumberFormatException e) {
            newRemainingMonkeyNumbers.put(a,
                evaluate(String.valueOf(en.getValue()) + " " + inverse(op) + " " + b));
          }
        }
      }

      remainingMonkeyNumbers = newRemainingMonkeyNumbers;
    }

    System.out.println("humn (2): " + remainingMonkeyNumbers.get("humn"));
  }

  public static long evaluate(String str) {
    String[] terms = str.split(" ");

    if (terms[1].equals("+")) {
      return Long.parseLong(terms[0]) + Long.parseLong(terms[2]);
    } else if (terms[1].equals("-")) {
      return Long.parseLong(terms[0]) - Long.parseLong(terms[2]);
    } else if (terms[1].equals("*")) {
      return Long.parseLong(terms[0]) * Long.parseLong(terms[2]);
    } else if (terms[1].equals("/")) {
      return Long.parseLong(terms[0]) / Long.parseLong(terms[2]);
    }

    return 0;
  }

  public static String inverse(String op) {
    if (op.equals("+")) {
      return "-";
    } else if (op.equals("-")) {
      return "+";
    } else if (op.equals("*")) {
      return "/";
    } else if (op.equals("/")) {
      return "*";
    }

    return "";
  }
}
