package simulation.engine;

public record SimProcess(Runnable action, int frequency) {
}
