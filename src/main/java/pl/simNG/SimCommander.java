package pl.simNG;

import pl.simNG.commands.SimCommand;
import pl.simNG.scheduler.SimExecutionScheduler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Klasa zarządzająca rozkazami w symulacji.
 * Odpowiada za dystrybucję rozkazów do jednostek (grup) oraz monitorowanie ich realizacji.
 */
public class SimCommander extends SimExecutionScheduler {
    /** Kolejka rozkazów do wykonania. */
    protected LinkedList<SimCommand> commandQueue = new LinkedList<>();
    /** Historia rozkazów. */
    protected LinkedList<SimCommand> commandQueueHistory = new LinkedList<>();
    /** Lista jednostek (grup) zarządzanych przez SimCommandera. */
    protected List<SimGroup> groups = new ArrayList<>();
    /** Bieżący rozkaz przypisany do grup jednostek. */
    public SimCommand currentCommand = null;

    /**
     * Konstruktor klasy SimCommander.
     * Dodaje zadanie głównej pętli kontrolnej odpowiedzialnej za zarządzanie rozkazami.
     */
    public SimCommander(){
        addTask(this::mainFx,1);
    }

    /**
     * Dodaje grupę jednostek do zarządzania przez SimCommandera.
     * @param group grupa jednostek
     */
    public void addGroups(SimGroup group) {
        group.commander = this;
        groups.add(group);
    }
    /**
     * Zwraca bieżący rozkaz.
     * @return aktualny rozkaz
     */
    public SimCommand getCurrentCommand() {
        return currentCommand;
    }
    /**
     * Dodaje rozkaz do kolejki.
     * @param command nowy rozkaz
     */
    public void addCommand(SimCommand command){
        commandQueue.add(command);
    }

    public void clearCommands(){
        this.commandQueue.clear();
    }

    public void stopCommands(){
        this.clearCommands();
        for (SimGroup g : groups) {
            g.route.clear();
            g.endCurrentCommand();
        }
    }

    public LinkedList<SimCommand> getCommandQueue() {
        return commandQueue;
    }

    public LinkedList<SimCommand> getCommandQueueHistory() {
        return commandQueueHistory;
    }

    /**
     * Główna pętla kontrolna odpowiedzialna za zarządzanie rozkazami.
     * Sprawdza, czy wszystkie jednostki wykonały bieżący rozkaz i przypisuje nowy rozkaz.
     */
    private void mainFx(){
        if (allDone()){
            assignNewCommand();
            addTask(this::mainFx,1);
        }else {
            addTask(this::mainFx,1);
        }
    }

    /**
     * Sprawdza, czy wszystkie grupy jednostek zakończyły wykonywanie bieżącego rozkazu.
     * @return true, jeśli wszystkie jednostki zakończyły rozkaz, false w przeciwnym razie
     */
    private boolean allDone(){
        for (SimGroup g : groups) {
            if (g.getCurrentCommand() != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Przypisuje nowy rozkaz z kolejki jako bieżący i rozsyła go do wszystkich grup jednostek.
     */
    private void assignNewCommand(){
        commandQueueHistory.add(currentCommand);
        currentCommand = commandQueue.isEmpty() ? null : commandQueue.poll();
        for (SimGroup g : groups) {
            g.assignCommand(currentCommand);
        }
    }
}
