package simulation.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SimCore {
    private final List<SimGroup> groups;
    private int currentStep = 0;

    public SimCore(List<SimGroup> groups) {
        this.groups = groups;
    }

    public void runSimulation() {
        while (true) {
            currentStep++;
            System.out.println("------------"+currentStep+"------------");
            for (SimGroup group : groups) {
                List<SimGroup> visibleGroups = getVisibleGroups(group);
                group.updateVisibleGroups(visibleGroups);
                group.runStep(currentStep);
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private List<SimGroup> getVisibleGroups(SimGroup group) {
        List<SimGroup> visibleGroups = new ArrayList<>();
        for (SimGroup otherGroup : this.groups) {
            if (!otherGroup.equals(group) && group.getViewRange() > group.position.distanceTo(otherGroup.position)) {
                visibleGroups.add(otherGroup);
            }
        }
        return visibleGroups;
    }
}
