package day_17;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.util.stream.Collectors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

  static Set<String> valves = new HashSet<>();
  static Map<String, Integer> flows = new HashMap<>();
  static Map<String, Integer> paths = new HashMap<>();
  static Map<String, Integer> cache = new HashMap<>();

  public static void main(String[] args) {
    var pattern =
        Pattern.compile("Valve (..) has flow rate=(\\d+); tunnels? leads? to valves? (.*)");

    try (var bufferedReader = new BufferedReader(new FileReader("input.txt"))) {
      bufferedReader.lines().forEach(line -> {
        Matcher matcher = pattern.matcher(line);
        matcher.find();
        String name = matcher.group(1);
        int flow = Integer.parseInt(matcher.group(2));

        valves.add(name);

        for (String t : Arrays.asList(matcher.group(3).split(", "))) {
          paths.put(name + t, 1);
        }

        if (flow > 0) {
          flows.put(name, flow);
        }
      });
    } catch (IOException e) {
      e.printStackTrace();
    }

    for (String a : valves) {
      for (String b : valves) {
        paths.putIfAbsent(a + b, Integer.MAX_VALUE / 2);
      }
    }

    for (String i : valves) {
      for (String j : valves) {
        for (String k : valves) {
          paths.put(j + k, Math.min(paths.get(j + k), paths.get(j + i) + paths.get(i + k)));
        }
      }
    }

    System.out.println("Most pressure (1): " + search(30, "AA", flows.keySet(), false));
    System.out
        .println("Most pressure with elephant (2): " + search(26, "AA", flows.keySet(), true));
  }

  public static String toKey(int time, String root, Set<String> visited, boolean elephant) {
    return new StringBuilder().append(time).append(root).append(visited).append(elephant)
        .toString();
  }

  public static int search(int time, String root, Set<String> visited, boolean elephant) {
    String key = toKey(time, root, visited, elephant);

    if (cache.containsKey(key)) {
      return cache.get(key);
    }

    List<Integer> results = new ArrayList<>();

    for (final String v : visited) {
      if (paths.get(root + v) < time) {
        results.add(flows.get(v) * (time - paths.get(root + v) - 1)
            + search(time - paths.get(root + v) - 1, v,
                visited.stream().filter(s -> !s.equals(v)).collect(Collectors.toSet()), elephant));

        if (elephant) {
          results.add(search(26, "AA", visited, false));
        }
      }
    }

    int ret = results.stream().mapToInt(Integer::valueOf).max().orElse(0);

    cache.put(key, ret);

    return ret;
  }
}
