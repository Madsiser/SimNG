package simulation;

import simulation.engine.Position;
import simulation.engine.SimGroup;

public class DeltaForce extends SimGroup {
    public DeltaForce(Position position) {
        super("Delta Force", position);
        addWork(this::move,1);
    }

    private void move(){
        System.out.println("move...");
    }



}
