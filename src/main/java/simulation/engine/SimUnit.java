package simulation.engine;

public abstract class SimUnit {
    public Integer viewRange;
    public Integer shotRange;
    public Integer speed;
    public Integer amount;

    private SimGroup parent = null;

    public SimUnit(Integer viewRange, Integer shotRange, Integer speed, Integer amount) {
        if (viewRange == null || shotRange == null || speed == null || amount == null) {
            throw new IllegalArgumentException("Wszystkie pola muszą być wypełnione.");
        }
        this.viewRange = viewRange;
        this.shotRange = shotRange;
        this.speed = speed;
        this.amount = amount;
    }

    public boolean isDestroyed(){
        return amount<=0;
    }

    public boolean inShotRange(SimPosition position){
        return shotRange >= this.getParent().getPosition().distanceTo(position);
    }

    public SimGroup getParent() {
        return parent;
    }

    public void setParent(SimGroup parent) {
        this.parent = parent;
    }
}

