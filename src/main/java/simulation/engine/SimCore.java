package simulation.engine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SimCore {
    private final List<SimGroup> groups;
    private final List<SimGroup> destroyedGroups = new ArrayList<>();
    private int currentStep = 0;
    private volatile boolean running = true;
    private Thread gameThread;
    private volatile boolean paused = false;
    private final int TIMEOFONESTEP = 100;

    public SimCore(List<SimGroup> groups) {
        this.groups = groups;
    }

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

            runStep(currentStep);
            visibleStep(currentStep);
            clearanceStep(currentStep);

            long elapsedTime = System.currentTimeMillis() - startTime;
            long sleepTime = TIMEOFONESTEP - elapsedTime;

            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            } else {
                System.out.println("Przekroczono " + TIMEOFONESTEP + " ms w kroku: " + currentStep + " Czas wykonywania: " + elapsedTime + "ms");
            }
        }
    }

    private void runStep(int currentStep){
        for (SimGroup group : groups) {
            group.runStep(currentStep);
            List<SimGroup> visibleGroups = getVisibleGroups(group);
            group.updateVisibleGroups(visibleGroups);
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

    public List<SimGroup> getGroups() {
        return groups;
    }
}
