package simulation.engine;

import simulation.engine.commands.Command;

import java.util.ArrayList;
import java.util.List;

public class SimGroup {
    protected Position position;
    private final String name;
    private final List<SimUnit> units = new ArrayList<>();
    private final List<Command> commandQueue = new ArrayList<>();
    private final List<SimWork> workQueue = new ArrayList<>();
    protected List<SimGroup> visibleGroups = new ArrayList<>();
    protected Command currentOrder;

    public SimGroup(String name, Position position) {
        this.name = name;
        this.position = position;
    }

    public void addUnit(SimUnit unit) {
        units.add(unit);
    }

    public void removeUnit(SimUnit unit) {
        units.remove(unit);
    }

    public void assignCommand(Command command) {
        commandQueue.add(command);
    }

    public Integer getViewRange(){
        Integer maxViewRange = Integer.MIN_VALUE;

        for (SimUnit unit : units) {
            Integer currentViewRange = unit.viewRange;
            if (currentViewRange > maxViewRange) {
                maxViewRange = currentViewRange;
            }
        }

        if (maxViewRange == Integer.MIN_VALUE) {
            return -1;
        }

        return maxViewRange;
    }

    public List<SimUnit> getUnits() {
        return new ArrayList<>(units);
    }

    protected final void addWork(Runnable action, int frequency) {
        workQueue.add(new SimWork(action, frequency));
    }

    public final void runStep(int currentStep) {

        for (SimWork work : workQueue) {
            if (currentStep % work.frequency() == 0) {
                work.action().run();
            }
        }
    }

    public final void updateVisibleGroups(List<SimGroup> visibleGroups) {
        this.visibleGroups = visibleGroups;
        this.visibleGroups.forEach(this::notifyVisibility);
    }

    protected final void notifyVisibility(SimGroup group) {
        System.out.println(this + " zauważył " + group);
    }

    @Override
    public String toString() {
        return "SimGroup{" +
                "name='" + name + '\'' +
                ", units=" + units +
                '}';
    }
}
