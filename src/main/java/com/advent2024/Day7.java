package com.advent2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/// --- Day 7: Bridge Repair ---
/// The Historians take you to a familiar rope bridge over a river in the middle of a jungle. The Chief isn't on this side of the bridge, though; maybe he's on the other side?
///
/// When you go to cross the bridge, you notice a group of engineers trying to repair it. (Apparently, it breaks pretty frequently.) You won't be able to cross until it's fixed.
///
/// You ask how long it'll take; the engineers tell you that it only needs final calibrations, but some young elephants were playing nearby and stole all the operators from their calibration equations! They could finish the calibrations if only someone could determine which test values could possibly be produced by placing any combination of operators into their calibration equations (your puzzle input).
///
/// For example:
///
/// 190: 10 19
/// 3267: 81 40 27
/// 83: 17 5
/// 156: 15 6
/// 7290: 6 8 6 15
/// 161011: 16 10 13
/// 192: 17 8 14
/// 21037: 9 7 18 13
/// 292: 11 6 16 20
/// Each line represents a single equation. The test value appears before the colon on each line; it is your job to determine whether the remaining numbers can be combined with operators to produce the test value.
///
/// Operators are always evaluated left-to-right, not according to precedence rules. Furthermore, numbers in the equations cannot be rearranged. Glancing into the jungle, you can see elephants holding two different types of operators: add (+) and multiply (*).
///
/// Only three of the above equations can be made true by inserting operators:
///
/// 190: 10 19 has only one position that accepts an operator: between 10 and 19. Choosing + would give 29, but choosing * would give the test value (10 * 19 = 190).
/// 3267: 81 40 27 has two positions for operators. Of the four possible configurations of the operators, two cause the right side to match the test value: 81 + 40 * 27 and 81 * 40 + 27 both equal 3267 (when evaluated left-to-right)!
/// 292: 11 6 16 20 can be solved in exactly one way: 11 + 6 * 16 + 20.
/// The engineers just need the total calibration result, which is the sum of the test values from just the equations that could possibly be true. In the above example, the sum of the test values for the three equations listed above is 3749.
///
/// Determine which equations could possibly be true. What is their total calibration result?
/// --- Part Two ---
/// The engineers seem concerned; the total calibration result you gave them is nowhere close to being within safety tolerances. Just then, you spot your mistake: some well-hidden elephants are holding a third type of operator.
///
/// The concatenation operator (||) combines the digits from its left and right inputs into a single number. For example, 12 || 345 would become 12345. All operators are still evaluated left-to-right.
///
/// Now, apart from the three equations that could be made true using only addition and multiplication, the above example has three more equations that can be made true by inserting operators:
///
/// 156: 15 6 can be made true through a single concatenation: 15 || 6 = 156.
/// 7290: 6 8 6 15 can be made true using 6 * 8 || 6 * 15.
/// 192: 17 8 14 can be made true using 17 || 8 + 14.
/// Adding up all six test values (the three that could be made before using only + and * plus the new three that can now be made by also using ||) produces the new total calibration result of 11387.
///
/// Using your new knowledge of elephant hiding spots, determine which equations could possibly be true. What is their total calibration result?
public class Day7 {

  public static void main(String[] args) throws IOException {

    String filePath = "src/main/resources/input_day7.txt";
    List<String> inputData = Files.readAllLines(Path.of(filePath));
    EquationValidator validator = new EquationValidator();
    long l = validator.calculateCalibrationResult(inputData);
    System.out.println("Calibration result: " + l);

  }

  private static class EquationValidator {
    static class Equation {
      long testValue;
      List<Long> numbers;

      public Equation(long testValue, List<Long> numbers) {
        this.testValue = testValue;
        this.numbers = numbers;
      }

      @Override
      public String toString() {
        return  testValue + " : " + numbers;
      }
    }

    private long evaluate(List<Long> numbers, String operators) {
      long result = numbers.getFirst();
      for (int i = 0; i < operators.length(); i++) {
        char op = operators.charAt(i);
        long nextNum = numbers.get(i + 1);

        if (op == '+') {
          result = result + nextNum;
        } else if (op == '*') {
          result = result * nextNum;
          // part 2
        } else if (op == '|') {
          String concatenated = result + String.valueOf(nextNum);
          result = Long.parseLong(concatenated);
        }
      }
      return result;
    }

    private List<String> generateOperators(int count) {
      List<String> combinations = new ArrayList<>();
      generateOperatorsHelp("", count, combinations);
      return combinations;
    }

    private void generateOperatorsHelp(String current, int remaining, List<String> combinations) {
      if (remaining == 0) {
        combinations.add(current);
        return;
      }
      generateOperatorsHelp(current + "+", remaining - 1, combinations);
      generateOperatorsHelp(current + "*", remaining - 1, combinations);
      // part 2
      generateOperatorsHelp(current + "|", remaining - 1, combinations);
    }

    private boolean canBeSolved(Equation eq) {
      int operatorsNeeded = eq.numbers.size() - 1;
      List<String> operatorCombinations = generateOperators(operatorsNeeded);

      for (String operators : operatorCombinations) {
        if (evaluate(eq.numbers, operators) == eq.testValue) {
          System.out.println("Found solution: " + eq.numbers + " " + operators + " = " + eq.testValue);
          return true;
        }
      }
      return false;
    }

    private Equation parseEquation(String line) {
      String[] parts = line.split(":");
      long testValue = Long.parseLong(parts[0].trim());
      String[] numberStrings = parts[1].trim().split("\\s+");
      List<Long> numbers = new ArrayList<>();
      for (String num : numberStrings) {
        numbers.add(Long.parseLong(num));
      }
      return new Equation(testValue, numbers);
    }

    public long calculateCalibrationResult(List<String> input) {
      long sum = 0;
      for (String line : input) {
        Equation eq = parseEquation(line);
        if (canBeSolved(eq)) {
          sum += eq.testValue;
        }
      }
      return sum;
    }
  }

}
