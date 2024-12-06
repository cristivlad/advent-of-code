package com.advent2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * --- Day 6: Guard Gallivant ---
 * The Historians use their fancy device again, this time to whisk you all away to the North Pole prototype suit manufacturing lab... in the year 1518! It turns out that having direct access to history is very convenient for a group of historians.
 * <p>
 * You still have to be careful of time paradoxes, and so it will be important to avoid anyone from 1518 while The Historians search for the Chief. Unfortunately, a single guard is patrolling this part of the lab.
 * <p>
 * Maybe you can work out where the guard will go ahead of time so that The Historians can search safely?
 * <p>
 * You start by making a map (your puzzle input) of the situation. For example:
 * <p>
 * ....#.....
 * .........#
 * ..........
 * ..#.......
 * .......#..
 * ..........
 * .#..^.....
 * ........#.
 * #.........
 * ......#...
 * The map shows the current position of the guard with ^ (to indicate the guard is currently facing up from the perspective of the map). Any obstructions - crates, desks, alchemical reactors, etc. - are shown as #.
 * <p>
 * Lab guards in 1518 follow a very strict patrol protocol which involves repeatedly following these steps:
 * <p>
 * If there is something directly in front of you, turn right 90 degrees.
 * Otherwise, take a step forward.
 * Following the above protocol, the guard moves up several times until she reaches an obstacle (in this case, a pile of failed suit prototypes):
 * <p>
 * ....#.....
 * ....^....#
 * ..........
 * ..#.......
 * .......#..
 * ..........
 * .#........
 * ........#.
 * #.........
 * ......#...
 * Because there is now an obstacle in front of the guard, she turns right before continuing straight in her new facing direction:
 * <p>
 * ....#.....
 * ........>#
 * ..........
 * ..#.......
 * .......#..
 * ..........
 * .#........
 * ........#.
 * #.........
 * ......#...
 * Reaching another obstacle (a spool of several very long polymers), she turns right again and continues downward:
 * <p>
 * ....#.....
 * .........#
 * ..........
 * ..#.......
 * .......#..
 * ..........
 * .#......v.
 * ........#.
 * #.........
 * ......#...
 * This process continues for a while, but the guard eventually leaves the mapped area (after walking past a tank of universal solvent):
 * <p>
 * ....#.....
 * .........#
 * ..........
 * ..#.......
 * .......#..
 * ..........
 * .#........
 * ........#.
 * #.........
 * ......#v..
 * By predicting the guard's route, you can determine which specific positions in the lab will be in the patrol path. Including the guard's starting position, the positions visited by the guard before leaving the area are marked with an X:
 * <p>
 * ....#.....
 * ....XXXXX#
 * ....X...X.
 * ..#.X...X.
 * ..XXXXX#X.
 * ..X.X.X.X.
 * .#XXXXXXX.
 * .XXXXXXX#.
 * #XXXXXXX..
 * ......#X..
 * In this example, the guard will visit 41 distinct positions on your map.
 * <p>
 * Predict the path of the guard. How many distinct positions will the guard visit before leaving the mapped area?
 * --- Part Two ---
 * While The Historians begin working around the guard's patrol route, you borrow their fancy device and step outside the lab. From the safety of a supply closet, you time travel through the last few months and record the nightly status of the lab's guard post on the walls of the closet.
 * <p>
 * Returning after what seems like only a few seconds to The Historians, they explain that the guard's patrol area is simply too large for them to safely search the lab without getting caught.
 * <p>
 * Fortunately, they are pretty sure that adding a single new obstruction won't cause a time paradox. They'd like to place the new obstruction in such a way that the guard will get stuck in a loop, making the rest of the lab safe to search.
 * <p>
 * To have the lowest chance of creating a time paradox, The Historians would like to know all of the possible positions for such an obstruction. The new obstruction can't be placed at the guard's starting position - the guard is there right now and would notice.
 * <p>
 * In the above example, there are only 6 different positions where a new obstruction would cause the guard to get stuck in a loop. The diagrams of these six situations use O to mark the new obstruction, | to show a position where the guard moves up/down, - to show a position where the guard moves left/right, and + to show a position where the guard moves both up/down and left/right.
 * <p>
 * Option one, put a printing press next to the guard's starting position:
 * <p>
 * ....#.....
 * ....+---+#
 * ....|...|.
 * ..#.|...|.
 * ....|..#|.
 * ....|...|.
 * .#.O^---+.
 * ........#.
 * #.........
 * ......#...
 * Option two, put a stack of failed suit prototypes in the bottom right quadrant of the mapped area:
 * <p>
 * <p>
 * ....#.....
 * ....+---+#
 * ....|...|.
 * ..#.|...|.
 * ..+-+-+#|.
 * ..|.|.|.|.
 * .#+-^-+-+.
 * ......O.#.
 * #.........
 * ......#...
 * Option three, put a crate of chimney-squeeze prototype fabric next to the standing desk in the bottom right quadrant:
 * <p>
 * ....#.....
 * ....+---+#
 * ....|...|.
 * ..#.|...|.
 * ..+-+-+#|.
 * ..|.|.|.|.
 * .#+-^-+-+.
 * .+----+O#.
 * #+----+...
 * ......#...
 * Option four, put an alchemical retroencabulator near the bottom left corner:
 * <p>
 * ....#.....
 * ....+---+#
 * ....|...|.
 * ..#.|...|.
 * ..+-+-+#|.
 * ..|.|.|.|.
 * .#+-^-+-+.
 * ..|...|.#.
 * #O+---+...
 * ......#...
 * Option five, put the alchemical retroencabulator a bit to the right instead:
 * <p>
 * ....#.....
 * ....+---+#
 * ....|...|.
 * ..#.|...|.
 * ..+-+-+#|.
 * ..|.|.|.|.
 * .#+-^-+-+.
 * ....|.|.#.
 * #..O+-+...
 * ......#...
 * Option six, put a tank of sovereign glue right next to the tank of universal solvent:
 * <p>
 * ....#.....
 * ....+---+#
 * ....|...|.
 * ..#.|...|.
 * ..+-+-+#|.
 * ..|.|.|.|.
 * .#+-^-+-+.
 * .+----++#.
 * #+----++..
 * ......#O..
 * It doesn't really matter what you choose to use as an obstacle so long as you and The Historians can put it into position without the guard noticing. The important thing is having enough options that you can find one that minimizes time paradoxes, and in this example, there are 6 different positions you could choose.
 * <p>
 * You need to get the guard stuck in a loop by adding a single new obstruction. How many different positions could you choose for this obstruction?
 */
public class Day6 {

  public static void main(String[] args) throws IOException {
    String filePath = "src/main/resources/input_day6.txt";
    List<String> gridLines = Files.readAllLines(Path.of(filePath));
    gridLines = gridLines.stream()
        .map(String::trim)
        .filter(line -> !line.isEmpty())
        .toList();
    if (gridLines.isEmpty()) {
      System.err.println("Error: Input file is empty");
      return;
    }
    int firstLineLength = gridLines.get(0).length();
    boolean validGrid = gridLines.stream()
        .allMatch(line -> line.length() == firstLineLength);

    if (!validGrid) {
      System.err.println("Error: Not all lines have the same length");
      return;
    }


    GuardPathSimulator simulator = new GuardPathSimulator(gridLines);
    int visitedCount = simulator.simulate();

    System.out.println("Grid dimensions: " + gridLines.get(0).length() + "x" + gridLines.size());
    System.out.println("The guard visited " + visitedCount + " distinct positions");
    System.out.println("\nPath visualization:");
    simulator.printPath();

    GuardLoopDetector detector = new GuardLoopDetector(gridLines);
    Set<Position> loopPositions = detector.findLoopPositions();

    System.out.println("Found " + loopPositions.size() + " positions that would create loops");
    System.out.println("\nGrid with possible loop positions marked as 'O':");
    detector.printWithLoopPositions(loopPositions);
  }

  public static class GuardPathSimulator {

      private final char[][] grid;
      private final int height;
      private final int width;
      private Set<Position> visited;
      private Position guardPosition;
      private Direction direction;

      public GuardPathSimulator(List<String> gridLines) {
        height = gridLines.size();
        width = height > 0 ? gridLines.get(0).length() : 0;
        grid = new char[height][width];
        visited = new HashSet<>(width * height);
        for (int i = 0; i < height; i++) {
          grid[i] = gridLines.get(i).toCharArray();
        }
        guardPosition = findGuard();
        direction = Direction.UP;
      }

      private Position findGuard() {
        for (int y = 0; y < height; y++) {
          for (int x = 0; x < width; x++) {
            if (grid[y][x] == '^') {
              return new Position(x, y);
            }
          }
        }
        throw new IllegalStateException("Guard not found");
      }

      private boolean isValidPosition(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
      }

      private boolean isObstacle(int x, int y) {
        return isValidPosition(x, y) && grid[y][x] == '#';
      }

      public int simulate() {
//        visited.clear();
        visited.add(guardPosition);
//        int steps = 0;
//        int maxSteps = width * height * 4;

        while (true) {
//          steps++;
          int nextX = guardPosition.x + direction.x;
          int nextY = guardPosition.y + direction.y;

          if (!isValidPosition(nextX, nextY)) {
            break;
          }

          if (isObstacle(nextX, nextY)) {
            direction = direction.turnRight();
          } else {
            guardPosition = new Position(nextX, nextY);
//            if (!isValidPosition(guardPosition.x, guardPosition.y)) {
//              break;
//            }
            visited.add(guardPosition);
          }
        }
//        if (steps >= maxSteps) {
//          System.out.println("Warning: Simulation stopped after " + steps + " steps");
//        }
        return visited.size();
      }

    public void printPath() {
      char[][] pathGrid = new char[height][width];

      // Copy original grid
      for (int y = 0; y < height; y++) {
        pathGrid[y] = grid[y].clone();
      }

      // Mark visited positions
      for (Position pos : visited) {
        if (isValidPosition(pos.x, pos.y) && pathGrid[pos.y][pos.x] != '#' && pathGrid[pos.y][pos.x] != '^') {
          pathGrid[pos.y][pos.x] = 'X';
        }
      }

      // Print the result
      for (char[] row : pathGrid) {
        System.out.println(new String(row));
      }
    }
  }

  public enum Direction {
    UP(0, -1),
    DOWN(1, 0),
    LEFT(0, 1),
    RIGHT(-1, 0);

    private final int x;
    private final int y;

    Direction(int x, int y) {
      this.x = x;
      this.y = y;
    }

    public Direction turnRight() {
      Direction[] values = Direction.values();
      return values[(this.ordinal() + 1) % values.length];
    }
  }

  public static class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
      this.x = x;
      this.y = y;
    }

    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Position position)) {
        return false;
      }
      return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
      return Objects.hash(x, y);
    }
  }

  public static class GuardLoopDetector {
    private static class State {
      Position pos;
      Direction dir;

      public State(Position pos, Direction dir) {
        this.pos = pos;
        this.dir = dir;
      }

      @Override
      public boolean equals(Object o) {
        if (!(o instanceof State state)) {
          return false;
        }
        return Objects.equals(pos, state.pos) && dir == state.dir;
      }

      @Override
      public int hashCode() {
        return Objects.hash(pos, dir);
      }
    }

    private final char[][] originalGrid;
    private final int height;
    private final int width;
    private Position startPos;
    private Direction startDir;

    public GuardLoopDetector(List<String> gridLines) {
      height = gridLines.size();
      width = height > 0 ? gridLines.get(0).length() : 0;
      originalGrid = new char[height][width];

      for (int y = 0; y < height; y++) {
        originalGrid[y] = gridLines.get(y).toCharArray();
        for (int x = 0; x < width; x++) {
          if (originalGrid[y][x] == '^') {
            startPos = new Position(x, y);
            startDir = Direction.UP;
          }
        }
      }
    }

    private boolean isValidPosition(int x, int y) {
      return x >= 0 && x < width && y >= 0 && y < height;
    }

    private boolean simulateWithObstacle(Position obstacle) {
      char[][] grid = new char[height][width];
      for (int y = 0; y < height; y++) {
        grid[y] = originalGrid[y].clone();
      }

      // Add the test obstacle
      grid[obstacle.y][obstacle.x] = '#';

      Position guardPos = startPos;
      Direction direction = startDir;
      Set<State> visited = new HashSet<>();

      while (true) {
        // Record current state
        State currentState = new State(guardPos, direction);
        if (!visited.add(currentState)) {
          return true; // Found a loop
        }

        // Calculate next position
        int nextX = guardPos.x + direction.x;
        int nextY = guardPos.y + direction.y;

        // Check if we're leaving the grid
        if (!isValidPosition(nextX, nextY)) {
          return false;
        }

        // Check for obstacle
        if (grid[nextY][nextX] == '#') {
          direction = direction.turnRight();
        } else {
          guardPos = new Position(nextX, nextY);
        }
      }
    }

    public Set<Position> findLoopPositions() {
      Set<Position> loopPositions = new HashSet<>();

      // Try adding an obstacle at each empty position
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          // Skip positions that already have obstacles or the guard's start
          if (originalGrid[y][x] == '#' || (x == startPos.x && y == startPos.y)) {
            continue;
          }

          Position testPos = new Position(x, y);
          if (simulateWithObstacle(testPos)) {
            loopPositions.add(testPos);
          }
        }
      }

      return loopPositions;
    }

    public void printWithLoopPositions(Set<Position> loopPositions) {
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          if (originalGrid[y][x] == '#') {
            System.out.print('#');
          } else if (originalGrid[y][x] == '^') {
            System.out.print('^');
          } else if (loopPositions.contains(new Position(x, y))) {
            System.out.print('O');
          } else {
            System.out.print('.');
          }
        }
        System.out.println();
      }
    }
  }

}
