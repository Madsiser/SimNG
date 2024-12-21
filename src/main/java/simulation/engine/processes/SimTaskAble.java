package simulation.engine.processes;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public abstract class SimTaskAble {

    protected final LinkedList<SimTask> taskQueue = new LinkedList<>();
    protected final LinkedList<SimTask> buforTaskQueue = new LinkedList<>();

    protected final void addTask(Runnable action, int nextCall) {
        buforTaskQueue.push(new SimTask(action, nextCall));
    }

    private final void update() {
        taskQueue.addAll(buforTaskQueue);
        buforTaskQueue.clear();
    }


    public void runStep(int currentStep) {
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
    }
}
