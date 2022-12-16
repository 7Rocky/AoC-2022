package day_13;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

public class Main {

  public static void main(String[] args) {
    List<Integer> orderedIndeces = new ArrayList<>();
    List<String> packets = new ArrayList<>();
    String left;
    String right;
    int index = 1;

    try (var bufferedReader = new BufferedReader(new FileReader("input.txt"))) {
      while ((left = bufferedReader.readLine()) != null) {
        right = bufferedReader.readLine();
        packets.add(left);
        packets.add(right);
        bufferedReader.readLine();

        if (isOrdered(left, right) == -1) {
          orderedIndeces.add(index);
        }

        index++;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    System.out.println("Sum of indeces (1): " + orderedIndeces.stream().mapToInt(a -> a).sum());

    packets.add("[[2]]");
    packets.add("[[6]]");

    packets.sort(Main::isOrdered);
    int decoderKey = (packets.indexOf("[[2]]") + 1) * (packets.indexOf("[[6]]") + 1);

    System.out.println("Decoder key (2): " + decoderKey);
  }

  private static List<String> separate(String packet) {
    List<String> lists = new ArrayList<>();

    if (!packet.startsWith("[")) {
      lists.add(packet);
      return lists;
    }

    int openBrackets = 0;
    List<Integer> separators = new ArrayList<>();

    for (int i = 0; i < packet.length(); i++) {
      if (packet.charAt(i) == '[') {
        openBrackets++;
      } else if (packet.charAt(i) == ']') {
        openBrackets--;
      } else if (packet.charAt(i) == ',' && openBrackets == 1) {
        separators.add(i);
      }
    }

    if (separators.isEmpty()) {
      if (packet.substring(1, packet.length() - 1).isEmpty()) {
        return lists;
      }

      lists.add(packet.substring(1, packet.length() - 1));
    } else {
      lists.add(packet.substring(1, separators.get(0)));

      for (int i = 1; i < separators.size(); i++) {
        lists.add(packet.substring(separators.get(i - 1) + 1, separators.get(i)));
      }

      lists.add(packet.substring(separators.get(separators.size() - 1) + 1, packet.length() - 1));
    }

    return lists;
  }

  private static int isOrdered(String left, String right) {
    List<String> leftPackets = separate(left);
    List<String> rightPackets = separate(right);

    int minSize = Math.min(leftPackets.size(), rightPackets.size());

    for (int i = 0; i < minSize; i++) {
      if (!leftPackets.get(i).startsWith("[") && !rightPackets.get(i).startsWith("[")) {
        if (Integer.parseInt(leftPackets.get(i)) != Integer.parseInt(rightPackets.get(i))) {
          return (int) Math
              .signum(Integer.parseInt(leftPackets.get(i)) - Integer.parseInt(rightPackets.get(i)));
        }
      } else {
        int result = isOrdered(leftPackets.get(i), rightPackets.get(i));

        if (result != 0) {
          return result;
        }
      }
    }

    return (int) Math.signum(leftPackets.size() - rightPackets.size());
  }
}
