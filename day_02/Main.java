package day_02;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

public class Main {

  public static void main(String[] args) {
    List<Integer> player1 = new ArrayList<>();
    List<Integer> player2 = new ArrayList<>();

    try (var bufferedReader = new BufferedReader(new FileReader("input.txt"))) {
      bufferedReader.lines().forEach(s -> {
        String[] players = s.split(" ");
        player1.add(parse(players[0].charAt(0)));
        player2.add(parse(players[1].charAt(0)));
      });
    } catch (IOException e) {
      e.printStackTrace();
    }

    int score = 0;

    for (int i = 0; i < player1.size(); i++) {
      score += player2.get(i);

      if (player1.get(i) == player2.get(i)) {
        score += 3;
      } else if ((3 + player2.get(i) - player1.get(i)) % 3 == 1) {
        score += 6;
      }
    }

    System.out.println("Total score (1): " + score);

    score = 0;

    for (int i = 0; i < player1.size(); i++) {
      if (player2.get(i) == 1) {
        int ch = (player1.get(i) - 1) % 3;
        score += ch == 0 ? 3 : ch;
      } else if (player2.get(i) == 2) {
        score += player1.get(i) + 3;
      } else {
        int ch = (player1.get(i) + 1) % 3;
        score += (ch == 0 ? 3 : ch) + 6;
      }
    }

    System.out.println("Total score (2): " + score);
  }

  private static int parse(char player) {
    switch (player) {
      case 'A':
      case 'X':
        return 1;
      case 'B':
      case 'Y':
        return 2;
      case 'C':
      case 'Z':
        return 3;
    }

    return 0;
  }

}
