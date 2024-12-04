package com.advent2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * --- Day 4: Ceres Search --- "Looks like the Chief's not here. Next!" One of The Historians pulls out a device and
 * pushes the only button on it. After a brief flash, you recognize the interior of the Ceres monitoring station!
 * <p>
 * As the search for the Chief continues, a small Elf who lives on the station tugs on your shirt; she'd like to know if
 * you could help her with her word search (your puzzle input). She only has to find one word: XMAS.
 * <p>
 * This word search allows words to be horizontal, vertical, diagonal, written backwards, or even overlapping other
 * words. It's a little unusual, though, as you don't merely need to find one instance of XMAS - you need to find all of
 * them. Here are a few ways XMAS might appear, where irrelevant characters have been replaced with .:
 * <p>
 * ..X... .SAMX. .A..A. XMAS.S .X.... The actual word search will be full of letters instead. For example:
 * <p>
 * MMMSXXMASM MSAMXMSMSA AMXSXMAAMM MSAMASMSMX XMASAMXAMM XXAMMXXAMA SMSMSASXSS SAXAMASAAA MAMMMXMMMM MXMXAXMASX In this
 * word search, XMAS occurs a total of 18 times; here's the same word search again, but where letters not involved in
 * any XMAS have been replaced with .:
 * <p>
 * ....XXMAS. .SAMXMS... ...S..A... ..A.A.MS.X XMASAMX.MM X.....XA.A S.S.S.S.SS .A.A.A.A.A ..M.M.M.MM .X.X.XMASX Take a
 * look at the little Elf's word search. How many times does XMAS appear? --- Part Two --- The Elf looks quizzically at
 * you. Did you misunderstand the assignment?
 * <p>
 * Looking for the instructions, you flip over the word search to find that this isn't actually an XMAS puzzle; it's an
 * X-MAS puzzle in which you're supposed to find two MAS in the shape of an X. One way to achieve that is like this:
 * <p>
 * M.S .A. M.S Irrelevant characters have again been replaced with . in the above diagram. Within the X, each MAS can be
 * written forwards or backwards.
 * <p>
 * Here's the same example from before, but this time all of the X-MASes have been kept instead:
 * <p>
 * .M.S...... ..A..MSMS. .M.S.MAA.. ..A.ASMSM. .M.S.M.... .......... S.S.S.S.S. .A.A.A.A.. M.M.M.M.M. .......... In this
 * example, an X-MAS appears 9 times.
 * <p>
 * Flip the word search from the instructions back over to the word search side and try again. How many times does an
 * X-MAS appear?
 */
public class Day4 {

  public static void main(String[] args) {
    String filePath = "src/main/resources/input_day4.txt";
    WordSearchProcessor wordSearchProcessor = new WordSearchProcessor();
    List<String> strings = wordSearchProcessor.readInput(filePath);
    long total = wordSearchProcessor.calculateTotal(strings);
    System.out.println("Total occurrences of XMAS: " + total);
    long xShapeOccurrences = wordSearchProcessor.calculateXShapeOccurrences(strings);
    System.out.println("Total occurrences of XMAS in X shape: " + xShapeOccurrences);
  }

  public static class WordSearchProcessor {

    public List<String> readInput(String filePath) {
      List<String> strings = new ArrayList<>();
      try {
        strings = Files.readAllLines(Paths.get(filePath));
      } catch (IOException e) {
        e.printStackTrace();
      }
      return strings;
    }

    public long calculateTotal(List<String> grid) {
      long total = 0;
      int numRows = grid.size();
      int numCols = grid.get(0).length();
      if (numRows == 0) {
        return 0;
      }

      // horizontal and reverse horizontal
      total = countHorizontalOccurrences(grid, total);

      // vertical and reverse vertical
      total = countVerticalOccurrences(grid, numCols, total);

      // diagonal and reverse diagonal from top-left to bottom-right\
      total = countDiagonalOccurrences(grid, numRows, numCols, total);
      return total;
    }

    private long countHorizontalOccurrences(List<String> grid, long total) {
      for (String row : grid) {
        total += countOccurrencesInLine(row, "XMAS");
        total += countOccurrencesInLine(new StringBuilder(row).reverse().toString(), "XMAS");
      }
      return total;
    }

    private long countVerticalOccurrences(List<String> grid, int numCols, long total) {
      for (int col = 0; col < numCols; col++) {
        StringBuilder colBuilder = new StringBuilder();
        for (String row : grid) {
          colBuilder.append(row.charAt(col));
        }
        total += countOccurrencesInLine(colBuilder.toString(), "XMAS");
        total += countOccurrencesInLine(colBuilder.reverse().toString(), "XMAS");
      }
      return total;
    }

    private long countDiagonalOccurrences(List<String> grid, int numRows, int numCols, long total) {
      for (int d = 0; d < numRows + numCols - 1; d++) {
        StringBuilder diagonal1 = new StringBuilder();
        StringBuilder diagonal2 = new StringBuilder();
        for (int row = 0; row < numRows; ++row) {
          int col1 = d - row;
          int col2 = numRows - 1 - (d - row);
          if (col1 >= 0 && col1 < numCols) {
            diagonal1.append(grid.get(row).charAt(col1));
          }
          if (col2 >= 0 && col2 < numCols) {
            diagonal2.append(grid.get(row).charAt(col2));
          }
        }
        total += countOccurrencesInLine(diagonal1.toString(), "XMAS");
        total += countOccurrencesInLine(diagonal1.reverse().toString(), "XMAS");
        total += countOccurrencesInLine(diagonal2.toString(), "XMAS");
        total += countOccurrencesInLine(diagonal2.reverse().toString(), "XMAS");
      }
      return total;
    }

    private long countOccurrencesInLine(String line, String word) {
      long count = 0;
      int index = line.indexOf(word);
      while (index != -1) {
        count++;
        index = line.indexOf(word, index + 1);
      }
      return count;
    }

    public long calculateXShapeOccurrences(List<String> grid) {
      long count = 0;
      int numRows = grid.size();
      if (numRows <= 2) {
        return 0;
      }
      int numCols = grid.get(0).length();

      for (int row = 1; row < numRows - 1; ++row) {
        for (int col = 1; col < numCols - 1; ++col) {
          count += countValidXShapes(grid, row, col);
        }
      }
      return count;
    }

    private int countValidXShapes(List<String> grid, int centerRow, int centerCol) {
      char topLeft = grid.get(centerRow - 1).charAt(centerCol - 1);
      char topRight = grid.get(centerRow - 1).charAt(centerCol + 1);
      char center = grid.get(centerRow).charAt(centerCol);
      char bottomLeft = grid.get(centerRow + 1).charAt(centerCol - 1);
      char bottomRight = grid.get(centerRow + 1).charAt(centerCol + 1);

      int validConfigurations = 0;
      if ((isMAS(topRight, center, bottomLeft) || isMAS(bottomLeft, center, topRight)) &&
          (isMAS(topLeft, center, bottomRight) || isMAS(bottomRight, center, topLeft))) {
        validConfigurations++;
      }

      return validConfigurations;
    }

    private boolean isMAS(char first, char middle, char last) {
      return (first == 'M' && middle == 'A' && last == 'S') || (first == 'S' && middle == 'A' && last == 'M');
    }
  }

}
