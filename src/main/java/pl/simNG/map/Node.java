package pl.simNG.map;

/**
 * Klasa reprezentująca węzeł w algorytmie A*.
 * Każdy węzeł opisuje pojedynczy punkt na mapie, zawierający informacje o kosztach i położeniu.
 */
public class Node implements Comparable<Node> {
    /** Wiersz (row) pozycji węzła na mapie. */
    public int row;
    /** Kolumna (col) pozycji węzła na mapie. */
    public int col;
    double gCost, hCost;
    /** Rodzic (poprzedni węzeł) węzła na ścieżce. */
    Node parent;

    /**
     * Konstruktor klasy Node.
     * @param row wiersz węzła
     * @param col kolumna węzła
     * @param gCost
     * @param hCost
     * @param parent rodzic (poprzedni węzeł na ścieżce)
     */
    public Node(int row, int col, double gCost, double hCost, Node parent) {
        this.row = row;
        this.col = col;
        this.gCost = gCost;
        this.hCost = hCost;
        this.parent = parent;
    }

    /**
     * Zwraca całkowity koszt węzła (fCost), będący sumą gCost i hCost.
     * @return całkowity koszt węzła
     */
    public double getFCost() {
        return gCost + hCost;
    }

    /**
     * Porównuje bieżący węzeł z innym na podstawie całkowitego kosztu (fCost).
     * @param other węzeł do porównania
     * @return wartość ujemna, zero lub dodatnia w zależności od wyniku porównania
     */
    @Override
    public int compareTo(Node other) {
        return Double.compare(this.getFCost(), other.getFCost());
    }

    /**
     * Zwraca reprezentację węzła w formacie tekstowym.
     * @return ciąg znaków w formacie "row:col(row:col)"
     */
    @Override
    public String toString(){
        return "row:col(" + this.row + ":" + this.col + ")";
    }
}