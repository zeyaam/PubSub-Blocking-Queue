package DataStructures.Graph;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import DataStructures.LinkedList.LinkedList;
import DataStructures.LinkedList.LinkedListNode;
import DataStructures.UnionFind.UnionFind;

/**
 * Utility class for working with graphs.
 *
 * @param <T> the type of elements in the graph
 */
public class GraphUtils<T> {
    public GraphUtils() {
    }


    /**
        * Picks a random element from the given set of integers.
        *
        * @param items the set of integers to pick from
        * @return a randomly selected integer from the set, or null if the set is empty
        */
    public static Integer pickRandomElementFromSet(Set<Integer> items) {
        Random rand = new Random();
        int randIndex = rand.nextInt(items.size());
        int i = 0;
        for (Integer item : items) {
            if (i == randIndex) {
                return item;
            }
            i++;
        }
        return null;
    }

    /**
     * Creates a graph using DFS.
     * 
     * @param graph       the graph represented as a HashMap of nodes and their adjacent nodes
     * @param currentNode the current node to start the DFS from
     * @param nums        a set of available nodes to visit
     * @param rand        a random number generator
     */
    public static void createGraph(HashMap<Integer, ArrayList<Integer>> graph, Integer currentNode, Set<Integer> nums, Random rand) {
        if (nums.isEmpty()) {
            return;
        }
        int numChildren = Math.min(nums.size(), 2);
        ArrayList<Integer> children = new ArrayList<>();
        for (int i = 0; i < numChildren; i++) {
            Integer child = pickRandomElementFromSet(nums);
            nums.remove(child);
            children.add(child);
        }
        graph.put(currentNode, children);
        for (Integer c : children) {
            createGraph(graph, c, nums, rand);
        }
    }

    /**
     * Generates a dependency graph with the specified number of nodes.
     *
     * @param numNodes the number of nodes in the graph
     * @return a HashMap representing the dependency graph, where each node is mapped to its dependencies
     */
    public static HashMap<Integer, ArrayList<Integer>> generateDependencyGraph(int numNodes) {
        HashMap<Integer, ArrayList<Integer>> graph = new HashMap<>();
        Random rand = new Random();
        Set<Integer> nodes = IntStream.rangeClosed(1, numNodes).boxed().collect(Collectors.toSet());
        Integer root = 1;
        for (int i = 1; i < numNodes + 1; i++) {
            graph.put(i, new ArrayList<>());
        }
        nodes.remove(root);
        createGraph(graph, root, nodes, rand);
        return graph;
    }

    public static HashMap<Integer, ArrayList<Integer>> generateRandomGraph(int numNodes, boolean hasCycle) {
        Random rand = new Random();
        HashMap<Integer, ArrayList<Integer>> graph = new HashMap<>();
        Set<Integer> nodes = IntStream.rangeClosed(1, numNodes).boxed().collect(Collectors.toSet());
        int maxBucketSize = Double.valueOf(Math.sqrt(numNodes)).intValue();
        for (int i = 1; i <= numNodes; i++) {
            graph.put(i, new ArrayList<>());
            if (!hasCycle) {
                nodes.remove(i);
            }
            for (int j = 0; j < Math.min(nodes.size(), rand.nextInt(1, maxBucketSize)); j++) {
                Integer randomNode = pickRandomElementFromSet(nodes);
                if (!hasCycle) {
                    nodes.remove(randomNode);
                }
                graph.get(i).add(randomNode);
            }
        }

        return graph;
    }

    /**
     * Converts a graph represented by a HashMap into an array of strings.
     * Each string in the array represents a vertex in the graph along with its adjacent vertices.
     *
     * @param graph the graph represented by a HashMap where the key is the vertex and the value is a list of adjacent vertices
     * @return an array of strings representing the graph
     */
    public static String[] convertGraphToString(HashMap<Integer, ArrayList<Integer>> graph) {
        String[] graphString = new String[graph.size()];
        for (Integer key : graph.keySet()) {
            StringJoiner stringJoiner = new StringJoiner(", ");
            stringJoiner.add(String.valueOf(key));
            for(Integer val : graph.get(key)) {
                stringJoiner.add(String.valueOf(val));
            }
            graphString[key-1] = stringJoiner.toString();
        }
        return graphString;
    }

    /**
     * Determines whether a given graph has a cycle.
     *
     * @param graph the graph represented as a HashMap of nodes and their adjacent nodes
     * @return true if the graph has a cycle, false otherwise
     */
    public boolean hasCycle(HashMap<T, ArrayList<T>> graph) {
        Set<T> visited = new HashSet<>();
        LinkedList<T> queue = new LinkedList<T>();
        for (T key : graph.keySet()) {
            if (!graph.get(key).isEmpty()) {
                queue.add(new LinkedListNode<>(key));
                break;
            }
        }
        if (queue.size() == 0) {
            return true;
        }
        while (queue.size() > 0) {
            T node = queue.removeLeft();
            if (node == null) {
                break;
            }
            if (visited.contains(node)) {
                return true;
            }
            visited.add(node);
            for (T value : graph.get(node)) {
                queue.add(new LinkedListNode<T>(value));
            }
        }
        return false;
    }

    /**
     * Checks if a graph is connected.
     * 
     * @param graph the graph represented as a HashMap of nodes and their adjacent nodes
     * @return true if the graph is connected, false otherwise
     */
    public boolean isConnected(HashMap<T, ArrayList<T>> graph) {
        HashMap<T, UnionFind<T>> connections = new HashMap<>();
        for (T key : graph.keySet()) {
            UnionFind<T> node;
            if (connections.containsKey(key)) {
                node = connections.get(key);
            } else {
                node = new UnionFind<T>(key);
                connections.put(key, node);
            }
            for (T value : graph.get(key)) {
                if (connections.containsKey(value)) {
                    node.unionNode(node, connections.get(value));
                } else {
                    UnionFind<T> newNode = new UnionFind<T>(value);
                    node.unionNode(node, newNode);
                    connections.put(value, newNode);
                }
            }
        }
        HashSet<T> components = new HashSet<>();
        for (T key : connections.keySet()) {
            components.add(connections.get(key).getParent().value);
        }
        return components.size() == 1;
    }

}
