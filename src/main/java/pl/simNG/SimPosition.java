package pl.simNG;

import java.util.Objects;

public class SimPosition {
    private double x;
    private double y;

    public SimPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public SimPosition(SimPosition position) {
        this.x = position.x;
        this.y = position.y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(int x) {
        this.x = (double) x;
    }

    public void setY(int y) {
        this.y = (double) y;
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

//    public void add(SimPosition position) {
//        this.x += position.getX();
//        this.y += position.getY();
//    }
//    public void add(SimVector2i vector2i) {
//        this.x += vector2i.x;
//        this.y += vector2i.y;
//    }
//    public void add(double dx, double dy) {
//        this.x += dx;
//        this.y += dy;
//    }

    public SimPosition add(SimPosition position) {
        return new SimPosition(this.x + position.getX(), this.y + position.getY());
    }
    public SimPosition add(SimVector2i vector2i) {
        return new SimPosition(this.x + vector2i.x, this.y + vector2i.y);
    }
    public SimPosition add(double dx, double dy) {
        return new SimPosition(this.x + dx, this.y + dy);
    }
    public SimPosition subtract(SimPosition position) {
        return new SimPosition(this.x - position.getX(), this.y - position.getY());
    }
    public SimPosition subtract(SimVector2i vector2i) {
        return new SimPosition(this.x - vector2i.x, this.y - vector2i.y);
    }
    public SimPosition subtract(double dx, double dy) {
        return new SimPosition(this.x - dx, this.y - dy);
    }
    public double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }
    public SimVector2i normalize() {
        double len = length();
        if (len == 0) {
            return new SimVector2i(0, 0);
        }
        return new SimVector2i(x / len, y / len);
    }


    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        SimPosition that = (SimPosition) object;
        return Double.compare(x, that.x) == 0 && Double.compare(y, that.y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return String.format("(%.2f, %.2f)", x, y);
    }

}
