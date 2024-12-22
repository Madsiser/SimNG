package simulation.engine;

public class SimNode implements Comparable<SimNode> {
    public SimPosition position; // Pozycja węzła
    public int fScore; // Całkowity koszt (g + h)

    public SimNode(SimPosition position, int fScore) {
        this.position = position;
        this.fScore = fScore;
    }

    @Override
    public int compareTo(SimNode other) {
        return Integer.compare(this.fScore, other.fScore);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof SimNode)) return false;
        SimNode other = (SimNode) obj;
        return this.position.equals(other.position);
    }

    @Override
    public int hashCode() {
        return position.hashCode();
    }
}
