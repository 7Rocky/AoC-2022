package day_22;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;


public class Main {
  static List<List<Character>> map = new ArrayList<>();
  static String steps;
  static int[] position = new int[] {0, 0};
  static int[] direction = new int[] {0, 0};

  public static void setMap() {
    map.clear();

    try (var bufferedReader = new BufferedReader(new FileReader("input.txt"))) {
      String line;

      while ((line = bufferedReader.readLine()) != null) {
        List<Character> row = new ArrayList<>();

        for (int i = 0; i < line.length(); i++) {
          row.add(line.charAt(i));
        }

        row.add(0, ' ');
        row.add(' ');

        map.add(row);

        if (line.equals("")) {
          break;
        }
      }

      int maxSize = map.stream().mapToInt(l -> l.size()).max().orElse(0);

      map.add(0, new ArrayList<Character>());
      map.add(new ArrayList<Character>());

      for (int i = 0; i < map.size(); i++) {
        List<Character> row = map.get(i);

        for (int j = row.size(); j < maxSize; j++) {
          row.add(' ');
        }
      }

      steps = bufferedReader.readLine();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    Main.setMap();

    position[0] = map.get(1).indexOf('.');
    position[1] = 1;
    direction[0] = 1;
    direction[1] = 0;

    Main.traverseFlatMap();

    int result = 1000 * (position[1]) + 4 * (position[0]) + facing();
    System.out.println("Password (1): " + result);

    Main.setMap();

    position[0] = map.get(1).indexOf('.');
    position[1] = 1;
    direction[0] = 1;
    direction[1] = 0;

    Main.traverseCubeMap();

    result = 1000 * (position[1]) + 4 * (position[0]) + facing();
    System.out.println("Password (2): " + result);
  }

  public static int facing() {
    int facing = 0;

    if (direction[0] == 0 && direction[1] == 1) {
      facing = 1;
    } else if (direction[0] == -1 && direction[1] == 0) {
      facing = 2;
    } else if (direction[0] == 0 && direction[1] == -1) {
      facing = 3;
    }

    return facing;
  }

  public static void traverseFlatMap() {
    while (steps.length() != 0) {
      if (steps.charAt(0) == 'R') {
        steps = steps.substring(1);

        if (direction[0] == 0 && direction[1] == 1) {
          direction[0] = -1;
          direction[1] = 0;
        } else if (direction[0] == 0 && direction[1] == -1) {
          direction[0] = 1;
          direction[1] = 0;
        } else if (direction[0] == 1 && direction[1] == 0) {
          direction[0] = 0;
          direction[1] = 1;
        } else if (direction[0] == -1 && direction[1] == 0) {
          direction[0] = 0;
          direction[1] = -1;
        }
      } else if (steps.charAt(0) == 'L') {
        steps = steps.substring(1);

        if (direction[0] == 0 && direction[1] == 1) {
          direction[0] = 1;
          direction[1] = 0;
        } else if (direction[0] == 0 && direction[1] == -1) {
          direction[0] = -1;
          direction[1] = 0;
        } else if (direction[0] == 1 && direction[1] == 0) {
          direction[0] = 0;
          direction[1] = -1;
        } else if (direction[0] == -1 && direction[1] == 0) {
          direction[0] = 0;
          direction[1] = 1;
        }
      } else {
        int nextR = steps.indexOf('R');
        int nextL = steps.indexOf('L');
        int amount = 0;

        if (nextR == -1 && nextL == -1) {
          amount = Integer.parseInt(steps);
          steps = "";
        } else if (nextR == -1 || nextL < nextR) {
          amount = Integer.parseInt(steps.substring(0, nextL));
          steps = steps.substring(nextL);
        } else if (nextL == -1 || nextR < nextL) {
          amount = Integer.parseInt(steps.substring(0, nextR));
          steps = steps.substring(nextR);
        }

        for (int s = 0; s < amount; s++) {
          char tile = map.get(position[1] + direction[1]).get(position[0] + direction[0]);

          if (tile == ' ') {
            if (direction[0] == 0 && direction[1] == 1) {
              int j;

              for (j = position[1] - 1; j >= 0; j--) {
                if (map.get(j).get(position[0]) == ' ') {
                  break;
                }
              }

              if (map.get(j + 1).get(position[0]) == '.') {
                position[1] = j + 1;
              } else if (map.get(j + 1).get(position[0]) == '#') {
                break;
              }
            } else if (direction[0] == 0 && direction[1] == -1) {
              int j;

              for (j = position[1] + 1; j < map.size(); j++) {
                if (map.get(j).get(position[0]) == ' ') {
                  break;
                }
              }

              if (map.get(j - 1).get(position[0]) == '.') {
                position[1] = j - 1;
              } else if (map.get(j - 1).get(position[0]) == '#') {
                break;
              }
            } else if (direction[0] == 1 && direction[1] == 0) {
              int i;

              for (i = position[0] - 1; i >= 0; i--) {
                if (map.get(position[1]).get(i) == ' ') {
                  break;
                }
              }

              if (map.get(position[1]).get(i + 1) == '.') {
                position[0] = i + 1;
              } else if (map.get(position[1]).get(i + 1) == '#') {
                break;
              }
            } else if (direction[0] == -1 && direction[1] == 0) {
              int i;

              for (i = position[0] + 1; i < map.get(0).size(); i++) {
                if (map.get(position[1]).get(i) == ' ') {
                  break;
                }
              }

              if (map.get(position[1]).get(i - 1) == '.') {
                position[0] = i - 1;
              } else if (map.get(position[1]).get(i - 1) == '#') {
                break;
              }
            }
          } else if (tile == '.') {
            position[0] += direction[0];
            position[1] += direction[1];
          } else if (tile == '#') {
            break;
          }
        }
      }
    }
  }

  public static void traverseCubeMap() {
    while (steps.length() != 0) {
      if (steps.charAt(0) == 'R') {
        steps = steps.substring(1);

        if (direction[0] == 0 && direction[1] == 1) {
          direction[0] = -1;
          direction[1] = 0;
        } else if (direction[0] == 0 && direction[1] == -1) {
          direction[0] = 1;
          direction[1] = 0;
        } else if (direction[0] == 1 && direction[1] == 0) {
          direction[0] = 0;
          direction[1] = 1;
        } else if (direction[0] == -1 && direction[1] == 0) {
          direction[0] = 0;
          direction[1] = -1;
        }
      } else if (steps.charAt(0) == 'L') {
        steps = steps.substring(1);

        if (direction[0] == 0 && direction[1] == 1) {
          direction[0] = 1;
          direction[1] = 0;
        } else if (direction[0] == 0 && direction[1] == -1) {
          direction[0] = -1;
          direction[1] = 0;
        } else if (direction[0] == 1 && direction[1] == 0) {
          direction[0] = 0;
          direction[1] = -1;
        } else if (direction[0] == -1 && direction[1] == 0) {
          direction[0] = 0;
          direction[1] = 1;
        }
      } else {
        int nextR = steps.indexOf('R');
        int nextL = steps.indexOf('L');
        int amount = 0;

        if (nextR == -1 && nextL == -1) {
          amount = Integer.parseInt(steps);
          steps = "";
        } else if (nextR == -1 || nextL < nextR) {
          amount = Integer.parseInt(steps.substring(0, nextL));
          steps = steps.substring(nextL);
        } else if (nextL == -1 || nextR < nextL) {
          amount = Integer.parseInt(steps.substring(0, nextR));
          steps = steps.substring(nextR);
        }

        for (int s = 0; s < amount; s++) {
          char tile = map.get(position[1] + direction[1]).get(position[0] + direction[0]);

          if (tile == ' ') {
            if (direction[0] == 0 && direction[1] == 1) {
              if (position[1] == 200 && position[0] > 0 && position[0] <= 50) {
                if (map.get(1).get(position[0] + 100) != '#') {
                  position[0] = position[0] + 100;
                  position[1] = 1;
                } else {
                  break;
                }
              } else if (position[1] == 150 && position[0] > 50 && position[0] <= 100) {
                if (map.get(position[0] + 100).get(50) != '#') {
                  direction[0] = -1;
                  direction[1] = 0;
                  position[1] = position[0] + 100;
                  position[0] = 50;
                } else {
                  break;
                }
              } else if (position[1] == 50 && position[0] > 100 && position[0] <= 150) {
                if (map.get(position[0] - 50).get(100) != '#') {
                  direction[0] = -1;
                  direction[1] = 0;
                  position[1] = position[0] - 50;
                  position[0] = 100;
                } else {
                  break;
                }
              }
            } else if (direction[0] == 0 && direction[1] == -1) {
              if (position[1] == 1 && position[0] > 50 && position[0] <= 100) {
                if (map.get(position[0] + 100).get(1) != '#') {
                  direction[0] = 1;
                  direction[1] = 0;
                  position[1] = position[0] + 100;
                  position[0] = 1;
                } else {
                  break;
                }
              } else if (position[1] == 1 && position[0] > 100 && position[0] <= 150) {
                if (map.get(200).get(position[0] - 100) != '#') {
                  direction[0] = 0;
                  direction[1] = -1;
                  position[1] = 200;
                  position[0] = position[0] - 100;
                } else {
                  break;
                }
              } else if (position[1] == 101 && position[0] > 0 && position[0] <= 50) {
                if (map.get(position[0] + 50).get(51) != '#') {
                  direction[0] = 1;
                  direction[1] = 0;
                  position[1] = position[0] + 50;
                  position[0] = 51;
                } else {
                  break;
                }
              }
            } else if (direction[0] == 1 && direction[1] == 0) {
              if (position[0] == 150 && position[1] > 0 && position[1] <= 50) {
                if (map.get((51 - position[1]) + 100).get(100) != '#') {
                  direction[0] = -1;
                  direction[1] = 0;
                  position[1] = (51 - position[1]) + 100;
                  position[0] = 100;
                } else {
                  break;
                }
              } else if (position[0] == 100 && position[1] > 50 && position[1] <= 100) {
                if (map.get(50).get(position[1] + 50) != '#') {
                  direction[0] = 0;
                  direction[1] = -1;
                  position[0] = position[1] + 50;
                  position[1] = 50;
                } else {
                  break;
                }
              } else if (position[0] == 100 && position[1] > 100 && position[1] <= 150) {
                if (map.get(151 - position[1]).get(150) != '#') {
                  direction[0] = -1;
                  direction[1] = 0;
                  position[1] = 151 - position[1];
                  position[0] = 150;
                } else {
                  break;
                }
              } else if (position[0] == 50 && position[1] > 150 && position[1] <= 200) {
                if (map.get(150).get(position[1] - 100) != '#') {
                  direction[0] = 0;
                  direction[1] = -1;
                  position[0] = position[1] - 100;
                  position[1] = 150;
                } else {
                  break;
                }
              }
            } else if (direction[0] == -1 && direction[1] == 0) {
              if (position[0] == 51 && position[1] > 0 && position[1] <= 50) {
                if (map.get((51 - position[1]) + 100).get(1) != '#') {
                  direction[0] = 1;
                  direction[1] = 0;
                  position[1] = (51 - position[1]) + 100;
                  position[0] = 1;
                } else {
                  break;
                }
              } else if (position[0] == 51 && position[1] > 50 && position[1] <= 100) {
                if (map.get(101).get(position[1] - 50) != '#') {
                  direction[0] = 0;
                  direction[1] = 1;
                  position[0] = position[1] - 50;
                  position[1] = 101;
                } else {
                  break;
                }
              } else if (position[0] == 1 && position[1] > 100 && position[1] <= 150) {
                if (map.get(151 - position[1]).get(51) != '#') {
                  direction[0] = 1;
                  direction[1] = 0;
                  position[1] = 151 - position[1];
                  position[0] = 51;
                } else {
                  break;
                }
              } else if (position[0] == 1 && position[1] > 150 && position[1] <= 200) {
                if (map.get(1).get(position[1] - 100) != '#') {
                  direction[0] = 0;
                  direction[1] = 1;
                  position[0] = position[1] - 100;
                  position[1] = 1;
                } else {
                  break;
                }
              }
            }

            if (direction[0] == 0 && direction[1] == 1) {
              int j;

              for (j = position[1] - 1; j >= 0; j--) {
                if (map.get(j).get(position[0]) == ' ') {
                  break;
                }
              }

              if (map.get(j + 1).get(position[0]) == '.') {
                position[1] = j + 1;
              } else if (map.get(j + 1).get(position[0]) == '#') {
                break;
              }
            } else if (direction[0] == 0 && direction[1] == -1) {
              int j;

              for (j = position[1] + 1; j < map.size(); j++) {
                if (map.get(j).get(position[0]) == ' ') {
                  break;
                }
              }

              if (map.get(j - 1).get(position[0]) == '.') {
                position[1] = j - 1;
              } else if (map.get(j - 1).get(position[0]) == '#') {
                break;
              }
            } else if (direction[0] == 1 && direction[1] == 0) {
              int i;

              for (i = position[0] - 1; i >= 0; i--) {
                if (map.get(position[1]).get(i) == ' ') {
                  break;
                }
              }

              if (map.get(position[1]).get(i + 1) == '.') {
                position[0] = i + 1;
              } else if (map.get(position[1]).get(i + 1) == '#') {
                break;
              }
            } else if (direction[0] == -1 && direction[1] == 0) {
              int i;

              for (i = position[0] + 1; i < map.get(0).size(); i++) {
                if (map.get(position[1]).get(i) == ' ') {
                  break;
                }
              }

              if (map.get(position[1]).get(i - 1) == '.') {
                position[0] = i - 1;
              } else if (map.get(position[1]).get(i - 1) == '#') {
                break;
              }
            }
          } else if (tile == '.') {
            position[0] += direction[0];
            position[1] += direction[1];
          } else if (tile == '#') {
            break;
          }
        }
      }
    }
  }

}
