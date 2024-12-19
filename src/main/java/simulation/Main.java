package simulation;

import simulation.engine.SimCore;
import simulation.engine.SimUnit;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<SimUnit> units = new ArrayList<>();
        units.add(new Tank());

        SimCore simulation = new SimCore(units);
        simulation.runSimulation();
    }
}
