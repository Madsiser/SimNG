import simulation.engine.SimUnit;

public class Tank extends SimUnit{

    public Tank(Integer viewRange, Integer shotRange, Integer speed, Integer amount) {
        super(viewRange, shotRange, speed, amount);
    }

    private void shoot() {
        System.out.println("Strzelanie!");
    }
}
