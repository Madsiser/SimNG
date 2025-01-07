package pl.simNG.scheduler;

public record SimProcess(Runnable action, int frequency){}
