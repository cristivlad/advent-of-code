package com.advent2025;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day2 {

  public static void main(String[] args) throws IOException {
    String filePath = "src/main/resources/advent2025/day2.txt";
    List<String> strings = Files.readAllLines(Path.of(filePath));

    List<Range> ranges = getRanges(strings);
    List<Long> invalidIds = parseRanges(ranges);
    long total = invalidIds.stream().mapToLong(Long::longValue).sum();
    System.out.println("Total sum of invalid IDs: " + total);


  }

  private static List<Long> parseRanges(List<Range> ranges) {
    List<Long> invalidIds = new ArrayList<>();
    for (Range range : ranges) {
      long currentNumber = range.start();
      while (currentNumber <= range.end()) {
        if (checkNumberV2(currentNumber)) {
          invalidIds.add(currentNumber);
        }
        currentNumber++;
      }
    }
    return invalidIds;
  }

  private static boolean checkNumber(long number) {
//    System.out.println("Checking number: " + number);
    String numStr = String.valueOf(number);
    int size = numStr.length();
    if (size % 2 != 0) {
      return false;
    }
    int halfPoint = size / 2;
    long firstHalf = Long.parseLong(numStr.substring(0, halfPoint));
    long secondHalf = Long.parseLong(numStr.substring(halfPoint));
    return firstHalf == secondHalf;
  }

  private static boolean checkNumberV2(long number) {
    String numStr = String.valueOf(number);
    int size = numStr.length();

    for (int i = 1; i <= size / 2; i++) {
      if (size % i == 0) {
        String part = numStr.substring(0, i);
        if (isRepeatedSubstring(numStr, part)) {
          return true;
        }
      }
    }
    return false;
  }

  private static boolean isRepeatedSubstring(String str, String pattern) {
    for (int i = 0; i < str.length(); i += pattern.length()) {
      if (!str.startsWith(pattern, i)) {
        return false;
      }
    }
    return true;
  }

  private static List<Range> getRanges(List<String> strings) {
    List<Range> ranges = new ArrayList<>();
    for (String s : strings) {
      String[] parts = s.split(",");
      for (String part : parts) {
        String[] bounds = part.split("-");
        long start = Long.parseLong(bounds[0]);
        long end = Long.parseLong(bounds[1]);
        Range range = new Range(start, end);
        ranges.add(range);
      }
    }
    return ranges;
  }

}

record Range(long start, long end) {

}
