package com.advent2025;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day4 {

  public static void main(String[] args) throws IOException {
    String filePath = "src/main/resources/advent2025/day4.txt";
    long t1 = System.currentTimeMillis();
    List<String> input = Files.readAllLines(Path.of(filePath));
    System.out.println("reading input data took: " + (System.currentTimeMillis() - t1) + " ms");

//    countNeighbours(input);
    part2(input);
  }

  private static void part2(List<String> input) {
    int rows = input.size();
    int cols = input.getFirst().length();
    boolean changed = true;
    int totalUpdated = 0;
    char[][] grid = new char[rows][cols];
    for (int i = 0; i < rows; i++) {
      grid[i] = input.get(i).toCharArray();
    }

    while (changed) {
      changed = false;

      char[][] newGrid = new char[rows][cols];
      for (int row = 0; row < rows; row++) {
        for (int col = 0; col < cols; col++) {
          newGrid[row][col] = grid[row][col];
          if (grid[row][col] == '@') {
            int count = countThem(grid, row, col);

            if (count < 4) {
              changed = true;
              newGrid[row][col] = 'x';
              totalUpdated++;
            }
          }
        }
      }
      grid = newGrid;
      System.out.println("Total updated so far: " + totalUpdated);
//      printGrid(grid);
    }
    System.out.println("Final total updated: " + totalUpdated);
  }

  private static void printGrid(char[][] grid) {
    for (char[] row : grid) {
      System.out.println(new String(row));
    }
    System.out.println();
  }

  private static int countThem(char[][] grid, int row, int col) {
    int[][] directions = {
        {-1, -1}, {-1, 0}, {-1, 1},
        {0, -1}, {0, 1},
        {1, -1}, {1, 0}, {1, 1}
    };

    int count = 0;
    for (int[] dir : directions) {
      int newRow = row + dir[0];
      int newCol = col + dir[1];

      if (newRow >= 0 && newRow < grid.length && newCol >= 0 && newCol < grid[0].length) {
        if (grid[newRow][newCol] == '@') {
          count++;
        }
      }
    }
    return count;
  }

  private static void countNeighbours(List<String> input) {
    int rows = input.size();
    int cols = input.getFirst().length();
    int accessibleCount = 0;
    char[][] grid = new char[rows][cols];
    for (int i = 0; i < rows; i++) {
      grid[i] = input.get(i).toCharArray();
    }

    int[][] directions = {
        {-1, -1}, {-1, 0}, {-1, 1},
        {0, -1}, {0, 1},
        {1, -1}, {1, 0}, {1, 1}
    };
    int[][] neighborCounts = new int[rows][cols];

    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        if (grid[row][col] == '@') {
          int count = 0;
          for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols) {
              if (grid[newRow][newCol] == '@') {
                count++;
              }
            }
          }
          neighborCounts[row][col] = count;

          if (count < 4) {
            accessibleCount++;
            System.out.println("Position (" + row + ", " + col + ") has " + count + " neighboring '@' symbols.");
          }
        }
      }
    }
    System.out.println("Total accessible positions: " + accessibleCount);
  }

}
