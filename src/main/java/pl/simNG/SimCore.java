package pl.simNG;

import pl.simNG.map.SimMap;
import pl.simNG.scheduler.SimExecutionScheduler;

import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SimCore {
    private SimMap map = null;
    private List<SimExecutionScheduler> simObjects = new ArrayList<>();
    private final List<SimGroup> groups;
    private final List<SimGroup> destroyedGroups = new ArrayList<>();
    private final List<SimGroup> modifingBufforGroups = new ArrayList<>();
    private int currentStep = 0;
    private volatile boolean running = true;
    private Thread gameThread;
    private volatile boolean paused = false;
    private int timeOfOneStep = 100;

    public SimCore() {
        this.groups = new ArrayList<>();
    }

    private void runSimulation() {
        while (running) {
            if (paused) {
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }

            long startTime = System.currentTimeMillis();
            currentStep++;

            groupModifications(currentStep);
            runStep(currentStep);
            visibleStep(currentStep);
            clearanceStep(currentStep);

            long elapsedTime = System.currentTimeMillis() - startTime;
            long sleepTime = timeOfOneStep - elapsedTime;

            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            } else {
                System.out.println("Przekroczono " + timeOfOneStep + " ms w kroku: " + currentStep + " Czas wykonywania: " + elapsedTime + "ms");
            }
        }
    }

    private void groupModifications(int currentStep) {

    }
    private void runStep(int currentStep){
        for (SimExecutionScheduler simObject : simObjects) {
            simObject.runStep(currentStep);
        }
        for (SimGroup group : groups) {
            group.runStep(currentStep);
        }
    }
    private void visibleStep(int currentStep){
        for (SimGroup group : groups) {
            List<SimGroup> visibleGroups = getVisibleGroups(group);
            group.updateVisibleGroups(visibleGroups);
        }
    }
    private void clearanceStep(int currentStep){
        Iterator<SimGroup> iterator = groups.iterator();
        while (iterator.hasNext()) {
            SimGroup group = iterator.next();
            if (group.isDestroyed()){
                destroyedGroups.add(group);
                iterator.remove();
            }
        }
    }
    private List<SimGroup> getVisibleGroups(SimGroup group) {
        List<SimGroup> visibleGroups = new ArrayList<>();
        for (SimGroup otherGroup : this.groups) {
            if (!otherGroup.equals(group) && (otherGroup.getForceType() != group.getForceType()) && group.getViewRange()  > group.position.distanceTo(otherGroup.position)) {
                visibleGroups.add(otherGroup);
            }
        }
        return visibleGroups;
    }

    /// Methods for steering the simulation workflow.
    public void stopSimulation() {
        running = false;
        if (gameThread != null) {
            gameThread.interrupt();
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    public void pauseSimulation() {
        paused = true;
    }
    public void resumeSimulation() {
        paused = false;
        synchronized (this) {
            notify();
        }
    }
    public void startSimulation() {
        if (gameThread == null || !gameThread.isAlive()) {
            running = true;
            gameThread = new Thread(this::runSimulation);
            gameThread.start();
        }
    }

    /// Methods for the simulation parameters/works sets/gets.
    public void setTimeOfOneStep(int timeOfOneStep) {
        this.timeOfOneStep = timeOfOneStep;
    }
    public void addSimObjects(SimExecutionScheduler simObject) {
        this.simObjects.add(simObject);
    }
    public int getSimulationTime() {
        return currentStep;
    }

    /// Methods for steering the groups.
    public void addGroup(SimGroup group){
        group.parent = this;
        group.init();
        this.groups.add(group);
    }
    public List<SimGroup> getGroups() {
        return groups;
    }
    public boolean editGroup(SimGroup group) {
        modifingBufforGroups.add(group);
        for (int i = 0; i < groups.size(); i++) {
            if (groups.get(i).id == group.id) {
//                groups.set(i, group);
                return true;
            }
        }
        return false;
    }

    /// Methods for steering the map.
    public SimMap getMap() {
        return map;
    }
    public void setMap(SimMap map) {
        this.map = map;
    }

}
