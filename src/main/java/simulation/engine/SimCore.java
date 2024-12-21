package simulation.engine;

import java.util.ArrayList;
import java.util.List;

public class SimCore {
    private final List<SimGroup> groups;
    private int currentStep = 0;
    private volatile boolean running = true;
    private Thread gameThread;
    private volatile boolean paused = false;

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

    public void runSimulation() {
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
            currentStep++;
//            System.out.println("------------  " + currentStep + "  ------------");
            for (SimGroup group : groups) {
                group.runStep(currentStep);

                List<SimGroup> visibleGroups = getVisibleGroups(group);
                group.updateVisibleGroups(visibleGroups);
            }
            try {
                Thread.sleep(1);
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

    public List<SimGroup> getGroups() {
        return groups;
    }
}
