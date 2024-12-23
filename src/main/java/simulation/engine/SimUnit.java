package simulation.engine;

public abstract class SimUnit {
    protected String name;
    protected Integer viewRange;
    protected Integer shotRange;
    protected Integer speed;
    protected Integer amount;

    private SimGroup parent = null;

    public SimUnit(String name ,Integer viewRange, Integer shotRange, Integer speed, Integer amount) {
        if (name ==null || viewRange == null || shotRange == null || speed == null || amount == null) {
            throw new IllegalArgumentException("Wszystkie pola muszą być wypełnione.");
        }
        this.name = name;
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


    //Getters
    public Integer getAmount() {
        return amount;
    }

    public Integer getSpeed() {
        return speed;
    }

    public Integer getShotRange() {
        return shotRange;
    }

    public Integer getViewRange() {
        return viewRange;
    }

    public String getName() {
        return name;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}

