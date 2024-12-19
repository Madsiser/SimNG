package simulation;

import simulation.engine.SimUnit;

public class Tank extends SimUnit {

    public Tank() {
        addWork(this::move, 3);  // Co 3 kroki - move
        addWork(this::scan, 2);   // Co 2 kroki - scan
        addWork(this::shoot, 1);  // Co 1 krok - shoot
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
