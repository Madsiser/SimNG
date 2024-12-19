package simulation;

import simulation.engine.*;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<SimGroup> groups = new ArrayList<>();
        SimGroup group = new DeltaForce(new Position(1,1));
        group.addUnit(new Tank(SimForceType.BLUFORCE,10,10,1,5));
        groups.add(group);

        group = new SimGroup("Alpha Force", new Position(1,8));
        group.addUnit(new Tank(SimForceType.REDFORCE,1,1,1,5));
        groups.add(group);

        SimCore simulation = new SimCore(groups);
        simulation.runSimulation();
    }
}
