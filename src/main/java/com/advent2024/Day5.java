package com.advent2024;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * --- Day 5: Print Queue ---
 * Satisfied with their search on Ceres, the squadron of scholars suggests subsequently scanning the stationery stacks of sub-basement 17.
 * <p>
 * The North Pole printing department is busier than ever this close to Christmas, and while The Historians continue their search of this historically significant facility, an Elf operating a very familiar printer beckons you over.
 * <p>
 * The Elf must recognize you, because they waste no time explaining that the new sleigh launch safety manual updates won't print correctly. Failure to update the safety manuals would be dire indeed, so you offer your services.
 * <p>
 * Safety protocols clearly indicate that new pages for the safety manuals must be printed in a very specific order. The notation X|Y means that if both page number X and page number Y are to be produced as part of an update, page number X must be printed at some point before page number Y.
 * <p>
 * The Elf has for you both the page ordering rules and the pages to produce in each update (your puzzle input), but can't figure out whether each update has the pages in the right order.
 * <p>
 * For example:
 * <p>
 * 47|53
 * 97|13
 * 97|61
 * 97|47
 * 75|29
 * 61|13
 * 75|53
 * 29|13
 * 97|29
 * 53|29
 * 61|53
 * 97|53
 * 61|29
 * 47|13
 * 75|47
 * 97|75
 * 47|61
 * 75|61
 * 47|29
 * 75|13
 * 53|13
 * <p>
 * 75,47,61,53,29
 * 97,61,53,29,13
 * 75,29,13
 * 75,97,47,61,53
 * 61,13,29
 * 97,13,75,29,47
 * The first section specifies the page ordering rules, one per line. The first rule, 47|53, means that if an update includes both page number 47 and page number 53, then page number 47 must be printed at some point before page number 53. (47 doesn't necessarily need to be immediately before 53; other pages are allowed to be between them.)
 * <p>
 * The second section specifies the page numbers of each update. Because most safety manuals are different, the pages needed in the updates are different too. The first update, 75,47,61,53,29, means that the update consists of page numbers 75, 47, 61, 53, and 29.
 * <p>
 * To get the printers going as soon as possible, start by identifying which updates are already in the right order.
 * <p>
 * In the above example, the first update (75,47,61,53,29) is in the right order:
 * <p>
 * 75 is correctly first because there are rules that put each other page after it: 75|47, 75|61, 75|53, and 75|29.
 * 47 is correctly second because 75 must be before it (75|47) and every other page must be after it according to 47|61, 47|53, and 47|29.
 * 61 is correctly in the middle because 75 and 47 are before it (75|61 and 47|61) and 53 and 29 are after it (61|53 and 61|29).
 * 53 is correctly fourth because it is before page number 29 (53|29).
 * 29 is the only page left and so is correctly last.
 * Because the first update does not include some page numbers, the ordering rules involving those missing page numbers are ignored.
 * <p>
 * The second and third updates are also in the correct order according to the rules. Like the first update, they also do not include every page number, and so only some of the ordering rules apply - within each update, the ordering rules that involve missing page numbers are not used.
 * <p>
 * The fourth update, 75,97,47,61,53, is not in the correct order: it would print 75 before 97, which violates the rule 97|75.
 * <p>
 * The fifth update, 61,13,29, is also not in the correct order, since it breaks the rule 29|13.
 * <p>
 * The last update, 97,13,75,29,47, is not in the correct order due to breaking several rules.
 * <p>
 * For some reason, the Elves also need to know the middle page number of each update being printed. Because you are currently only printing the correctly-ordered updates, you will need to find the middle page number of each correctly-ordered update. In the above example, the correctly-ordered updates are:
 * <p>
 * 75,47,61,53,29
 * 97,61,53,29,13
 * 75,29,13
 * These have middle page numbers of 61, 53, and 29 respectively. Adding these page numbers together gives 143.
 * <p>
 * Of course, you'll need to be careful: the actual list of page ordering rules is bigger and more complicated than the above example.
 * <p>
 * Determine which updates are already in the correct order. What do you get if you add up the middle page number from those correctly-ordered updates?
 * --- Part Two ---
 * While the Elves get to work printing the correctly-ordered updates, you have a little time to fix the rest of them.
 * <p>
 * For each of the incorrectly-ordered updates, use the page ordering rules to put the page numbers in the right order. For the above example, here are the three incorrectly-ordered updates and their correct orderings:
 * <p>
 * 75,97,47,61,53 becomes 97,75,47,61,53.
 * 61,13,29 becomes 61,29,13.
 * 97,13,75,29,47 becomes 97,75,47,29,13.
 * After taking only the incorrectly-ordered updates and ordering them correctly, their middle page numbers are 47, 29, and 47. Adding these together produces 123.
 * <p>
 * Find the updates which are not in the correct order. What do you get if you add up the middle page numbers after correctly ordering just those updates?
 */
public class Day5 {

  public static void main(String[] args) {
    String filePath = "src/main/resources/input_day5.txt";
    PrintQueueProcessor printQueueProcessor = new PrintQueueProcessor();
    printQueueProcessor.readInput(filePath);
    long total = printQueueProcessor.calculateTotal();
    System.out.println("Total: " + total);
    long reorderedMiddleSum = printQueueProcessor.calculateReorderedMiddleSum();
    System.out.println("Reordered middle sum: " + reorderedMiddleSum);
  }

  public static class PrintQueueProcessor {
    private List<int[]> rules = new ArrayList<>();
    private List<int[]> updates = new ArrayList<>();
    private Map<Integer, List<Integer>> graph;
    private Map<Integer, Integer> inDegree;

    public void readInput(String filePath) {
      try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
        String line;
        boolean parsingUpdates = false;
        while ((line = reader.readLine()) != null) {
          if (line.trim().isEmpty()) {
            parsingUpdates = true;
            continue;
          }
          if (!parsingUpdates) {
            String[] split = line.split("\\|");
            rules.add(new int[]{Integer.parseInt(split[0]), Integer.parseInt(split[1])});
          } else {
            String[] split = line.split(",");
            updates.add(Arrays.stream(split).mapToInt(Integer::parseInt).toArray());
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    public long calculateTotal() {
      long total = 0;
      for (int[] update : updates) {
        if (isCorrectOrder(update)) {
          int middleIndex = update.length / 2;
          total += update.length % 2 == 0 ? update[middleIndex - 1] : update[middleIndex];
        }
      }
      return total;
    }

    private boolean isCorrectOrder(int[] update) {
      buildGraph(update);

      Queue<Integer> queue = new LinkedList<>();
      for (int node : update) {
        if (!inDegree.containsKey(node) || inDegree.get(node) == 0) {
          queue.offer(node);
        }
      }

      int index = 0;
      while (!queue.isEmpty()) {
        int current = queue.poll();

        // Check if current matches the current index in the original update sequence
        if (index >= update.length || update[index++] != current) {
          return false;
        }

        for (int neighbor : graph.getOrDefault(current, new ArrayList<>())) {
          int degree = inDegree.get(neighbor) - 1;
          inDegree.put(neighbor, degree);
          if (degree == 0) {
            queue.offer(neighbor);
          }
        }
      }

      // If all nodes are processed and matched the update, it means order is correct
      return index == update.length;
    }

    private void buildGraph(int[] update) {
      graph = new HashMap<>();
      inDegree = new HashMap<>();

      Set<Integer> updateSet = new HashSet<>();
      for (int page : update) {
        updateSet.add(page);
      }

      for (int[] rule : rules) {
        int start = rule[0];
        int end = rule[1];

        if (updateSet.contains(start) && updateSet.contains(end)) {
          graph.computeIfAbsent(start, k -> new ArrayList<>()).add(end);
          inDegree.put(end, inDegree.getOrDefault(end, 0) + 1);
          inDegree.putIfAbsent(start, 0);
        }
      }
    }

    public long calculateReorderedMiddleSum() {
      long total = 0;

      for (int[] update : updates) {
        if (!isCorrectOrder(update)) {
          int [] reorderedUpdate = correctlyOrderUpdate(update);
          int middleIndex = reorderedUpdate.length / 2;
          total += reorderedUpdate.length % 2 == 0 ? reorderedUpdate[middleIndex - 1] : reorderedUpdate[middleIndex];
        }
      }
      return total;
    }

    private int[] correctlyOrderUpdate(int[] update) {
      Map<Integer, List<Integer>> graph = new HashMap<>();
      Map<Integer, Integer> inDegree = new HashMap<>();

      Set<Integer> updateSet = new HashSet<>();
      for (int page : update) {
        updateSet.add(page);
      }

      for (int[] rule : rules) {
        int start = rule[0];
        int end = rule[1];

        if (updateSet.contains(start) && updateSet.contains(end)) {
          graph.computeIfAbsent(start, k -> new ArrayList<>()).add(end);
          inDegree.put(end, inDegree.getOrDefault(end, 0) + 1);
          inDegree.putIfAbsent(start, 0);
        }
      }

      List<Integer> order = new ArrayList<>();
      Queue<Integer> queue = new LinkedList<>();

      for (int node : update) {
        if (!inDegree.containsKey(node) || inDegree.get(node) == 0) {
          queue.offer(node);
        }
      }

      while (!queue.isEmpty()) {
        int current = queue.poll();
        order.add(current);

        for (int neighbor : graph.getOrDefault(current, new ArrayList<>())) {
          int degree = inDegree.get(neighbor) - 1;
          inDegree.put(neighbor, degree);
          if (degree == 0) {
            queue.offer(neighbor);
          }
        }
      }

      return order.stream().mapToInt(i -> i).toArray();
    }
  }
}
