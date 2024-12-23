import simulation.engine.*;
import simulation.engine.map.SimMap;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        List<SimGroup> groups = new ArrayList<>();
//        SimGroup group = new DeltaForce();
//        groups.add(group);

        groups.add(new RandomForce("Alpha Force", new SimPosition(12, 12), SimForceType.BLUFORCE));
        groups.add(new RandomForce("Bravo Force", new SimPosition(15, 15), SimForceType.BLUFORCE));
        groups.add(new RandomForce("Charlie Force", new SimPosition(20, 20), SimForceType.BLUFORCE));
        groups.add(new RandomForce("Echo Force", new SimPosition(25, 13), SimForceType.BLUFORCE));
        groups.add(new RandomForce("Foxtrot Force", new SimPosition(13, 25), SimForceType.REDFORCE));
        groups.add(new RandomForce("Golf Force", new SimPosition(27, 15), SimForceType.REDFORCE));
        groups.add(new RandomForce("Hotel Force", new SimPosition(15, 27), SimForceType.REDFORCE));
        groups.add(new RandomForce("India Force", new SimPosition(22, 22), SimForceType.REDFORCE));


        Random random = new Random();

        for (int i = 1; i <= 100; i++) {
            int x = random.nextInt(501); // Losowa wartość x w zakresie 0-500
            int y = random.nextInt(501); // Losowa wartość y w zakresie 0-500
            groups.add(new RandomForce("Ally " + i, new SimPosition(x, y), SimForceType.BLUFORCE));
        }

        for (int i = 1; i <= 100; i++) {
            int x = random.nextInt(501); // Losowa wartość x w zakresie 0-500
            int y = random.nextInt(501); // Losowa wartość y w zakresie 0-500
            groups.add(new RandomForce("Enemy " + i, new SimPosition(x, y), SimForceType.REDFORCE));
        }






        SimCore simulation = new SimCore(groups);

        simulation.setMap(new SimMap(MapGenerator.generate()));

        // Tworzenie okna
        JFrame frame = new JFrame("SimNG");
        SimulationPanel panel = new SimulationPanel(groups);
        frame.add(panel);
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        simulation.startSimulation();
        int counter = 1000000;
        while (counter > 0) {
            counter--;
            try {
                Thread.sleep(1);
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
