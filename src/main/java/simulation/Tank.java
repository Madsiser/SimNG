package simulation;

import simulation.engine.SimForceType;
import simulation.engine.SimGroup;
import simulation.engine.SimUnit;

public class Tank extends SimUnit{

    public Tank(SimForceType type, Integer viewRange, Integer shotRange, Integer speed, Integer amount) {
        super(type, viewRange, shotRange, speed, amount);
    }

    private void move() {
        System.out.println("Zmiana pozycji...");
    }

    private void scan() {
        System.out.println("Skanowanie otoczenia...");
    }

    private void shoot() {
        System.out.println("Strzelanie!");
    }
}
