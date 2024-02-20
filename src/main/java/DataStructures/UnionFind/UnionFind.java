package DataStructures.UnionFind;

/**
 * The UnionFind class represents a data structure that maintains a collection of disjoint sets.
 * Each set is represented by a representative element, and the class provides operations to
 * perform union and find operations on the sets.
 *
 * @param <T> the type of elements stored in the UnionFind data structure.
 */
public class UnionFind<T> {
    public T value = null;
    public UnionFind<T> parent = this;
    public int size = 1;

    /**
     * Constructs a new UnionFind object with the specified value as the representative element.
     *
     * @param value the value of the representative element.
     */
    public UnionFind(T value) {
        this.value = value;
    }

    /**
     * Returns the parent of this UnionFind object.
     *
     * @return the parent of this UnionFind object.
     */
    public UnionFind<T> getParent() {
        UnionFind<T> p = parent;
        while(p != p.parent) {
            p = p.parent;
        }
        return p;
    }

    /**
     * Unions two nodes by merging their respective sets.
     * Returns the representative element of the resulting set.
     *
     * @param nodeA the first node to be unioned.
     * @param nodeB the second node to be unioned.
     * @return the representative element of the resulting set.
     */
    public UnionFind<T> unionNode(UnionFind<T> nodeA, UnionFind<T> nodeB) {
        UnionFind<T> parentA = nodeA.getParent();
        UnionFind<T> parentB = nodeB.getParent();
        if (parentA != parentB) {
            parentB.parent = parentA;
            parentA.size += parentB.size;
        }
        return parentA;
    }
}
