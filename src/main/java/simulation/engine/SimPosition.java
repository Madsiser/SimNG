package simulation.engine;

public class SimPosition {
    private Integer x;
    private Integer y;

    public SimPosition(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public int[] toArray(){
        return new int[]{getX(), getY()};
    }

    public double distanceTo(SimPosition other) {
        double dx = this.x - other.getX();
        double dy = this.y - other.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    public void add(SimPosition position) {
        this.x += position.getX();
        this.y += position.getY();
    }
    public void add(SimVector2i vector2i) {
        this.x += vector2i.x;
        this.y += vector2i.y;
    }
    public void add(Integer x, Integer y) {
        this.x += x;
        this.y += y;
    }

    @Override
    public String toString() {
        return "Position{" + "x=" + x + ", y=" + y + '}';
    }
}
