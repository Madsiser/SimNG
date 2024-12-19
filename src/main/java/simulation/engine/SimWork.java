package simulation.engine;

public class SimWork {
    private final Runnable action;
    private final int frequency;

    public SimWork(Runnable action, int frequency) {
        this.action = action;
        this.frequency = frequency;
    }

    public Runnable getAction() {
        return action;
    }

    public int getFrequency() {
        return frequency;
    }
}
