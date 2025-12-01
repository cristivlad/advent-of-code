package com.advent2025;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day1 {

  public static void main(String[] args) throws IOException {
    String filePath = "src/main/resources/advent2025/input_day1.txt";
    List<String> strings = Files.readAllLines(Path.of(filePath));

    int numberOfZeroRotations = getNumberOfZeroRotations(strings);
    System.out.println("Number of zero rotations: " + numberOfZeroRotations);
  }

  private static int getNumberOfZeroRotations(List<String> input) {
    int currentIndex = 50;
    int zeroCounter = 0;
    for (String val : input) {
      if (val.charAt(0) == 'R') {
        int rotation = Integer.parseInt(val.substring(1));
        int[] result = wrapValue(currentIndex, rotation);
        currentIndex = result[0];
        zeroCounter += Math.abs(result[1]);
      } else if (val.charAt(0) == 'L') {
        int rotation = Integer.parseInt(val.substring(1));
        int[] result = wrapValue(currentIndex, -rotation);
        currentIndex = result[0];
        zeroCounter += Math.abs(result[1]);
      }
    }
    return zeroCounter;
  }

  private static int[] wrapValue(int currentVal, int delta) {
    int min = 0;
    int max = 99;
    int range = max - min + 1;
    int newVal = currentVal + delta;
    int shifted = newVal - min;

    int wrappedValue = Math.floorMod(shifted, range) + min;
    int overflows;
    if (delta >= 0) {
      overflows = Math.floorDiv(newVal - min, range) - Math.floorDiv(currentVal - min, range);
    } else {
      overflows = Math.floorDiv(currentVal - min - 1, range) - Math.floorDiv(newVal - min - 1, range);
    }

    return new int[]{wrappedValue, overflows};
  }

}
