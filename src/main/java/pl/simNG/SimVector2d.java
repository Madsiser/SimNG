package pl.simNG;

public class SimVector2d {
    public double x;
    public double y;

    public SimVector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getDx() {
        return x;
    }

    public double getDy() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public SimVector2i toIntVector() {
        return new SimVector2i(x, y);
    }

    public SimVector2d scale(double factor) {
        return new SimVector2d(this.x * factor, this.y * factor);
    }

    public SimVector2d add(SimVector2d other) {
        return new SimVector2d(this.x + other.x, this.y + other.y);
    }

    @Override
    public String toString() {
        return "SimVector2d{" + "x=" + x + ", y=" + y + '}';
    }
}

