package simulation;

import simulation.engine.SimForceType;
import simulation.engine.SimGroup;
import simulation.engine.SimPosition;
import simulation.engine.SimUnit;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SimulationPanel extends JPanel {
    private List<SimGroup> groups;

    public SimulationPanel(List<SimGroup> groups) {
        this.groups = groups;
    }

    public void updateGroups(List<SimGroup> groups) {
        this.groups = groups;
        repaint(); // Odświeżenie panelu
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (SimGroup group : groups) {
            SimPosition pos = group.getPosition();
            int rectWidth = 15;
            int rectHeight = 15;

            // Ustawienie koloru prostokąta
            if (group.forceType == SimForceType.REDFORCE){
                g.setColor(Color.RED);
            }else{
                g.setColor(Color.BLUE);
            }
            // Rysowanie prostokąta z uwzględnieniem środka
            g.fillRect((int) pos.getX() * 20 - rectWidth / 2, (int) pos.getY() * 20 - rectHeight / 2, rectWidth, rectHeight);
            // Rysowanie nazwy grupy
            g.drawString(group.getName(), (int) pos.getX() * 20-10, (int) pos.getY() * 20 - 10);

            // Rysowanie zasięgu jednostek
            if (group.visibleGroups.isEmpty()){
                g.setColor(Color.GREEN);
            }else{
                g.setColor(Color.BLUE);
            }

            for (SimUnit unit : group.getUnits()) {
                int range = unit.viewRange*20-10;

                // Rysowanie koła zasięgu
                g.drawOval((int) pos.getX() * 20 - range, (int) pos.getY() * 20 - range, range * 2, range * 2);
            }
//            g.setColor(Color.RED);
//            for (SimUnit unit : group.getUnits()) {
//                int range = unit.shotRange*20-10;
//
//                // Rysowanie koła zasięgu
//                g.drawOval((int) pos.getX() * 20 - range, (int) pos.getY() * 20 - range, range * 2, range * 2);
//            }
        }
    }

}
