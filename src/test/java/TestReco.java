import pl.simNG.SimPosition;
import pl.simNG.map.Node;
import pl.simNG.map.SimMap;

import java.util.List;

public class TestReco {
    public static void main(String[] args) {
        SimMap simMap = getSimMap();

        SimPosition start = new SimPosition(0,0);
        SimPosition recoPoint1 = new SimPosition(1,7);
        SimPosition recoPoint2 = new SimPosition(6,7);
        SimPosition stop = new SimPosition(0,9);

        SimPosition[] recoPath = new SimPosition[]{recoPoint1, recoPoint2};

        List<Node> path = simMap.calculateRoute(start, stop, recoPath);

        if (path.isEmpty()) {
            System.out.println("No path found.");
        } else {
            System.out.println("Path found:");
            for (Node node : path) {
                System.out.println("(" + node.row + ", " + node.col + ")");
            }
        }
    }

    private static SimMap getSimMap() {
        int[][] grid = {
                {3, 7, 0, 10, 2, 5, 8, 1, 6, 4},
                {9, 2, 6, 1, 7, 0, 3, 10, 5, 8},
                {4, 8, 10, 5, 6, 9, 2, 7, 0, 1},
                {1, 5, 3, 7, 8, 4, 10, 6, 9, 2},
                {0, 10, 4, 2, 9, 6, 1, 3, 7, 5},
                {7, 6, 8, 9, 3, 10, 5, 4, 2, 0},
                {2, 1, 5, 6, 4, 7, 9, 8, 10, 3},
                {10, 3, 9, 0, 5, 2, 6, 7, 4, 8},
                {6, 4, 7, 8, 1, 3, 0, 5, 10, 9},
                {5, 9, 2, 4, 10, 8, 7, 0, 3, 6}
        };

        return new SimMap(grid);
    }
}
