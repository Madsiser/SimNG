package pl.simNG.map;

import pl.simNG.SimPosition;
import pl.simNG.SimVector2i;

import java.util.*;

/**
 * Klasa reprezentująca mapę symulacji.
 * Jest odpowiedzialna za wyznaczanie tras między punktami.
 * Posiada algorytm wyszukiwania najlepszej drogi A*.
 */
public class SimMap {
    /** Tablica reprezentująca siatkę mapy, gdzie każda komórka opisuje trudność terenu lub dostępność. */
    int[][] map;

    public SimMap(int[][] map){
        this.map = map;
    }

    /**
     * Oblicza trasę między dwiema pozycjami na mapie za pomocą algorytmu A*.
     * @param startPosition pozycja początkowa
     * @param stopPosition pozycja docelowa
     * @return lista wektorów opisujących kierunki ruchu
     */
    public LinkedList<SimVector2i> calculateRoute(SimPosition startPosition, SimPosition stopPosition) {
        List<Node> path = aStarSearch(map,startPosition.toIntArray(), stopPosition.toIntArray());
         return convertPathToDirections(path);
    }

    // parametry do testow pozniej wyrzucic
    public List<Node> calculateRoute(SimPosition startPosition, SimPosition stopPosition, SimPosition... recoPath) {
        List<Node> fullPath = new ArrayList<>();
        SimPosition currentPosition = startPosition;

        if (recoPath != null) {
            for (SimPosition recoPoint : recoPath) {
                SimPosition bestRecoPoint = findBestAround(recoPoint);

                List<Node> pathSegment = aStarSearch(map, currentPosition.toIntArray(), bestRecoPoint.toIntArray());
                fullPath.addAll(pathSegment);

                currentPosition = bestRecoPoint;
            }
        }

        List<Node> lastPathSegment = aStarSearch(map, currentPosition.toIntArray(), stopPosition.toIntArray());
        fullPath.addAll(lastPathSegment);

        return fullPath;
    }

    // parametry do testow pozniej wyrzucic
    private SimPosition findBestAround(SimPosition recoPoint) {
        int x = (int) recoPoint.getX();
        int y = (int) recoPoint.getY();
        int n = map.length;

        int bestX = x;
        int bestY = y;
        int bestValue = map[x][y];

        for (int i = x - 3; i <= x + 3 ; i++) {
            for (int j = y - 3; j <= y + 3 ; j++) {
                if (i >= 0 && i < n && j>= 0 && j < map[0].length) {
                    if (map[i][j] != 0 && map[i][j] < bestValue) {
                        bestValue = map[i][j];
                        bestX = i;
                        bestY = j;
                    }
                }
            }
        }

        recoPoint.setX(bestX);
        recoPoint.setY(bestY);
        System.out.println("bestX: " + bestX + " bestY: " + bestY);

        return recoPoint;
    }

    /** Możliwe kierunki ruchu na mapie (północ, południe, wschód, zachód). */
    private static final int[][] DIRECTIONS = {
            {1, 0}, {0, 1}, {-1, 0}, {0, -1}, // N/S/E/W
            //{1, 1}, {1, -1}, {-1, 1}, {-1, -1} // Diagonals
    };

    /**
     * Implementacja algorytmu A* do znajdowania najkrótszej trasy na mapie.
     * @param grid dwuwymiarowa tablica reprezentująca mapę
     * @param start pozycja początkowa
     * @param end pozycja docelowa
     * @return lista węzłów opisujących trasę
     */
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

    /**
     * Odtwarza ścieżkę na podstawie końcowego węzła.
     * @param endNode ostatni węzeł trasy
     * @return lista węzłów opisujących trasę
     */
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

    /**
     * Funkcja heurystyczna dla algorytmu A*.
     * Oblicza przybliżony koszt dotarcia do celu.
     * @param current obecna pozycja
     * @param end pozycja docelowa
     * @return wartość heurystyczna
     */
    private static double heuristic(int[] current, int[] end) {
        return Math.abs(current[0] - end[0]) + Math.abs(current[1] - end[1]);
    }

    /**
     * Sprawdza, czy komórka na mapie jest przejezdna.
     * @param grid dwuwymiarowa tablica reprezentująca mapę
     * @param row wiersz komórki
     * @param col kolumna komórki
     * @return true, jeśli komórka jest przejezdna, false w p.p.
     */
    private static boolean isValidCell(int[][] grid, int row, int col) {
        return row >= 0 && row < grid.length && col >= 0 && col < grid[0].length && grid[row][col] > 0;
    }

    /**
     * Konwertuje listę węzłów na listę kierunków ruchu.
     * @param path lista węzłów
     * @return lista wektorów opisujących kierunki ruchu
     */
    public static LinkedList<SimVector2i> convertPathToDirections(List<Node> path) {
        LinkedList<SimVector2i> directions = new LinkedList<>();
        for (int i = 1; i < path.size(); i++) {
            Node prev = path.get(i - 1);
            Node curr = path.get(i);

            int dRow = curr.row - prev.row;
            int dCol = curr.col - prev.col;

            if (dRow == 0 && dCol == -1) {
                directions.add(SimVector2i.DOWN);
            } else if (dRow == 0 && dCol == 1) {
                directions.add(SimVector2i.UP);
            } else if (dRow == -1 && dCol == 0) {
                directions.add(SimVector2i.LEFT);
            } else if (dRow == 1 && dCol == 0) {
                directions.add(SimVector2i.RIGHT);
            }
        }
        return directions;
    }
}
