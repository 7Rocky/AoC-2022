package day_07;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java.util.stream.Collectors;

public class Main {
  public static final int MAX_DIR_SIZE = 100000;
  public static final int MAX_FS_SIZE = 70000000;
  public static final int MIN_UPDATE_SPACE = 30000000;

  public static void main(String[] args) {
    List<String> lines = null;

    try (var bufferedReader = new BufferedReader(new FileReader("input.txt"))) {
      lines = bufferedReader.lines().collect(Collectors.toList());
    } catch (IOException e) {
      e.printStackTrace();
    }

    var root = new Directory("/", null);
    Directory cwd = root;

    Set<Directory> dirs = new HashSet<>();
    dirs.add(root);

    for (String line : lines) {
      if (line.startsWith("$")) {
        if (line.equals("$ cd /")) {
          cwd = root;
        } else if (line.equals("$ cd ..")) {
          cwd = cwd.getParent();
        } else if (line.startsWith("$ cd ")) {
          var dir = new Directory(line.substring(5), cwd);
          cwd.addNode(dir);
          dirs.add(dir);
          cwd = dir;
        }
      } else if (line.startsWith("dir ")) {
        var dir = new Directory(line.substring(4), cwd);
        cwd.addNode(dir);
        dirs.add(dir);
      } else {
        String[] splitted = line.split(" ");
        var file = new File(splitted[1], cwd, Integer.parseInt(splitted[0]));
        cwd.addNode(file);
      }
    }

    int sizes = dirs.stream().mapToInt(Node::getSize).filter(s -> s < MAX_DIR_SIZE).sum();
    System.out.println("Total '< 100000' directories size (1): " + sizes);

    int minToFree = root.getSize() - (MAX_FS_SIZE - MIN_UPDATE_SPACE);
    int size = dirs.stream().mapToInt(Node::getSize).filter(s -> s >= minToFree).min().orElse(0);
    System.out.println("Deleted directory size (2): " + size);
  }
}


abstract class Node {
  String name;
  Directory parent = null;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Directory getParent() {
    return parent == null ? (Directory) this : parent;
  }

  public void setParent(Directory parent) {
    this.parent = parent;
  }

  public int hashcode() {
    return this.getPath().toCharArray().hashCode();
  }

  public abstract int getSize();

  public abstract String getPath();
}


class Directory extends Node {
  Set<Node> nodes = new HashSet<>();

  public Directory(String name, Directory parent) {
    super.setName(name);
    super.setParent(parent);
  }

  public void addNode(Node node) {
    nodes.add(node);
  }

  public int getSize() {
    return nodes.stream().mapToInt(Node::getSize).sum();
  }

  public String getPath() {
    var path = new StringBuilder();
    Node cwd = this;

    while (!cwd.getName().equals("/")) {
      path.insert(0, cwd.getName() + "/");
      cwd = cwd.getParent();
    }

    return "/" + path;
  }
}


class File extends Node {
  int size;

  public File(String name, Directory parent, int size) {
    super.setName(name);
    super.setParent(parent);
    this.setSize(size);
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public String getPath() {
    var path = new StringBuilder(this.name);
    Node cwd = this.getParent();

    while (!cwd.getName().equals("/")) {
      path.insert(0, cwd.getName() + "/");
      cwd = cwd.getParent();
    }

    return "/" + path;
  }
}
