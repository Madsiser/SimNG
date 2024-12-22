package simulation.engine.scheduler;

public record SimProcess(Runnable action, int frequency){}
