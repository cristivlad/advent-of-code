package com.advent2025;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day3 {

  public static void main(String[] args) throws IOException {
    String filePath = "src/main/resources/advent2025/day3.txt";
    List<String> input = Files.readAllLines(Path.of(filePath));

    List<String> result = getLargest2Digits(input);
    List<String> resultPart2 = getLarges12Digits(input);
    int sum = result.stream().mapToInt(Integer::valueOf).sum();
    long sumPart2 = resultPart2.stream().mapToLong(Long::valueOf).sum();
    System.out.println("Sum: " + sum);
    System.out.println("Sum part2: " + sumPart2);
  }

  private static List<String> getLarges12Digits(List<String> input) {
    List<String> result = new ArrayList<>();
    for (String s : input) {
      int targetLength = 12;
      int toRemove = s.length() - targetLength;
      StringBuilder sb = new StringBuilder(s);

      for (int i = 0; i < toRemove; i++) {
        int indexToRemove = -1;

        for (int j = 0; j < sb.length() - 1; j++) {
          if (sb.charAt(j) < sb.charAt(j + 1)) {
            indexToRemove = j;
            break;
        }
      }
        if (indexToRemove == -1) {
          indexToRemove = sb.length() -1;
        }
        sb.deleteCharAt(indexToRemove);
      }
      result.add(sb.toString());
    }
    return result;
  }

  private static List<String> getLargest2Digits(List<String> input) {
    List<String> result = new ArrayList<>();
    for (String line : input) {
      char[] chars = line.toCharArray();
      int largestNum = 0;
      int secondLargestNum = 0;

      for (int i = 0; i < chars.length; i++) {
        int digit = chars[i] - '0';

        if (digit > largestNum && i < chars.length - 1) {
          secondLargestNum = 0;
          largestNum = digit;
          continue;
        }
        if (digit > secondLargestNum) {
          secondLargestNum = digit;
        }
      }

      String current = "" + largestNum + secondLargestNum;
      result.add(current);
//      System.out.println("IT: " + j + " - First: " + largestNum + ", Second: " + secondLargestNum);
    }
    return result;
  }

}
