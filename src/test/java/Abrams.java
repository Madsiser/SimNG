import pl.simNG.SimUnit;

public class Abrams extends SimUnit{

    public Abrams(int amount) {
        super("Abrams", 5, 2, 4, amount);
    }

    private void shoot() {
        System.out.println("Strzelanie!");
    }
}
