package day_19;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {
  static List<Blueprint> blueprints = new ArrayList<>();
  static int globalMaxOreRate;
  static int globalMaxClayRate;
  static int globalMaxObsidianRate;

  public static void main(String[] args) {
    Pattern pattern = Pattern.compile(
        "Blueprint (\\d+): Each ore robot costs (\\d+) ore\\. Each clay robot costs (\\d+) ore\\. Each obsidian robot costs (\\d+) ore and (\\d+) clay\\. Each geode robot costs (\\d+) ore and (\\d+) obsidian\\.");

    try (var bufferedReader = new BufferedReader(new FileReader("input.txt"))) {
      bufferedReader.lines().forEach(line -> {
        Matcher matcher = pattern.matcher(line);
        matcher.find();

        int index = Integer.parseInt(matcher.group(1));
        int oreOreCost = Integer.parseInt(matcher.group(2));
        int clayOreCost = Integer.parseInt(matcher.group(3));
        int obsidianOreCost = Integer.parseInt(matcher.group(4));
        int obsidianClayCost = Integer.parseInt(matcher.group(5));
        int geodeOreCost = Integer.parseInt(matcher.group(6));
        int geodeObsidianCost = Integer.parseInt(matcher.group(7));

        blueprints.add(new Blueprint(index, oreOreCost, clayOreCost, obsidianOreCost,
            obsidianClayCost, geodeOreCost, geodeObsidianCost));
      });
    } catch (IOException e) {
      e.printStackTrace();
    }

    int qualityLevel = 0;

    for (Blueprint b : blueprints) {
      globalMaxOreRate = Math.max(Math.max(b.clayOreCost(), b.obsidianOreCost()), b.geodeOreCost());
      globalMaxClayRate = b.obsidianClayCost();
      globalMaxObsidianRate = b.geodeObsidianCost();
      qualityLevel +=
          b.index() * dfs(b, 24, 1, 0, 0, 0, 0, 0, 0, 0, new HashMap<String, Integer>());
    }

    System.out.println("Largest quality level (1): " + qualityLevel);

    int geodes = 1;

    for (Blueprint b : blueprints.subList(0, 3)) {
      globalMaxOreRate = Math.max(Math.max(b.clayOreCost(), b.obsidianOreCost()), b.geodeOreCost());
      globalMaxClayRate = b.obsidianClayCost();
      globalMaxObsidianRate = b.geodeObsidianCost();
      geodes *= dfs(b, 32, 1, 0, 0, 0, 0, 0, 0, 0, new HashMap<String, Integer>());
    }

    System.out.println("Product of largest number of geodes (2): " + geodes);
  }

  public static String toKey(int timeLeft, int oreRate, int clayRate, int obsidianRate,
      int geodeRate, int ore, int clay, int obsidian, int geode) {
    return new StringBuilder().append(timeLeft).append(oreRate).append(clayRate)
        .append(obsidianRate).append(geodeRate).append(ore).append(clay).append(obsidian)
        .append(geode).toString();
  }

  public static int dfs(Blueprint b, int timeLeft, int oreRate, int clayRate, int obsidianRate,
      int geodeRate, int ore, int clay, int obsidian, int geode, Map<String, Integer> solved) {
    if (timeLeft == 0) {
      return geode;
    }

    String key =
        toKey(timeLeft, oreRate, clayRate, obsidianRate, geodeRate, ore, clay, obsidian, geode);

    if (solved.containsKey(key)) {
      return solved.get(key);
    }

    List<Integer> trySolve = new ArrayList<>();

    if (ore >= b.geodeOreCost() && obsidian >= b.geodeObsidianCost()) {
      int res = dfs(b, timeLeft - 1, oreRate, clayRate, obsidianRate, geodeRate + 1,
          ore - b.geodeOreCost() + oreRate, clay + clayRate,
          obsidian - b.geodeObsidianCost() + obsidianRate, geode + geodeRate, solved);

      solved.put(key, res);
      return res;
    }

    if (ore >= b.oreOreCost() && oreRate < globalMaxOreRate) {
      trySolve.add(dfs(b, timeLeft - 1, oreRate + 1, clayRate, obsidianRate, geodeRate,
          ore - b.oreOreCost() + oreRate, clay + clayRate, obsidian + obsidianRate,
          geode + geodeRate, solved));
    }

    if (ore >= b.clayOreCost() && clayRate < globalMaxClayRate) {
      trySolve.add(dfs(b, timeLeft - 1, oreRate, clayRate + 1, obsidianRate, geodeRate,
          ore - b.clayOreCost() + oreRate, clay + clayRate, obsidian + obsidianRate,
          geode + geodeRate, solved));
    }

    if (ore >= b.obsidianOreCost() && clay >= b.obsidianClayCost()
        && obsidianRate < globalMaxObsidianRate) {
      trySolve.add(dfs(b, timeLeft - 1, oreRate, clayRate, obsidianRate + 1, geodeRate,
          ore - b.obsidianOreCost() + oreRate, clay - b.obsidianClayCost() + clayRate,
          obsidian + obsidianRate, geode + geodeRate, solved));
    }

    int res = dfs(b, timeLeft - 1, oreRate, clayRate, obsidianRate, geodeRate, ore + oreRate,
        clay + clayRate, obsidian + obsidianRate, geode + geodeRate, solved);

    res = Math.max(res, trySolve.stream().mapToInt(Integer::valueOf).max().orElse(0));

    solved.put(key, res);
    return res;
  }
}


record Blueprint(int index, int oreOreCost, int clayOreCost, int obsidianOreCost,
    int obsidianClayCost, int geodeOreCost, int geodeObsidianCost) {
}
