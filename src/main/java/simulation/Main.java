package simulation;

import simulation.engine.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        List<SimGroup> groups = new ArrayList<>();
        SimGroup group = new DeltaForce();
        groups.add(group);

        groups.add(new RandomForce("Alpha Force", new SimPosition(12, 12), SimForceType.REDFORCE));
        groups.add(new RandomForce("Bravo Force", new SimPosition(15, 15), SimForceType.REDFORCE));
        groups.add(new RandomForce("Charlie Force", new SimPosition(20, 20), SimForceType.REDFORCE));
        groups.add(new RandomForce("Echo Force", new SimPosition(25, 13), SimForceType.REDFORCE));
        groups.add(new RandomForce("Foxtrot Force", new SimPosition(13, 25), SimForceType.REDFORCE));
        groups.add(new RandomForce("Golf Force", new SimPosition(27, 15), SimForceType.REDFORCE));
        groups.add(new RandomForce("Hotel Force", new SimPosition(15, 27), SimForceType.REDFORCE));
        groups.add(new RandomForce("India Force", new SimPosition(22, 22), SimForceType.REDFORCE));




        SimCore simulation = new SimCore(groups);

        // Tworzenie okna
        JFrame frame = new JFrame("SimNG");
        SimulationPanel panel = new SimulationPanel(groups);
        frame.add(panel);
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        simulation.startSimulation();
        int counter = 10000;
        while (counter > 0) {
            counter--;
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            List<SimGroup> allGroups = simulation.getGroups();
//            System.out.println("Current allGroups: " + allGroups);
            panel.updateGroups(allGroups); // Aktualizacja grup w panelu
        }

        simulation.stopSimulation();
    }
}
