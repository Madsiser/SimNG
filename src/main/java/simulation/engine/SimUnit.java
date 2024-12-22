package simulation.engine;

public abstract class SimUnit {
    public Integer viewRange;
    public Integer shotRange;
    public Integer speed;
    public Integer amount;

    public SimUnit(Integer viewRange, Integer shotRange, Integer speed, Integer amount) {
        if (viewRange == null || shotRange == null || speed == null || amount == null) {
            throw new IllegalArgumentException("Wszystkie pola muszą być wypełnione.");
        }
        this.viewRange = viewRange;
        this.shotRange = shotRange;
        this.speed = speed;
        this.amount = amount;
    }
}

