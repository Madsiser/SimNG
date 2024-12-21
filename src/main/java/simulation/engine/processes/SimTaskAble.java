package simulation.engine.processes;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class SimTaskAble {

    protected final Map<String, SimTask> taskQueue = new HashMap<>();

    protected final void addTask(String name, Runnable action, int frequency) {
        taskQueue.put(name, new SimTask(action, frequency));
    }

    public final void runStep(int currentStep) {
        Iterator<Map.Entry<String, SimTask>> iterator = taskQueue.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, SimTask> entry = iterator.next();
            SimTask task = entry.getValue();

            if (task.nextOperation == 0) {
                task.action.run();
                iterator.remove();
            } else {
                task.nextOperation--;
            }
        }
    }
}
