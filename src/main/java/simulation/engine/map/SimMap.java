package simulation.engine.map;

import simulation.engine.SimPosition;
import simulation.engine.SimVector2i;

import java.util.*;

public class SimMap {
    private int[][] costMap;
    private static final int IMPASSABLE_COST = Integer.MAX_VALUE;

    public SimMap(int width, int height) {
        costMap = new int[width][height];
    }

    public void setCost(int x, int y, int cost) {
        costMap[x][y] = cost;
    }

    public void setImpassable(int x, int y) {
        costMap[x][y] = IMPASSABLE_COST;
    }

    public int getCost(int x, int y) {
        return costMap[x][y];
    }

    public List<SimVector2i> calculateRoute(SimPosition startPosition, SimPosition stopPosition) {
        //TODO "Implementation of A*
        return new ArrayList<>();
    }
}
