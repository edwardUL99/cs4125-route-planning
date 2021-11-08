package ie.ul.routeplanning.routes.algorithms;

import ie.ul.routeplanning.routes.Route;
import ie.ul.routeplanning.routes.Waypoint;
import ie.ul.routeplanning.routes.graph.Edge;
import ie.ul.routeplanning.routes.graph.Graph;
import ie.ul.routeplanning.routes.graph.weights.WeightFunction;
import ie.ul.routeplanning.routes.graph.weights.WeightFunctionBuilder;
import ie.ul.routeplanning.transport.TransportMethod;

import java.util.*;

/**
 * An abstract class that is the base of all path finding algorithms.
 *
 * TODO provide some default operations, can be an example of template method too
 */
public abstract class PathFindingAlgorithm implements Algorithm<Route> {
    /**
     * The start waypoint for the path finding
     */
    protected final Waypoint start;
    /**
     * The end waypoint for the path finding
     */
    protected final Waypoint end;
    /**
     * The weight function to use for calculating weights
     */
    protected final WeightFunction weightFunction;

    /**
     * Construct an algorithm for path finding with the provided parameters
     * @param start the start waypoint for the path finding
     * @param end the end waypoint for the path finding
     * @param weightFunction the weight function used to calculate weights
     */
    public PathFindingAlgorithm(Waypoint start, Waypoint end, WeightFunction weightFunction) {
        this.start = start;
        this.end = end;
        this.weightFunction = weightFunction;
    }

    /**
     * Construct an algorithm for path finding using a default WeightFunction
     * @param start the start waypoint for the path finding
     * @param end the end waypoint for the path finding
     */
    public PathFindingAlgorithm(Waypoint start, Waypoint end) {
        this(start, end, WeightFunctionBuilder.DEFAULT);
    }

    /**
     * A delegate function to the algorithm method
     * @param graph the graph to perform the algorithm on
     * @return the list of routes
     */
    public List<Route> generateRoutes(Graph graph) {
        return perform(graph).collect();
    }

    /**
     * Get the start waypoint used for generating the path
     * @return the start waypoint
     */
    public Waypoint getStart() {
        return start;
    }

    /**
     * Get the end waypoint used for generating the path
     * @return the end waypoint
     */
    public Waypoint getEnd() {
        return end;
    }

    /**
     * Get the weight function used for calculating edge weights
     * @return the weight function used to calculate edge weights
     */
    public WeightFunction getWeightFunction() {
        return weightFunction;
    }

    /**
     * Generate the adjacency list for the provided graph
     * @param graph the graph to generate the adjacency list for
     * @return adjacency list
     */
    protected Map<Waypoint, List<Node>> generateAdjacencyList(Graph graph) {
        Map<Waypoint, List<Node>> adjacencyList = new HashMap<>();

        for (Waypoint vertex : graph.getVertices()) {
            List<Node> adjacentNodes = new ArrayList<>();
            adjacencyList.put(vertex, adjacentNodes);

            for (Edge edge : graph.getNeighbours(vertex)) {
                // TODO remove neighbours and rename getNeighbours to getNeighbours
                assert edge.getStart().equals(vertex); // TODO remove this when not needed
                adjacentNodes.add(new Node(edge.getEnd(), weightFunction.calculate(edge), edge.getDistance(), edge.getTransportMethod())); // use the weight function to calculate the weight/cost
            }
        }

        return adjacencyList;
    }

    /**
     * Perform the algorithm on the provided graph
     *
     * @param graph the graph to perform the algorithm on
     * @return the result of the algorithm
     */
    @Override
    public abstract Result<Route> perform(Graph graph);

    /**
     * A node for path finding
     */
    protected static class Node implements Comparator<Node> {
        /**
         * The waypoint node behind this priority queue node
         */
        protected Waypoint node;
        /**
         * The calculated cost for this node
         */
        protected double cost;
        /**
         * The original distance without a cost function added on
         */
        protected double distance;
        /**
         * The original transport method for the node
         */
        protected TransportMethod transportMethod;
        /**
         * The parent node
         */
        protected Node parent;

        /**
         * An empty constructor to allow default initialization
         */
        protected Node() { }

        /**
         * Construct a Node with the provided backing waypoint and the calculated cost
         * @param node the node waypoint backing the priority queue node
         * @param cost the calculated cost for this node
         * @param distance the distance without time/eco friendly penalties added on etc.
         * @param transportMethod the original transport method
         */
        protected Node(Waypoint node, double cost, double distance, TransportMethod transportMethod) {
            this.node = node;
            this.cost = cost;
            this.distance = distance;
            this.transportMethod = transportMethod;
        }

        /**
         * Compare the two nodes
         */
        @Override
        public int compare(Node o1, Node o2) {
            return Double.compare(o1.cost, o2.cost);
        }

        /**
         * Checks if the provided object equals this node
         * @param o the other object to check
         * @return true if equal, false if not
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node1 = (Node) o;
            return Double.compare(node1.cost, cost) == 0 &&
                    Double.compare(node1.distance, distance) == 0 &&
                    Objects.equals(node, node1.node) &&
                    Objects.equals(transportMethod, node1.transportMethod);
        }

        /**
         * Generate the hashcode for this object
         * @return generated hashcode
         */
        @Override
        public int hashCode() {
            return Objects.hash(node, cost, distance, transportMethod);
        }
    }
}
