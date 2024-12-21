package simulation.engine.processes;

public class SimTask {

    Runnable action;
    int nextOperation;

    SimTask(Runnable action, int nextOperation) {
        this.action = action;
        this.nextOperation = nextOperation;
    }

}
