package simulation.engine;

public class SimUnit {
    public SimForceType type;
    public Integer viewRange;
    public Integer shotRange;
    public Integer speed;
    public Integer amount;

    // Konstruktor wymuszający przekazanie wartości
    public SimUnit(SimForceType type, Integer viewRange, Integer shotRange, Integer speed, Integer amount) {
        if (type == null || viewRange == null || shotRange == null || speed == null || amount == null) {
            throw new IllegalArgumentException("Wszystkie pola muszą być wypełnione.");
        }
        this.type = type;
        this.viewRange = viewRange;
        this.shotRange = shotRange;
        this.speed = speed;
        this.amount = amount;
    }
}

