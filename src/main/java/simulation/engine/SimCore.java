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

            long startTime = System.currentTimeMillis();
            currentStep++;
            // System.out.println("------------  " + currentStep + "  ------------");

            for (SimGroup group : groups) {
                group.runStep(currentStep);
                List<SimGroup> visibleGroups = getVisibleGroups(group);
                group.updateVisibleGroups(visibleGroups);
            }

            long elapsedTime = System.currentTimeMillis() - startTime;
            long sleepTime = 100 - elapsedTime;

            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            } else {
                System.out.println("Przekroczono 100 ms w kroku: " + currentStep + " Czas wykonywania: " + elapsedTime + "ms");
            }
        }
    }


    private List<SimGroup> getVisibleGroups(SimGroup group) {
        List<SimGroup> visibleGroups = new ArrayList<>();
        for (SimGroup otherGroup : this.groups) {
            if (!otherGroup.equals(group) && (otherGroup.forceType != group.forceType) && group.getViewRange()  > group.position.distanceTo(otherGroup.position)) {
                visibleGroups.add(otherGroup);
            }
        }
        return visibleGroups;
    }

    public List<SimGroup> getGroups() {
        return groups;
    }
}
