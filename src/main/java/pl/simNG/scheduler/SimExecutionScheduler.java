package pl.simNG.scheduler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public abstract class SimExecutionScheduler {
    protected final Map<String, SimProcess> processQueue = new HashMap<>();

    protected final LinkedList<SimTask> taskQueue = new LinkedList<>();
    protected final LinkedList<SimTask> buforTaskQueue = new LinkedList<>();

    protected final void addProcess(String name, Runnable action, int frequency) {
        processQueue.put(name, new SimProcess(action, frequency));
    }
    
    protected final void removeProcess(String name) {
        processQueue.remove(name);
    }

    protected final void addTask(Runnable action, int nextCall) {
        buforTaskQueue.push(new SimTask(action, nextCall));
    }

    private final void update() {
        taskQueue.addAll(buforTaskQueue);
        buforTaskQueue.clear();
    }

    public final void runStep(int currentStep) {
        update();
        Iterator<SimTask> iterator = taskQueue.iterator();
        while (iterator.hasNext()) {
            SimTask task = iterator.next();
            task.nextOperation--;
            if (task.nextOperation <= 1) {
                task.action.run();
                iterator.remove();
            }
        }

        for (Map.Entry<String, SimProcess> entry : processQueue.entrySet()) {
            SimProcess work = entry.getValue();
            if (currentStep % work.frequency() == 0) {
                work.action().run();
            }
        }
    }
}
