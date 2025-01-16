package pl.simNG;

public class SimPosition {
    private double x;
    private double y;

    public SimPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double[] toArray(){
        return new double[]{getX(), getY()};
    }

    public int[] toIntArray() {
        return new int[]{(int) Math.round(this.x), (int) Math.round(this.y)};
    }

    public double distanceTo(SimPosition other) {
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }

    public void add(SimPosition position) {
        this.x += position.getX();
        this.y += position.getY();
    }
    public void add(SimVector2i vector2i) {
        this.x += vector2i.x;
        this.y += vector2i.y;
    }
    public void add(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    @Override
    public String toString() {
        return String.format("(%.2f, %.2f)", x, y);
    }
}
