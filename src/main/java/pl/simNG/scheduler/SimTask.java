package pl.simNG.scheduler;

/**
 * Klasa reprezentująca jednorazowe zadanie w harmonogramie symulacji.
 * Zadanie jest wykonywane po określonej liczbie kroków symulacji.
 */
public class SimTask {
    /** Akcja do wykonania w zadaniu. */
    Runnable action;
    /** Liczba kroków symulacji, po których zadanie zostanie wykonane. */
    int nextOperation;

    /**
     * Konstruktor klasy SimTask.
     * @param action akcja do wykonania
     * @param nextOperation liczba kroków do wykonania zadania
     */
    SimTask(Runnable action, int nextOperation) {
        this.action = action;
        this.nextOperation = nextOperation;
    }
}
