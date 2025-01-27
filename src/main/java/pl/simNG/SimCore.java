package pl.simNG;

import pl.simNG.map.SimMap;
import pl.simNG.scheduler.SimExecutionScheduler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Klasa zarządzająca symulacją, odpowiedzialna za kontrolowanie grup, harmonogramowanie zadań oraz zarządzanie mapą.
 * Obsługuje przepływ symulacji krok po kroku, uwzględniając logikę grup oraz obiekty symulacyjne.
 */
public class SimCore {
    /** Obiekt reprezentujący mapę symulacji. */
    private SimMap map = null;
    /** Kolekcja obiektów wykonujących własne procesy podczas każdego kroku symulacji. */
    private List<SimExecutionScheduler> simObjects = new ArrayList<>();
    /** Lista jednostek (grup) biorących udział w symulacji. */
    private final List<SimGroup> groups;
    /** Lista zniszczonych jednostek (grup), które zostały usunięte z symulacji. */
    private final List<SimGroup> destroyedGroups = new ArrayList<>();
    /** Bufor jednostek (grup) poddawanych modyfikacjom. */
    private final List<SimGroup> modifingBufforGroups = new ArrayList<>();
    /** Licznik bieżącego kroku symulacji. */
    private int currentStep = 0;
    /** Flaga oznaczająca, czy symulacja jest uruchomiona. */
    private volatile boolean running = true;
    /** Wątek odpowiedzialny za działanie symulacji. */
    private Thread gameThread;
    /** Flaga oznaczająca, czy symulacja jest wstrzymana. */
    private volatile boolean paused = false;
    /** Czas trwania jednego kroku symulacji w milisekundach. */
    private int timeOfOneStep = 100;

    /**
     * Konstruktor klasy SimCore.
     * Inicjalizuje listę jednostek (grup).
     */
    public SimCore() {
        this.groups = new ArrayList<>();
    }

    /**
     * Główna pętla symulacji.
     * Zarządza przepływem kroków symulacji.
     */
    private void runSimulation() {
        while (running) {
            if (paused) {
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }

            long startTime = System.currentTimeMillis();
            currentStep++;

            groupModifications(currentStep);
            runStep(currentStep);
            visibleStep(currentStep);
            clearanceStep(currentStep);

            long elapsedTime = System.currentTimeMillis() - startTime;
            long sleepTime = timeOfOneStep - elapsedTime;

            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            } else {
                System.out.println("Przekroczono " + timeOfOneStep + " ms w kroku: " + currentStep + " Czas wykonywania: " + elapsedTime + "ms");
            }
        }
    }

    private void groupModifications(int currentStep) {

    }

    /**
     * Wykonuje zadania i procesy dla bieżącego kroku symulacji.
     * @param currentStep bieżący krok symulacji
     */
    private void runStep(int currentStep){
        for (SimExecutionScheduler simObject : simObjects) {
            simObject.runStep(currentStep);
        }
        for (SimGroup group : groups) {
            group.runStep(currentStep);
        }
    }

    /**
     * Aktualizuje listę widocznych jednostek (grup) dla każdej jednostki (grupy) w symulacji.
     * @param currentStep bieżący krok symulacji
     */
    private void visibleStep(int currentStep){
        for (SimGroup group : groups) {
            List<SimGroup> visibleGroups = getVisibleGroups(group);
            group.updateVisibleGroups(visibleGroups);
        }
    }

    /**
     * Usuwa zniszczone jednostki (grupy) z symulacji.
     * @param currentStep bieżący krok symulacji
     */
    private void clearanceStep(int currentStep){
        Iterator<SimGroup> iterator = groups.iterator();
        while (iterator.hasNext()) {
            SimGroup group = iterator.next();
            if (group.isDestroyed()){
                destroyedGroups.add(group);
                iterator.remove();
            }
        }
    }

    /**
     * Zwraca listę jednostek (grup) widocznych dla danej jednostki na podstawie zasięgu widzenia.
     * @param group jednostka (grupa), dla której sprawdzane są widoczne grupy
     * @return lista widocznych jednostek (grup)
     */
    private List<SimGroup> getVisibleGroups(SimGroup group) {
        List<SimGroup> visibleGroups = new ArrayList<>();
        for (SimGroup otherGroup : this.groups) {
            if (!otherGroup.equals(group) && (otherGroup.getForceType() != group.getForceType()) && group.getViewRange()  > group.position.distanceTo(otherGroup.position)) {
                visibleGroups.add(otherGroup);
            }
        }
        return visibleGroups;
    }

    /// Methods for steering the simulation workflow.

    /**
     * Zatrzymuje działanie symulacji.
     * Ustawia flagę `running` na `false` i przerywa wątek symulacji.
     */
    public void stopSimulation() {
        running = false;
        if (gameThread != null) {
            gameThread.interrupt();
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Wstrzymuje działanie symulacji, ustawiając flagę `paused` na `true`.
     */
    public void pauseSimulation() {
        paused = true;
    }

    /**
     * Wznawia działanie symulacji, ustawiając flagę `paused` na `false`.
     * Wybudza wątek, jeśli był w stanie oczekiwania.
     */
    public void resumeSimulation() {
        paused = false;
        synchronized (this) {
            notify();
        }
    }

    /**
     * Rozpoczyna symulację, uruchamiając główny wątek symulacji, jeśli nie jest on aktywny.
     */
    public void startSimulation() {
        if (gameThread == null || !gameThread.isAlive()) {
            running = true;
            gameThread = new Thread(this::runSimulation);
            gameThread.start();
        }
    }

    /// Methods for the simulation parameters/works sets/gets.

    /**
     * Ustawia czas trwania jednego kroku symulacji.
     * @param timeOfOneStep czas trwania kroku w milisekundach
     */
    public void setTimeOfOneStep(int timeOfOneStep) {
        this.timeOfOneStep = timeOfOneStep;
    }

    /**
     * Dodaje nowy obiekt symulacyjny do listy `simObjects`.
     * @param simObject obiekt implementujący SimExecutionScheduler
     */
    public void addSimObjects(SimExecutionScheduler simObject) {
        this.simObjects.add(simObject);
    }

    /**
     * Zwraca bieżący krok symulacji.
     * @return numer bieżącego kroku symulacji
     */
    public int getSimulationTime() {
        return currentStep;
    }

    /// Methods for steering the groups.

    /**
     * Dodaje nową jednostkę (grupę) do symulacji.
     * Ustawia referencję do `SimCore` jako rodzica i inicjalizuje grupę.
     * @param group jednostka (grupa) do dodania
     */
    public void addGroup(SimGroup group){
        group.parent = this;
        group.init();
        this.groups.add(group);
    }

    /**
     * Zwraca listę jednostek (grup) obecnych w symulacji.
     * @return lista jednostek (grup)
     */
    public List<SimGroup> getGroups() {
        return groups;
    }

    /**
     * Edytuje istniejącą jednostkę (grupę) w symulacji.
     * Dodaje ją do bufora modyfikacji i sprawdza, czy grupa istnieje w aktualnej liście.
     * @param group jednostka (grupa) do edycji
     * @return true, jeśli grupa została znaleziona i można ją edytować, false w przeciwnym razie
     */
    public boolean editGroup(SimGroup group) {
        modifingBufforGroups.add(group);
        for (int i = 0; i < groups.size(); i++) {
            if (groups.get(i).id == group.id) {
//                groups.set(i, group);
                return true;
            }
        }
        return false;
    }

    /// Methods for steering the map.

    /**
     * Zwraca mapę symulacji.
     * @return obiekt mapy symulacji
     */
    public SimMap getMap() {
        return map;
    }

    /**
     * Ustawia mapę symulacji.
     * @param map obiekt mapy
     */
    public void setMap(SimMap map) {
        this.map = map;
    }

}
