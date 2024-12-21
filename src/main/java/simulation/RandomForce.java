package simulation;

import simulation.engine.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomForce extends SimGroup {

    Random random = new Random();

    public RandomForce(String name, SimPosition position, SimForceType forceType) {
        super(name, position, forceType);


        SimUnit unit = new Tank((random.nextInt()%3)+3,5,1,5);
        this.addUnit(unit);

        List<SimVector2i> directions = new ArrayList<>();
        directions.add(SimVector2i.UP);
        directions.add(SimVector2i.DOWN);
        directions.add(SimVector2i.LEFT);
        directions.add(SimVector2i.RIGHT);

        int length = 1000;
        for (int i = 0; i < length; i++) {
            SimVector2i randomDirection = directions.get(random.nextInt(directions.size()));
            this.route.add(randomDirection);
        }

//        addProcess("move", this::move, random.nextInt()%10+1);
        addTask(this::move,1);
    }

    @Override
    public void move(){
        SimVector2i direction = route.poll();
        if (direction != null){
            this.position.add(direction);
            addTask(this::move,(random.nextInt()%5)+5);
        }
//        System.out.println(this.position);
    }




}
