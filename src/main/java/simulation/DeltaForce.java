package simulation;

import simulation.engine.*;

public class DeltaForce extends SimGroup {

    public DeltaForce() {
        super("Delta Force", new SimPosition(1,1));


        SimUnit unit = new Tank(SimForceType.BLUFORCE,5,10,1,5);
        this.addUnit(unit);

        this.route.push(SimVector2i.LEFT);
        this.route.push(SimVector2i.LEFT);
        this.route.push(SimVector2i.LEFT);
        this.route.push(SimVector2i.LEFT);
        this.route.push(SimVector2i.LEFT);
        this.route.push(SimVector2i.LEFT);
        this.route.push(SimVector2i.LEFT);

        addProcess("move", this::move, 1);
    }



}
