package com.advent2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/// --- Day 8: Resonant Collinearity ---
/// You find yourselves on the roof of a top-secret Easter Bunny installation.
///
/// While The Historians do their thing, you take a look at the familiar huge antenna. Much to your surprise, it seems to have been reconfigured to emit a signal that makes people 0.1% more likely to buy Easter Bunny brand Imitation Mediocre Chocolate as a Christmas gift! Unthinkable!
///
/// Scanning across the city, you find that there are actually many such antennas. Each antenna is tuned to a specific frequency indicated by a single lowercase letter, uppercase letter, or digit. You create a map (your puzzle input) of these antennas. For example:
///
/// ............
/// ........0...
/// .....0......
/// .......0....
/// ....0.......
/// ......A.....
/// ............
/// ............
/// ........A...
/// .........A..
/// ............
/// ............
/// The signal only applies its nefarious effect at specific antinodes based on the resonant frequencies of the antennas. In particular, an antinode occurs at any point that is perfectly in line with two antennas of the same frequency - but only when one of the antennas is twice as far away as the other. This means that for any pair of antennas with the same frequency, there are two antinodes, one on either side of them.
///
/// So, for these two antennas with frequency a, they create the two antinodes marked with #:
///
/// ..........
/// ...#......
/// ..........
/// ....a.....
/// ..........
/// .....a....
/// ..........
/// ......#...
/// ..........
/// ..........
/// Adding a third antenna with the same frequency creates several more antinodes. It would ideally add four antinodes, but two are off the right side of the map, so instead it adds only two:
///
/// ..........
/// ...#......
/// #.........
/// ....a.....
/// ........a.
/// .....a....
/// ..#.......
/// ......#...
/// ..........
/// ..........
/// Antennas with different frequencies don't create antinodes; A and a count as different frequencies. However, antinodes can occur at locations that contain antennas. In this diagram, the lone antenna with frequency capital A creates no antinodes but has a lowercase-a-frequency antinode at its location:
///
/// ..........
/// ...#......
/// #.........
/// ....a.....
/// ........a.
/// .....a....
/// ..#.......
/// ......A...
/// ..........
/// ..........
/// The first example has antennas with two different frequencies, so the antinodes they create look like this, plus an antinode overlapping the topmost A-frequency antenna:
///
/// ......#....#
/// ...#....0...
/// ....#0....#.
/// ..#....0....
/// ....0....#..
/// .#....A.....
/// ...#........
/// #......#....
/// ........A...
/// .........A..
/// ..........#.
/// ..........#.
/// Because the topmost A-frequency antenna overlaps with a 0-frequency antinode, there are 14 total unique locations that contain an antinode within the bounds of the map.
///
/// Calculate the impact of the signal. How many unique locations within the bounds of the map contain an antinode?
public class Day8 {

  public static void main(String[] args) throws IOException {
    String filePath = "src/main/resources/input_day8.txt";
    List<String> inputData = Files.readAllLines(Path.of(filePath));

    AntennaAntinodes antinodes = new AntennaAntinodes(inputData);
    System.out.println("Antinodes: " + antinodes.countAntinodes());
    antinodes.printResult();

  }

  public static class AntennaAntinodes {
    private static class Point {
      int x;
      int y;

      public Point(int x, int y) {
        this.x = x;
        this.y = y;
      }

      @Override
      public boolean equals(Object o) {
        if (!(o instanceof Point point)) {
          return false;
        }
        return x == point.x && y == point.y;
      }

      @Override
      public int hashCode() {
        return Objects.hash(x, y);
      }
    }

    private char[][] grid;
    private int rows;
    private int cols;
    private Map<Character, List<Point>> antennasByFrequency;
    private Set<Point> antinodes;

    public AntennaAntinodes(List<String> input) {
      rows = input.size();
      cols = input.getFirst().length();
      grid = new char[rows][cols];
      for (int i = 0; i < rows; i++) {
        grid[i] = input.get(i).toCharArray();
      }
      antennasByFrequency = new HashMap<>();
      findAntennas();

      antinodes = new HashSet<>();
      calculateAntinodes();
    }

    private void findAntennas() {
      for (int y = 0; y < rows; y++) {
        for (int x = 0; x < cols; x++) {
          char c = grid[y][x];
          if (c != '.') {
            antennasByFrequency.computeIfAbsent(c, k -> new ArrayList<>()).add(new Point(x, y));
          }
        }
      }
    }

    private void calculateAntinodes() {
      for (Map.Entry<Character, List<Point>> entry : antennasByFrequency.entrySet()) {
        List<Point> antennas = entry.getValue();
        for (int i = 0; i < antennas.size() - 1; i++) {
          for (int j = i + 1; j < antennas.size(); j++) {
            Point a1 = antennas.get(i);
            Point a2 = antennas.get(j);

            int dx = a2.x - a1.x;
            int dy = a2.y - a1.y;

            checkAndAddAntinode(a1.x - dx, a1.y - dy, dx, dy);
            checkAndAddAntinode(a2.x + dx, a2.y + dy, dx, dy);
          }
        }
      }
    }

    private void checkAndAddAntinode(int potentialX, int potentialY, int dx, int dy) {
      if (isInBounds(potentialX, potentialY) && Math.abs(dx) == Math.abs(dy)) {
        for (int sx = -1; sx <= 1; sx +=2) {
          for (int sy = -1; sy <= 1; sy += 2) {
            int x = potentialX + sx * dx;
            int y = potentialY + sy * dy;
            if (isInBounds(x, y)) {
              antinodes.add(new Point(x, y));
            }
          }
        }
      }
    }

//    private void calculateAntinodes() {
//      for (List<Point> antennas : antennasByFrequency.values()) {
//        for (int i = 0; i < antennas.size(); i++) {
//          for (int j = i + 1; j < antennas.size(); j++) {
//            Point a1 = antennas.get(i);
//            Point a2 = antennas.get(j);
////            findNodesForPair(a1, a2);
//
//            int dx = a2.x - a1.x;
//            int dy = a2.y - a1.y;
//
//            if (dx == 0 || dy == 0 || Math.abs(dx) == Math.abs(dy)) {
//              int stepX = dx == 0 ? 0 : dx / Math.abs(dx);
//              int stepY = dy == 0 ? 0 : dy / Math.abs(dy);
//
//              int steps = Math.max(Math.abs(dx), Math.abs(dy));
//
//              for (int isFirst = 0; isFirst <= 1; isFirst++) {
//                Point closer = isFirst == 0 ? a1 : a2;
//                Point farther = isFirst == 0 ? a2 : a1;
//
//                int antinodeSteps = steps / 3;
//                if (steps % 3 == 0) {
//                  int antinodeX = closer.x + antinodeSteps * stepX;
//                  int antinodeY = closer.y + antinodeSteps * stepY;
//
//                  if (isInBounds(antinodeX, antinodeY)) {
//                    antinodes.add(new Point(antinodeX, antinodeY));
//                  }
//                }
//              }
//            }
//
////            int x1 = a1.x + dx / 3;
////            int y1 = a1.y + dy / 3;
////            int x2 = a2.x - dx / 3;
////            int y2 = a2.y - dy / 3;
//
////            if (isInBounds(x1, y1)) {
////              double d1 = distance(x1, y1, a1.x, a1.y);
////              double d2 = distance(x1, y1, a2.x, a2.y);
////              if (Math.abs(d2 - 2 * d1) < 0.1) {
////                antinodes.add(new Point(x1, y1));
////              }
////            }
//
////            if (isInBounds(x2, y2)) {
////              double d1 = distance(x2, y2, a1.x, a1.y);
////              double d2 = distance(x2, y2, a2.x, a2.y);
////              if (Math.abs(d2 - 2 * d1) < 0.1) {
////                antinodes.add(new Point(x2, y2));
////              }
////            }
//          }
//        }
//      }
//    }

//    private double distance(int x1, int y1, int x2, int y2) {
//      int dx = x2 - x1;
//      int dy = y2 - y1;
//      return Math.sqrt(dx * dx + dy * dy);
//    }
//
//    private void findNodesForPair(Point a1, Point a2) {
//      int dx = a2.x - a1.x;
//      int dy = a2.y - a1.y;
//
//     checkAndAddAntinode(a1.x + dx / 3, a1.y + dy / 3);
//     checkAndAddAntinode(a1.x - dx / 3, a1.y - dy / 3);
//
//     checkAndAddAntinode(a2.x + dx / 3, a2.y + dy / 3);
//     checkAndAddAntinode(a2.x - dx / 3, a2.y - dy / 3);
//    }

//    private void checkAndAddAntinode(int x, int y) {
//      if (isInBounds(x, y)) {
//        antinodes.add(new Point(x, y));
//      }
//    }

    private boolean isInBounds(int x, int y) {
      return x >= 0 && x < cols && y >= 0 && y < rows;
    }

    public int countAntinodes() {
      return antinodes.size();
    }

    public void printResult() {
      char[][] result = new char[rows][cols];
      for (int i = 0; i < rows; i++) {
        result[i] = grid[i].clone();
      }

      for (Point p : antinodes) {
        if (result[p.y][p.x] == '.') {
          result[p.y][p.x] = '#';
        }
      }

      for (char[] row : result) {
        System.out.println(new String(row));
      }
    }
  }

}
