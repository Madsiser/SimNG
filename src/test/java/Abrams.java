import simulation.engine.SimUnit;

public class Abrams extends SimUnit{

    public Abrams(Integer viewRange, Integer shotRange, Integer speed, Integer amount) {
        super("Abrams", viewRange, shotRange, speed, amount);
    }

    private void shoot() {
        System.out.println("Strzelanie!");
    }
}
