package pl.simNG;

import pl.simNG.commands.SimCommand;
import pl.simNG.scheduler.SimExecutionScheduler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SimCommander extends SimExecutionScheduler {
    protected LinkedList<SimCommand> commandQueue = new LinkedList<>();
    protected List<SimGroup> groups = new ArrayList<>();
    public SimCommand currentCommand = null;

    public SimCommander(){
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

    public SimCommand getCurrentCommand() {
        return currentCommand;
    }

    public void assignCommand(SimCommand command) {
        for (SimGroup g: groups){
            g.assignCommand(currentCommand);
        }
    }

    public void addCommand(SimCommand command){
        commandQueue.add(command);
    }

    private boolean allDone(){
        for (SimGroup g : groups) {
            if (g.getCurrentCommand() != null) {
                return false;
            }
        }
        return true;
    }

    private void assignNewCommand(){
        currentCommand = commandQueue.isEmpty() ? null : commandQueue.poll();
        for (SimGroup g : groups) {
            g.assignCommand(currentCommand);
        }
    }
}
