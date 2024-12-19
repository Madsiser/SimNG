package simulation;

import simulation.engine.*;

import java.util.LinkedList;
import java.util.function.Supplier;

public class DeltaForce extends SimGroup {

    public DeltaForce(Position position) {
        super("Delta Force", position);


        SimUnit unit = new Tank(SimForceType.BLUFORCE,3,10,1,5);
        this.addUnit(unit);

        this.route.push(Vector2i.LEFT);
        this.route.push(Vector2i.LEFT);
        this.route.push(Vector2i.LEFT);
        this.route.push(Vector2i.LEFT);
        this.route.push(Vector2i.LEFT);
        this.route.push(Vector2i.LEFT);
        this.route.push(Vector2i.LEFT);

        addWork("move", this::move, 1);
    }



}
