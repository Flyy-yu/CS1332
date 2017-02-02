/**
 * Utility class implementing the disjoint-set data structure.
 *
 * Do not modify this file.
 * @author CS 1332 TAs
 * @version 1.0
 */
public class DisjointSet {

    private DisjointSet parent;
    private int rank;

    /**
     * Creates a new one-element disjoint set.
     */
    public DisjointSet() {
        parent = this;
    }

    /**
     * Finds and returns the root element of this disjoint set.
     * @return the root element of the given set
     */
    public DisjointSet find() {
        if (parent != this) {
            parent = parent.find();
        }
        return parent;
    }

    /**
     * Merges this disjoint set with another disjoint set.
     * @param y the second set
     */
    public void union(DisjointSet y) {
        DisjointSet xRoot = find();
        DisjointSet yRoot = y.find();

        if (xRoot == yRoot) {
            return;
        }

        if (xRoot.rank < yRoot.rank) {
            xRoot.parent = yRoot;
        } else if (xRoot.rank > yRoot.rank) {
            yRoot.parent = xRoot;
        } else {
            yRoot.parent = xRoot;
            xRoot.rank++;
        }
    }
}
