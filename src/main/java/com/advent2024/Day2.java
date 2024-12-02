package com.advent2024;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * --- Day 2: Red-Nosed Reports --- Fortunately, the first location The Historians want to search isn't a long walk from
 * the Chief Historian's office.
 * <p>
 * While the Red-Nosed Reindeer nuclear fusion/fission plant appears to contain no sign of the Chief Historian, the
 * engineers there run up to you as soon as they see you. Apparently, they still talk about the time Rudolph was saved
 * through molecular synthesis from a single electron.
 * <p>
 * They're quick to add that - since you're already here - they'd really appreciate your help analyzing some unusual
 * data from the Red-Nosed reactor. You turn to check if The Historians are waiting for you, but they seem to have
 * already divided into groups that are currently searching every corner of the facility. You offer to help with the
 * unusual data.
 * <p>
 * The unusual data (your puzzle input) consists of many reports, one report per line. Each report is a list of numbers
 * called levels that are separated by spaces. For example:
 * <p>
 * 7 6 4 2 1 1 2 7 8 9 9 7 6 2 1 1 3 2 4 5 8 6 4 4 1 1 3 6 7 9 This example data contains six reports each containing
 * five levels.
 * <p>
 * The engineers are trying to figure out which reports are safe. The Red-Nosed reactor safety systems can only tolerate
 * levels that are either gradually increasing or gradually decreasing. So, a report only counts as safe if both of the
 * following are true:
 * <p>
 * The levels are either all increasing or all decreasing. Any two adjacent levels differ by at least one and at most
 * three. In the example above, the reports can be found safe or unsafe by checking those rules:
 * <p>
 * 7 6 4 2 1: Safe because the levels are all decreasing by 1 or 2. 1 2 7 8 9: Unsafe because 2 7 is an increase of 5. 9
 * 7 6 2 1: Unsafe because 6 2 is a decrease of 4. 1 3 2 4 5: Unsafe because 1 3 is increasing but 3 2 is decreasing. 8
 * 6 4 4 1: Unsafe because 4 4 is neither an increase or a decrease. 1 3 6 7 9: Safe because the levels are all
 * increasing by 1, 2, or 3. So, in this example, 2 reports are safe.
 * <p>
 * Analyze the unusual data from the engineers. How many reports are safe? --- Part Two --- The engineers are surprised
 * by the low number of safe reports until they realize they forgot to tell you about the Problem Dampener.
 * <p>
 * The Problem Dampener is a reactor-mounted module that lets the reactor safety systems tolerate a single bad level in
 * what would otherwise be a safe report. It's like the bad level never happened!
 * <p>
 * Now, the same rules apply as before, except if removing a single level from an unsafe report would make it safe, the
 * report instead counts as safe.
 * <p>
 * More of the above example's reports are now safe:
 * <p>
 * 7 6 4 2 1: Safe without removing any level. 1 2 7 8 9: Unsafe regardless of which level is removed. 9 7 6 2 1: Unsafe
 * regardless of which level is removed. 1 3 2 4 5: Safe by removing the second level, 3. 8 6 4 4 1: Safe by removing
 * the third level, 4. 1 3 6 7 9: Safe without removing any level. Thanks to the Problem Dampener, 4 reports are
 * actually safe!
 * <p>
 * Update your analysis by handling situations where the Problem Dampener can remove a single level from unsafe reports.
 * How many reports are now safe?
 */
public class Day2 {

  public static void main(String[] args) {
    String filePath = "src/main/resources/input_day2.txt";

    ReportAnalyzer reportAnalyzer = new ReportAnalyzer(filePath);
    long analyze = reportAnalyzer.analyze();
    System.out.println("Safe reports: " + analyze);
    long l = reportAnalyzer.analyzeWithPossibleRemoval();
    System.out.println("Safe reports with possible removal: " + l);
  }

  private static class ReportAnalyzer {

    private final String filePath;

    public ReportAnalyzer(String filePath) {
      this.filePath = filePath;
    }

    public long analyze() {
      List<List<Integer>> allData = loadData();

      return allData.stream()
          .filter(Day2.ReportAnalyzer::isValidLine)
          .count();
    }

    public long analyzeWithPossibleRemoval() {
      List<List<Integer>> allData = loadData();

      return allData.stream()
          .filter(Day2.ReportAnalyzer::isValidLineWithRemoval)
          .count();
    }

    private List<List<Integer>> loadData() {
      try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
        return lines
            .map(Day2.ReportAnalyzer::parseLine)
            .collect(Collectors.toList());
      } catch (Exception e) {
        e.printStackTrace();
        return new ArrayList<>();
      }
    }

    private static List<Integer> parseLine(String line) {
      return Stream.of(line.split("\\s+"))
          .map(Integer::parseInt)
          .collect(Collectors.toList());
    }

    private static boolean isValidLine(List<Integer> data) {
      if (data.isEmpty()) {
        return false;
      }

      int initialDIff = data.get(1) - data.get(0);
      boolean increasing = initialDIff > 0;
      boolean decreasing = initialDIff < 0;

      for (int i = 0; i < data.size() - 1; i++) {
        int diff = data.get(i + 1) - data.get(i);
        if (Math.abs(diff) < 1 || Math.abs(diff) > 3) {
          return false;
        }

        if (increasing && diff <= 0) {
          return false;
        }
        if (decreasing && diff >= 0) {
          return false;
        }
      }
      return true;
    }

    public static boolean isValidLineWithRemoval(List<Integer> data) {
      if (isValidLine(data)) {
        return true;
      }
      return IntStream.range(0, data.size())
          .anyMatch(i -> isValidLine(removeAt(data, i)));

//      for (int i = 0; i < data.size(); i++) {
//        List<Integer> copy = new ArrayList<>(data);
//        copy.remove(i);
//        if (isValidLine(copy)) {
//          return true;
//        }
//      }
//      return false;
    }

    private static List<Integer> removeAt(List<Integer> list, int index) {
      List<Integer> result = new ArrayList<>(list);
      result.remove(index);
      return result;
    }
  }
}
