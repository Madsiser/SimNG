package pl.simNG.commands;

import pl.simNG.SimGroup;
import pl.simNG.scheduler.SimExecutionScheduler;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasa zarządzająca rozkazami w symulacji.
 * Odpowiada za dystrybucję rozkazów do jednostek (grup) oraz monitorowanie ich realizacji.
 */
public class SimCommander extends SimExecutionScheduler {
    /** Kolejka rozkazów do wykonania. */
    protected List<Command> commandQueue = new ArrayList<>();
    /** Lista jednostek (grup) zarządzanych przez SimCommandera. */
    protected List<SimGroup> groups = new ArrayList<>();
    /** Bieżący rozkaz przypisany do grup jednostek. */
    public Command currentCommand = null;

    /**
     * Konstruktor klasy SimCommander.
     * Dodaje zadanie głównej pętli kontrolnej odpowiedzialnej za zarządzanie rozkazami.
     */
    SimCommander(){
        addTask(this::mainFx,1);
    }

    /**
     * Główna pętla kontrolna odpowiedzialna za zarządzanie rozkazami.
     * Sprawdza, czy wszystkie jednostki wykonały bieżący rozkaz i przypisuje nowy rozkaz.
     */
    private void mainFx(){
        if (allDone()){
            assignNewCommand();
            addTask(this::mainFx,15);
        }else {
            addTask(this::mainFx,1);
        }

    }

    /**
     * Dodaje grupę jednostek do zarządzania przez SimCommandera.
     * @param group grupa jednostek
     */
    public void addGroups(SimGroup group) {
        groups.add(group);
    }

    /**
     * Zwraca bieżący rozkaz.
     * @return aktualny rozkaz
     */
    public Command getCurrentCommand() {
        return currentCommand;
    }

    /**
     * Przypisuje nowy rozkaz do kolejki i automatycznie ustawia go jako bieżący, jeśli brak aktywnego rozkazu.
     * @param command nowy rozkaz
     */
    public void assignCommand(Command command) {
        commandQueue.add(command);
        if (currentCommand == null) {
            currentCommand = commandQueue.remove(0);
        }
        assignNewCommand();
    }

    /**
     * Dodaje rozkaz do kolejki.
     * @param command nowy rozkaz
     */
    public void addCommand(Command command){
        commandQueue.add(command);
    }

    /**
     * Sprawdza, czy wszystkie grupy jednostek zakończyły wykonywanie bieżącego rozkazu.
     * @return true, jeśli wszystkie jednostki zakończyły rozkaz, false w przeciwnym razie
     */
    private boolean allDone(){
        boolean allDone = true;
        for (SimGroup g : groups) {
            if (g.currentCommand == null) {
                allDone = false;
                break;
            }
        }
        return allDone;
    }

    /**
     * Przypisuje nowy rozkaz z kolejki jako bieżący i rozsyła go do wszystkich grup jednostek.
     */
    private void assignNewCommand(){
        currentCommand = commandQueue.isEmpty() ? null : commandQueue.remove(0);
        for (SimGroup g : groups) {
            g.assignCommand(currentCommand);
        }
    }
}
