package pl.simNG.scheduler;

public class SimTask {

    Runnable action;
    int nextOperation;

    SimTask(Runnable action, int nextOperation) {
        this.action = action;
        this.nextOperation = nextOperation;
    }

}
