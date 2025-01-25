package pl.simNG;

import pl.simNG.commands.Command;
import pl.simNG.scheduler.SimExecutionScheduler;

import java.util.*;

public abstract class SimGroup extends SimExecutionScheduler {
    protected SimPosition position;
    private final String name;
    protected final List<SimUnit> units = new ArrayList<>();
    protected final List<SimUnit> destroyedUnits = new ArrayList<>();
    protected List<SimGroup> visibleGroups = new ArrayList<>();
    public Command currentCommand = null; //TODO Implementation commands execution
    protected LinkedList<SimVector2i> route = new LinkedList<>();
    private final SimForceType forceType;
    public SimCore parent = null;

    public SimGroup(String name, SimPosition position) {
        this.name = name;
        this.position = position;
        this.forceType = SimForceType.BLUFORCE;
    }
    public SimGroup(String name, SimPosition position, SimForceType forceType) {
        this.name = name;
        this.position = position;
        this.forceType = forceType;
    }

    public abstract void init();

    public boolean isDestroyed(){
        return units.isEmpty();
    }

    public SimPosition getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public Integer getRange(){
        return 0;
    }

    public void addUnit(SimUnit unit) {
        unit.setParent(this);
        units.add(unit);
    }

    public void removeUnit(SimUnit unit) {
        units.remove(unit);
    }

    public void assignCommand(Command command) {
        this.currentCommand = command;
    }

    public Integer getViewRange(){
        Integer maxViewRange = Integer.MIN_VALUE;

        for (SimUnit unit : units) {
            Integer currentViewRange = unit.visibilityRange;
            if (currentViewRange > maxViewRange) {
                maxViewRange = currentViewRange;
            }
        }

        if (maxViewRange == Integer.MIN_VALUE) {
            return -1;
        }

        return maxViewRange;
    }

    public int getSpeed() {
        return units.stream()
                .map(SimUnit::getSpeed)
                .min(Integer::compareTo)
                .orElse(1);
    }

    public List<SimUnit> getUnits() {
        return new ArrayList<>(units);
    }

    protected void move(){
        SimVector2i direction = route.poll();
        if (direction != null){
            this.position.add(direction);
        }
    }

    protected void cleanDestroyedUnits() {
        Iterator<SimUnit> iterator = units.iterator();
        while (iterator.hasNext()) {
            SimUnit unit = iterator.next();
            if (unit.isDestroyed()) {
                destroyedUnits.add(unit);
                iterator.remove();
            }
        }
    }

    public final void updateVisibleGroups(List<SimGroup> visibleGroups) {
        this.visibleGroups = visibleGroups;
    }

    protected LinkedList<SimVector2i> calculateRouteTo(SimPosition target){
        return parent.getMap().calculateRoute(position, target);
    }

    public abstract void applyDamage(SimGroup attacker, SimUnit targetUnit);

    @Override
    public String toString() {
        return "SimGroup{" +
                "name='" + name + '\'' +
                ", pos=" + position +
                '}';
    }

    public SimForceType getForceType() {
        return forceType;
    }

    public List<SimGroup> getVisibleGroups() {
        return visibleGroups;
    }
}
