package simulation.engine;

import java.util.ArrayList;
import java.util.List;

public class SimUnit {
    protected Position position;
    private final List<SimWork> workQueue = new ArrayList<>();
    protected int visibilityRange;
    protected List<SimUnit> visibleUnits = new ArrayList<>();

    protected final void addWork(Runnable action, int frequency) {
        workQueue.add(new SimWork(action, frequency));
    }

    public final void runStep(int currentStep) {

        for (SimWork work : workQueue) {
            if (currentStep % work.frequency() == 0) {
                work.action().run();
            }
        }
    }

    public final void updateVisibleUnits(List<SimUnit> visibleUnits) {
        this.visibleUnits = visibleUnits;
        for (SimUnit unit : visibleUnits) {
            notifyVisibility(unit);
        }
    }

    protected final void notifyVisibility(SimUnit unit) {
        System.out.println(this + " zauważył " + unit);
    }

}
