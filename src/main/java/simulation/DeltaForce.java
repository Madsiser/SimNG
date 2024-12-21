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

        //addProcess("move", this::move, 1);
        addTask(this::move,1);
    }

    @Override
    public void move(){
        SimVector2i direction = route.poll();
        if (direction != null){
            this.position.add(direction);
            addTask(this::move,1);
        }
        System.out.println(this.position);
    }




}
