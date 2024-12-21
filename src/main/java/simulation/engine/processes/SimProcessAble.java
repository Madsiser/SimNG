package simulation.engine.processes;

import java.util.HashMap;
import java.util.Map;

public abstract class SimProcessAble {

    protected final Map<String, SimProcess> processQueue = new HashMap<>();

    protected final void addProcess(String name, Runnable action, int frequency) {
        processQueue.put(name, new SimProcess(action, frequency));
    }

    public final void runStep(int currentStep) {
        for (Map.Entry<String, SimProcess> entry : processQueue.entrySet()) {
            SimProcess work = entry.getValue();
            if (currentStep % work.frequency() == 0) {
                work.action().run();
            }
        }

    }
}