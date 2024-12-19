package simulation.engine;

public class Vector2i {
    public Integer x;
    public Integer y;

    public static final Vector2i UP = new Vector2i(0, 1);
    public static final Vector2i DOWN = new Vector2i(0, -1);
    public static final Vector2i LEFT = new Vector2i(-1, 0);
    public static final Vector2i RIGHT = new Vector2i(1, 0);

    public Vector2i(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public double distanceTo(Vector2i other) {
        double dx = this.x - other.x;
        double dy = this.y - other.x;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public void add(Vector2i vector) {
        this.x += vector.x;
        this.y += vector.y;
    }
    public void add(Integer x, Integer y) {
        this.x += x;
        this.y += y;
    }

    @Override
    public String toString() {
        return "Vector2i{" + "x=" + x + ", y=" + y + '}';
    }
}
