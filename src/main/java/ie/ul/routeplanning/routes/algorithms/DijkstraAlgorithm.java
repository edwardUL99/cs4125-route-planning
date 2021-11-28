package ie.ul.routeplanning.routes.algorithms;

import ie.ul.routeplanning.routes.Route;
import ie.ul.routeplanning.routes.Waypoint;
import ie.ul.routeplanning.routes.graph.Graph;
import ie.ul.routeplanning.routes.graph.weights.WeightFunction;

import java.util.*;

/**
 * This class represents the Dijkstra's shortest path algorithm for finding the shortest path based on the provided weight
 * function
 */
public class DijkstraAlgorithm extends PathFindingAlgorithm {
    /**
     * Constructs Dijkstra's algorithm with the provided start and end waypoints and the weight function for calculating
     * the weight of an edge
     * @param start the start waypoint
     * @param end the end waypoint
     * @param weightFunction the weight function for calculating the edge weights
     */
    public DijkstraAlgorithm(Waypoint start, Waypoint end, WeightFunction weightFunction) {
        super(start, end, weightFunction);
    }

    /**
     * Constructs Dijkstra's algorithm with the default weight function
     * @param start the start waypoint
     * @param end the end waypoint
     */
    public DijkstraAlgorithm(Waypoint start, Waypoint end) {
        super(start, end);
    }

    /**
     * Perform the dijkstra's after all the necessary data structures have been initialised
     * @param adjacencyList the generated adjacency list
     * @param distances the distances map to keep track of distance costs
     * @param priorityQueue the priority queue backing the algorithm
     * @param visited the set of visited waypoints
     * @return the generated route
     */
    private Route doDijkstra(Map<Waypoint, List<Node>> adjacencyList, Map<Waypoint, Double> distances,
                             PriorityQueue<Node> priorityQueue, Set<Waypoint> visited) {
        int vertices = adjacencyList.size();

        Node target = null;

        while (visited.size() != vertices && !priorityQueue.isEmpty()) {
            Node node = priorityQueue.remove();
            Waypoint u = node.node;
            double uDist = distances.get(u);

            if (u.equals(end)) {
                target = node;
                break; // we found our end node. Since Dijkstra's always follows the shortest path, we can break after the end is reached
            }

            if (node.cost > uDist)
                continue;

            visited.add(u);

            double edgeDistance, newDistance;

            List<Node> adjacentNodes = adjacencyList.get(u);

            for (Node adjacent : adjacentNodes) {
                if (!visited.contains(adjacent.node)) {
                    edgeDistance = adjacent.cost;
                    newDistance = uDist + edgeDistance;

                    if (newDistance < distances.get(adjacent.node)) {
                        distances.put(adjacent.node, newDistance);
                        adjacent.parent = node;
                        priorityQueue.add(adjacent);
                    }
                }
            }
        }

        if (target != null) {
            List<Node> path = constructPath(target);

            return convertPathToRoute(path);
        } else {
            return null;
        }
    }

    /**
     * Generate the route using dijkstra's algorithm
     * @param graph the graph to use for route generation
     * @return the generated route
     */
    protected Route dijkstra(Graph graph) {
        Map<Waypoint, List<Node>> adjacencyList = generateAdjacencyList(graph);
        int vertices = adjacencyList.size(); // the number of vertices in the graph
        Map<Waypoint, Double> distances = new HashMap<>();
        Set<Waypoint> visited = new HashSet<>();
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(vertices, new Node());

        adjacencyList.keySet().forEach(k -> distances.put(k, Double.MAX_VALUE)); // initialise the distances to infinity

        priorityQueue.add(new Node(start, 0, 0, null)); // no cost to travel to start
        distances.put(start, 0.0);

        return doDijkstra(adjacencyList, distances, priorityQueue, visited);
    }

    /**
     * Perform the algorithm on the provided graph
     *
     * @param graph the graph to perform the algorithm on
     * @return the result of the algorithm
     */
    @Override
    public Result<Route> perform(Graph graph) {
        Result<Route> routeResult = new ListResult<>();
        Route route = dijkstra(graph);

        if (route != null)
            routeResult.addItem(route);

        return routeResult;
    }
}
