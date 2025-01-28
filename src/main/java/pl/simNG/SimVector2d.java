package pl.simNG;

/** Klasa reprezentująca wektor w przestrzeni dwuwymiarowej o współrzędnych typu double. */
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

    /**
     * Konwertuje wektor na obiekt {@link SimVector2i} o współrzędnych zaokrąglonych do najbliższej liczby całkowitej.
     * @return wektor z wartościami całkowitymi
     */
    public SimVector2i toIntVector() {
        return new SimVector2i(x, y);
    }

    /**
     * Skaluje wektor przez określony współczynnik.
     * Wynikowe współrzędne są zaokrąglane do dwóch miejsc po przecinku.
     * @param factor współczynnik skalowania
     * @return nowy obiekt wektora po skalowaniu
     */
    public SimVector2d scale(double factor) {
        return new SimVector2d(Math.round(this.x * factor * 100) / 100.0,
                Math.round(this.y * factor * 100) / 100.0);
    }

    /**
     * Dodaje współrzędne innego wektora do bieżącego wektora.
     * @param other wektor do dodania
     * @return nowy obiekt wektora będący wynikiem dodawania
     */
    public SimVector2d add(SimVector2d other) {
        return new SimVector2d(this.x + other.x, this.y + other.y);
    }

    /**
     * Zwraca wektor w formacie tekstowym.
     * @return ciąg znaków reprezentujący współrzędne wektora
     */
    @Override
    public String toString() {
        return "SimVector2d{" + "x=" + x + ", y=" + y + '}';
    }
}

