package day_18;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.List;


public class Main {
  static List<Integer> numbers = new ArrayList<>();
  static List<Element> elements = new ArrayList<>();

  public static void main(String[] args) {
    try (var bufferedReader = new BufferedReader(new FileReader("input.txt"))) {
      numbers = bufferedReader.lines().map(Integer::parseInt).collect(Collectors.toList());
    } catch (IOException e) {
      e.printStackTrace();
    }

    for (int i = 0; i < numbers.size(); i++) {
      elements.add(new Element(numbers.get(i)));
    }

    for (int i = 1; i < elements.size() - 1; i++) {
      elements.get(i).setPrev(elements.get(i - 1));
      elements.get(i).setNext(elements.get(i + 1));
    }

    elements.get(0).setPrev(elements.get(elements.size() - 1));
    elements.get(0).setNext(elements.get(1));
    elements.get(elements.size() - 1).setPrev(elements.get(elements.size() - 2));
    elements.get(elements.size() - 1).setNext(elements.get(0));

    Element zero = elements.stream().filter(e -> e.getValue() == 0).findFirst().orElse(null);

    for (Element e : elements) {
      long amount = e.getValue();

      if (amount == 0) {
        continue;
      }

      Element f = e;
      Element prev = e.getPrev();
      Element next = e.getNext();

      prev.setNext(next);
      next.setPrev(prev);

      if (amount > 0) {
        while (amount > 0) {
          f = f.getNext();
          amount--;
        }
      } else {
        while (amount <= 0) {
          f = f.getPrev();
          amount++;
        }
      }

      prev = f;
      next = f.getNext();

      prev.setNext(e);
      next.setPrev(e);

      f.getNext().setPrev(e);
      f.setNext(e);

      e.setPrev(prev);
      e.setNext(next);
    }

    long result = 0;
    Element ee = zero;

    for (int r = 0; r < 3; r++) {
      for (int i = 0; i < 1000; i++) {
        ee = ee.getNext();
      }

      result += ee.getValue();
    }

    System.out.println("Sum of grove coordinates (1): " + result);

    elements.clear();

    long decryptionKey = 811589153;

    for (int i = 0; i < numbers.size(); i++) {
      elements.add(new Element(numbers.get(i) * decryptionKey));
    }

    for (int i = 1; i < elements.size() - 1; i++) {
      elements.get(i).setPrev(elements.get(i - 1));
      elements.get(i).setNext(elements.get(i + 1));
    }

    elements.get(0).setPrev(elements.get(elements.size() - 1));
    elements.get(0).setNext(elements.get(1));
    elements.get(elements.size() - 1).setPrev(elements.get(elements.size() - 2));
    elements.get(elements.size() - 1).setNext(elements.get(0));

    zero = elements.stream().filter(e -> e.getValue() == 0).findFirst().orElse(null);

    for (int i = 0; i < 10 * elements.size(); i++) {
      Element e = elements.get(i % elements.size());
      long amount = e.getValue() % (elements.size() - 1);

      if (amount == 0) {
        continue;
      }

      Element f = e;
      Element prev = e.getPrev();
      Element next = e.getNext();

      prev.setNext(next);
      next.setPrev(prev);

      if (amount > 0) {
        while (amount > 0) {
          f = f.getNext();
          amount--;
        }
      } else {
        while (amount <= 0) {
          f = f.getPrev();
          amount++;
        }
      }

      prev = f;
      next = f.getNext();

      prev.setNext(e);
      next.setPrev(e);

      f.getNext().setPrev(e);
      f.setNext(e);

      e.setPrev(prev);
      e.setNext(next);
    }

    result = 0;
    ee = zero;

    for (int r = 0; r < 3; r++) {
      for (int i = 0; i < 1000; i++) {
        ee = ee.getNext();
      }

      result += ee.getValue();
    }

    System.out.println("Sum of grove coordinates (2): " + result);
  }
}


public class Element {
  private long value;
  private Element prev;
  private Element next;

  public Element(long value) {
    this.setValue(value);
  }

  public void setValue(long value) {
    this.value = value;
  }

  public void setPrev(Element prev) {
    this.prev = prev;
  }

  public void setNext(Element next) {
    this.next = next;
  }

  public long getValue() {
    return value;
  }

  public Element getPrev() {
    return prev;
  }

  public Element getNext() {
    return next;
  }
}
