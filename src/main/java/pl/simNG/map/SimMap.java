package pl.simNG.map;

import pl.simNG.SimPosition;
import pl.simNG.SimVector2i;

import java.util.*;

public class SimMap {

    int[][] map;

    public SimMap(int[][] map){
        this.map = map;
    }

    public LinkedList<SimVector2i> calculateRoute(SimPosition startPosition, SimPosition stopPosition) {
        List<Node> path = aStarSearch(map,startPosition.toArray(), stopPosition.toArray());
         return convertPathToDirections(path);
    }

    private static final int[][] DIRECTIONS = {
            {1, 0}, {0, 1}, {-1, 0}, {0, -1}, // N/S/E/W
            //{1, 1}, {1, -1}, {-1, 1}, {-1, -1} // Diagonals
    };

    public static List<Node> aStarSearch(int[][] grid, int[] start, int[] end) {
        PriorityQueue<Node> openList = new PriorityQueue<>();
        boolean[][] closedList = new boolean[grid.length][grid[0].length];

        Node startNode = new Node(start[0], start[1], 0, heuristic(start, end), null);
        openList.add(startNode);

        while (!openList.isEmpty()) {
            Node current = openList.poll();

            if (current.row == end[0] && current.col == end[1]) {
                return reconstructPath(current);
            }

            closedList[current.row][current.col] = true;

            for (int[] dir : DIRECTIONS) {
                int newRow = current.row + dir[0];
                int newCol = current.col + dir[1];

                if (isValidCell(grid, newRow, newCol) && !closedList[newRow][newCol]) {
                    double newGCost = current.gCost + grid[newRow][newCol];
                    Node neighbor = new Node(newRow, newCol, newGCost, heuristic(new int[]{newRow, newCol}, end), current);

                    if (openList.stream().noneMatch(n -> n.row == newRow && n.col == newCol && n.getFCost() <= neighbor.getFCost())) {
                        openList.add(neighbor);
                    }
                }
            }
        }

        return Collections.emptyList(); // No path found
    }

    private static List<Node> reconstructPath(Node endNode) {
        List<Node> path = new ArrayList<>();
        Node current = endNode;
        while (current != null) {
            path.add(current);
            current = current.parent;
        }
        Collections.reverse(path);
        return path;
    }

    private static double heuristic(int[] current, int[] end) {
        return Math.abs(current[0] - end[0]) + Math.abs(current[1] - end[1]);
    }

    private static boolean isValidCell(int[][] grid, int row, int col) {
        return row >= 0 && row < grid.length && col >= 0 && col < grid[0].length && grid[row][col] > 0;
    }

    public static LinkedList<SimVector2i> convertPathToDirections(List<Node> path) {
        LinkedList<SimVector2i> directions = new LinkedList<>();
        for (int i = 1; i < path.size(); i++) {
            Node prev = path.get(i - 1);
            Node curr = path.get(i);

            int dRow = curr.row - prev.row;
            int dCol = curr.col - prev.col;

            if (dRow == 0 && dCol == -1) {
                directions.add(SimVector2i.UP);
            } else if (dRow == 0 && dCol == 1) {
                directions.add(SimVector2i.DOWN);
            } else if (dRow == -1 && dCol == 0) {
                directions.add(SimVector2i.LEFT);
            } else if (dRow == 1 && dCol == 0) {
                directions.add(SimVector2i.RIGHT);
            }
        }
        return directions;
    }


}
