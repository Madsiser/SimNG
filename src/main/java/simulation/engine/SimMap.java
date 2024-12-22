package simulation.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class SimMap {
    private int[][] costMap; // Tablica kosztów przejścia
    private static final int IMPASSABLE_COST = Integer.MAX_VALUE; // Koszt dla nieprzejezdnych kwadracików

    public SimMap(int width, int height) {
        costMap = new int[width][height];
    }

    public void setCost(int x, int y, int cost) {
        costMap[x][y] = cost;
    }

    public void setImpassable(int x, int y) {
        costMap[x][y] = IMPASSABLE_COST; // Ustawienie kwadracika jako nieprzejezdnego
    }

    public int getCost(int x, int y) {
        return costMap[x][y];
    }

    public List<SimVector2i> calculateRoute(SimPosition startPosition, SimPosition stopPosition) {
        PriorityQueue<SimNode> openSet = new PriorityQueue<>();
        Map<SimPosition, SimPosition> cameFrom = new HashMap<>();
        Map<SimPosition, Integer> gScore = new HashMap<>();
        Map<SimPosition, Integer> fScore = new HashMap<>();

        gScore.put(startPosition, 0);
        fScore.put(startPosition, heuristic(startPosition, stopPosition));
        openSet.add(new SimNode(startPosition, fScore.get(startPosition)));

        while (!openSet.isEmpty()) {
            SimPosition current = openSet.poll().position;

            if (current.equals(stopPosition)) {
                return reconstructPath(cameFrom, current);
            }

            for (SimVector2i direction : SimVector2i.values()) {
                SimPosition neighbor = new SimPosition(current.getX() + direction.x, current.getY() + direction.y);
                if (isValidPosition(neighbor)) {
                    int tentativeGScore = gScore.get(current) + getCost(neighbor.getX(), neighbor.getY());

                    if (tentativeGScore < gScore.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                        cameFrom.put(neighbor, current);
                        gScore.put(neighbor, tentativeGScore);
                        fScore.put(neighbor, tentativeGScore + heuristic(neighbor, stopPosition));

                        if (!openSet.contains(new SimNode(neighbor, fScore.get(neighbor)))) {
                            openSet.add(new SimNode(neighbor, fScore.get(neighbor)));
                        }
                    }
                }
            }
        }

        return new ArrayList<>(); // Brak trasy
    }

    private boolean isValidPosition(SimPosition position) {
        return position.getX() >= 0 && position.getX() < costMap.length &&
                position.getY() >= 0 && position.getY() < costMap[0].length &&
                getCost(position.getX(), position.getY()) != IMPASSABLE_COST;
    }

    private int heuristic(SimPosition a, SimPosition b) {
        // Używamy odległości Manhattan jako heurystyki
        return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
    }

    private List<SimVector2i> reconstructPath(Map<SimPosition, SimPosition> cameFrom, SimPosition current) {
        List<SimVector2i> totalPath = new ArrayList<>();
        while (cameFrom.containsKey(current)) {
            SimPosition prev = cameFrom.get(current);
            totalPath.add(SimVector2i.fromPositions(prev, current));
            current = prev;
        }
        // Odwróć ścieżkę, aby mieć ją w odpowiedniej kolejności
        List<SimVector2i> reversedPath = new ArrayList<>();
        for (int i = totalPath.size() - 1; i >= 0; i--) {
            reversedPath.add(totalPath.get(i));
        }
        return reversedPath;
    }
}
