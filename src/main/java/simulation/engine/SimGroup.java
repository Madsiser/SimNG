package simulation.engine;

import simulation.engine.commands.Command;
import simulation.engine.processes.SimProcessAble;

import java.util.*;

public class SimGroup extends SimProcessAble {
    protected SimPosition position;
    private final String name;
    private final List<SimUnit> units = new ArrayList<>();
    private final List<Command> commandQueue = new ArrayList<>();
    protected List<SimGroup> visibleGroups = new ArrayList<>();
    protected Command currentOrder;
    protected LinkedList<SimVector2i> route = new LinkedList<>();

    public SimGroup(String name, SimPosition position) {
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

    public Integer getSpeed(){
        Integer minViewRange = Integer.MAX_VALUE;

        for (SimUnit unit : units) {
            Integer currentViewRange = unit.viewRange;
            if (currentViewRange < minViewRange) {
                minViewRange = currentViewRange;
            }
        }

        if (minViewRange == Integer.MAX_VALUE) {
            return -1;
        }

        return minViewRange;
    }

    public List<SimUnit> getUnits() {
        return new ArrayList<>(units);
    }

    protected void move(){
        SimVector2i direction = route.poll();
        if (direction != null){
            this.position.add(direction);
        }
        System.out.println(this.position);
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
