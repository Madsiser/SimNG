package pl.simNG.scheduler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * Abstrakcyjna klasa odpowiedzialna za zarządzanie harmonogramem procesów i zadań w symulacji.
 * Obsługuje cykliczne procesy (SimProcess) i jednorazowe zadania (SimTask).
 */
public abstract class SimExecutionScheduler {
    /** Kolejka procesów cyklicznych z nazwami jako kluczami. */
    protected final Map<String, SimProcess> processQueue = new HashMap<>();
    /** Kolejka główna zadań do wykonania. */
    protected final LinkedList<SimTask> taskQueue = new LinkedList<>();
    /** Bufor zadań, które mają zostać dodane do głównej kolejki w następnym kroku symulacji. */
    protected final LinkedList<SimTask> buforTaskQueue = new LinkedList<>();

    /**
     * Dodaje proces do harmonogramu.
     * @param name unikalna nazwa procesu
     * @param action akcja do wykonania w procesie
     * @param frequency częstotliwość wykonywania procesu (w krokach symulacji)
     */
    protected final void addProcess(String name, Runnable action, int frequency) {
        processQueue.put(name, new SimProcess(action, frequency));
    }

    /**
     * Usuwa proces z harmonogramu.
     * @param name nazwa procesu do usunięcia
     */
    protected final void removeProcess(String name) {
        processQueue.remove(name);
    }

    /**
     * Dodaje jednorazowe zadanie do bufora zadań.
     * Zadanie zostanie przeniesione do głównej kolejki w następnym kroku symulacji.
     * @param action akcja do wykonania
     * @param nextCall liczba kroków symulacji do wykonania zadania
     */
    protected final void addTask(Runnable action, int nextCall) {
        buforTaskQueue.push(new SimTask(action, nextCall));
    }

    /**
     * Aktualizuje kolejkę zadań, przenosząc zadania z bufora do głównej kolejki.
     */
    private final void update() {
        taskQueue.addAll(buforTaskQueue);
        buforTaskQueue.clear();
    }

    /**
     * Wykonuje jeden krok symulacji, przetwarzając zadania i procesy.
     * @param currentStep bieżący krok symulacji
     */
    public final void runStep(int currentStep) {
        update();
        Iterator<SimTask> iterator = taskQueue.iterator();
        while (iterator.hasNext()) {
            SimTask task = iterator.next();
            task.nextOperation--;
            if (task.nextOperation <= 1) {
                task.action.run();
                iterator.remove();
            }
        }

        for (Map.Entry<String, SimProcess> entry : processQueue.entrySet()) {
            SimProcess work = entry.getValue();
            if (currentStep % work.frequency() == 0) {
                work.action().run();
            }
        }
    }
}
