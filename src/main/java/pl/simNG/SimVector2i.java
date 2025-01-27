package pl.simNG;

import java.util.Arrays;
import java.util.List;

/** Klasa reprezentująca wektor w przestrzeni dwuwymiarowej o współrzędnych całkowitych. */
public class SimVector2i {
    public int x;
    public int y;

    /** Wektor skierowany w górę. */
    public static final SimVector2i UP = new SimVector2i(0, 1);
    /** Wektor skierowany w dół. */
    public static final SimVector2i DOWN = new SimVector2i(0, -1);
    /** Wektor skierowany w lewo. */
    public static final SimVector2i LEFT = new SimVector2i(-1, 0);
    /** Wektor skierowany w prawo. */
    public static final SimVector2i RIGHT = new SimVector2i(1, 0);

    public SimVector2i(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * Konstruktor klasy SimVector2i z wartościami double, które są zaokrąglane do najbliższej liczby całkowitej.
     * @param x współrzędna X
     * @param y współrzędna Y
     */
    public SimVector2i(double x, double y) {
        this.x = (int) Math.round(x);
        this.y = (int) Math.round(y);
    }

    /**
     * Oblicza odległość euklidesową do innego wektora.
     * @param other wektor, do którego liczona jest odległość
     * @return odległość między bieżącym a podanym wektorem
     */
    public double distanceTo(SimVector2i other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Dodaje współrzędne innego wektora do bieżącego wektora.
     * @param vector wektor do dodania
     */
    public void add(SimVector2i vector) {
        this.x += vector.x;
        this.y += vector.y;
    }

    /**
     * Dodaje wartości przesunięcia do bieżącego wektora.
     * @param x wartość przesunięcia w osi X
     * @param y wartość przesunięcia w osi Y
     */
    public void add(Integer x, Integer y) {
        this.x += x;
        this.y += y;
    }

    /**
     * Zwraca wektor w formacie tekstowym.
     * @return ciąg znaków reprezentujący współrzędne wektora
     */
    @Override
    public String toString() {
        return "Vector2i{" + "x=" + x + ", y=" + y + '}';
    }

    /**
     * Zwraca listę podstawowych wektorów kierunkowych (góra, dół, lewo, prawo).
     * @return lista wektorów kierunkowych
     */
    public static List<SimVector2i> values() {
        return Arrays.asList(UP, DOWN, LEFT, RIGHT);
    }

    /**
     * Tworzy wektor kierunkowy na podstawie dwóch pozycji:
     * @param start pozycja początkowa
     * @param end pozycja końcowa
     * @return wektor kierunkowy od pozycji początkowej do końcowej
     */
    public static SimVector2i fromPositions(SimPosition start, SimPosition end) {
        return new SimVector2i(end.getX() - start.getX(), end.getY() - start.getY());
    }
}
