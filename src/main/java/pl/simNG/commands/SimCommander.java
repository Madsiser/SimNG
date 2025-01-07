package pl.simNG.commands;

import pl.simNG.SimGroup;
import pl.simNG.scheduler.SimExecutionScheduler;

import java.util.ArrayList;
import java.util.List;

public class SimCommander extends SimExecutionScheduler {
    protected List<Command> commandQueue = new ArrayList<>();
    protected List<SimGroup> groups = new ArrayList<>();
    public Command currentCommand = null;

    SimCommander(){
        addTask(this::mainFx,1);
    }

    private void mainFx(){
        if (allDone()){
            assignNewCommand();
            addTask(this::mainFx,15);
        }else {
            addTask(this::mainFx,1);
        }

    }

    public void addGroups(SimGroup group) {
        groups.add(group);
    }

    public Command getCurrentCommand() {
        return currentCommand;
    }

    public void assignCommand(Command command) {
        commandQueue.add(command);
        if (currentCommand == null) {
            currentCommand = commandQueue.remove(0);
        }
        assignNewCommand();
    }

    public void addCommand(Command command){
        commandQueue.add(command);
    }

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

    private void assignNewCommand(){
        currentCommand = commandQueue.isEmpty() ? null : commandQueue.remove(0);
        for (SimGroup g : groups) {
            g.assignCommand(currentCommand);
        }
    }
}
