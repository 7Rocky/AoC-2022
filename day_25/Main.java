package day_25;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Main {
  static List<String> snafuNumbers = new ArrayList<>();
  static Map<Character, Long> s2d = new HashMap<>();
  static Map<Long, Character> d2s = new HashMap<>();

  public static void main(String[] args) {
    try (var bufferedReader = new BufferedReader(new FileReader("input.txt"))) {
      bufferedReader.lines().forEach(snafuNumbers::add);
    } catch (IOException e) {
      e.printStackTrace();
    }

    s2d.put('0', 0L);
    s2d.put('1', 1L);
    s2d.put('2', 2L);
    s2d.put('-', -1L);
    s2d.put('=', -2L);

    d2s.put(0L, '0');
    d2s.put(1L, '1');
    d2s.put(2L, '2');
    d2s.put(-1L, '-');
    d2s.put(-2L, '=');

    System.out.println("SNAFU number (1): "
        + decimalToSnafu(snafuNumbers.stream().mapToLong(Main::snafuToDecimal).sum()));
  }

  public static long snafuToDecimal(String s) {
    long n = 0;
    int length = s.length();

    for (int i = 0; i < length; i++) {
      n += ((long) Math.pow(5, length - i - 1)) * s2d.get(s.charAt(i));
    }

    return n;
  }

  public static String decimalToSnafu(long n) {
    var sB = new StringBuilder();

    while (n != 0) {
      long x = n % 5;
      x -= x > 2 ? 5 : 0;
      sB.append(d2s.get(x));
      n = (n - x) / 5;
    }

    return sB.reverse().toString();
  }
}
