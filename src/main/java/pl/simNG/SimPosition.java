package pl.simNG;

/**
 * Klasa reprezentująca pozycję w przestrzeni dwuwymiarowej.
 * Używana w symulacji do określania lokalizacji jednostek lub punktów na mapie.
 */
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

    public void setX(int x) {
        this.x = (double) x;
    }

    public void setY(int y) {
        this.y = (double) y;
    }

    /**
     * Zwraca tablicę współrzędnych w postaci zaokrąglonej do najbliższej liczby całkowitej.
     * @return tablica [X, Y] jako wartości typu int
     */
    public int[] toIntArray() {
        return new int[]{(int) Math.round(this.x), (int) Math.round(this.y)};
    }

    /**
     * Oblicza odległość euklidesową do innej pozycji.
     * @param other obiekt {@link SimPosition}, do którego liczona jest odległość
     * @return odległość między obecną a podaną pozycją
     */
    public double distanceTo(SimPosition other) {
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }

    /**
     * Dodaje współrzędne innego obiektu {@link SimPosition} do obecnej pozycji.
     * @param position obiekt pozycji do dodania
     */
    public void add(SimPosition position) {
        this.x += position.getX();
        this.y += position.getY();
    }

    /**
     * Dodaje wartości wektora {@link SimVector2i} do obecnej pozycji.
     * @param vector2i obiekt wektora z wartościami do dodania
     */
    public void add(SimVector2i vector2i) {
        this.x += vector2i.x;
        this.y += vector2i.y;
    }

    /**
     * Dodaje wartości przesunięcia (typu double) do obecnej pozycji.
     * @param dx wartość przesunięcia w osi X
     * @param dy wartość przesunięcia w osi Y
     */
    public void add(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    /**
     * Zwraca reprezentację obiektu w postaci ciągu znaków.
     * @return współrzędne pozycji w formacie "(X, Y)"
     */
    @Override
    public String toString() {
        return String.format("(%.2f, %.2f)", x, y);
    }
}
