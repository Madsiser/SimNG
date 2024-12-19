package simulation;

import simulation.engine.Position;
import simulation.engine.SimGroup;
import simulation.engine.Vector2i;

import java.util.LinkedList;
import java.util.function.Supplier;

public class DeltaForce extends SimGroup {

    public DeltaForce(Position position) {
        super("Delta Force", position);
        this.route.push(Vector2i.LEFT);
        this.route.push(Vector2i.LEFT);
        this.route.push(Vector2i.LEFT);
        this.route.push(Vector2i.LEFT);
        this.route.push(Vector2i.LEFT);
        this.route.push(Vector2i.LEFT);
        this.route.push(Vector2i.LEFT);
    }



}
