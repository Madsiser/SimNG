package simulation;

import simulation.engine.*;

public class DeltaForce extends SimGroup {

    public DeltaForce(SimPosition position) {
        super("Delta Force", position);


        SimUnit unit = new Tank(SimForceType.BLUFORCE,3,10,1,5);
        this.addUnit(unit);

        this.route.push(SimVector2i.LEFT);
        this.route.push(SimVector2i.LEFT);
        this.route.push(SimVector2i.LEFT);
        this.route.push(SimVector2i.LEFT);
        this.route.push(SimVector2i.LEFT);
        this.route.push(SimVector2i.LEFT);
        this.route.push(SimVector2i.LEFT);

        addWork("move", this::move, 1);
    }



}
