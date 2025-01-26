package pl.simNG.map;

public class Node implements Comparable<Node> {
    public int row, col;
    double gCost, hCost;
    Node parent;

    public Node(int row, int col, double gCost, double hCost, Node parent) {
        this.row = row;
        this.col = col;
        this.gCost = gCost;
        this.hCost = hCost;
        this.parent = parent;
    }

    public double getFCost() {
        return gCost + hCost;
    }

    @Override
    public int compareTo(Node other) {
        return Double.compare(this.getFCost(), other.getFCost());
    }

    @Override
    public String toString(){
        return "row:col(" + this.row + ":" + this.col + ")";
    }
}