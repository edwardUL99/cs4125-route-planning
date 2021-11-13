package ie.ul.routeplanning.routes.graph;

import ie.ul.routeplanning.routes.RouteLeg;
import ie.ul.routeplanning.routes.Waypoint;

import java.util.*;

/**
 * This class represents a graph of vertices representing a travel network
 *
 * This treats waypoints as vertices and RouteLegs as edges. From here, waypoints are known as vertices and routelegs are
 * edges
 */
public class Graph {
    /**
     * Mapping of the waypoint id to the corresponding waypoint
     */
    private final Map<Long, Waypoint> vertices;
    /**
     * This holds all the edges for each vertex in the Graph with each vertex mapping to a neighbour
     */
    private final Map<Waypoint, List<Edge>> edges;

    /**
     * Constructs an empty Graph with no vertices and edges
     */
    public Graph() {
        vertices = new HashMap<>();
        edges = new HashMap<>();
    }

    /**
     * A copy constructor to help facilitate the copy method
     * @param vertices the list of vertices to copy over
     * @param edges the list of edges to copy over
     */
    private Graph(Map<Long, Waypoint> vertices, Map<Waypoint, List<Edge>> edges) {
        this.vertices = vertices;
        this.edges = edges;
    }

    /**
     * Gets a set of all the vertices in the graph
     * @return the set of vertices in the graph
     */
    public Set<Waypoint> getVertices() {
        return new LinkedHashSet<>(vertices.values());
    }

    /**
     * Adds the provided vertex to the graph if it hasn't already been added
     * @param v the vertex to add
     */
    public void addVertex(Waypoint v) {
        if (!vertices.containsValue(v)) {
            Long vertexNumber = v.getId();

            if (vertexNumber == null) {
                vertexNumber = (long)vertices.size() + 1;
                v.setId(vertexNumber);
            }

            vertices.put(vertexNumber, v);
            edges.put(v, new ArrayList<>());
        }
    }

    /**
     * Retrieve vertex n from the Graph
     * @param n the vertex to retrieve
     * @return the retrieved vertex, or null if not found
     */
    public Waypoint getVertex(long n) {
        return vertices.get(n);
    }

    /**
     * Retrieve the edges for the provided vertex
     * @param waypoint the vertex to retrieve edges for
     * @return an unmodifiable list of edges, or null if the vertex is not in the graph
     */
    public List<Edge> getNeighbours(Waypoint waypoint) {
        return edges.get(waypoint);
    }

    /**
     * Removes the provided edge. Uses the edge's start waypoint to find the list of edges to remove it from
     * @param edge the edge to remove
     */
    public void removeEdge(Edge edge) {
        edges.get(edge.getStart()).remove(edge);
    }

    /**
     * Determines if the graph contains an edge from u to v.
     * @param edge the edge with vertex u and v
     * @return true if the graph contains u and v and there is an edge between them
     */
    public boolean containsEdge(Edge edge) {
        Waypoint u = edge.getStart();
        List<Edge> edges;
        return (edges = this.edges.get(u)) != null && edges.contains(edge);
    }

    /**
     * This method adds an edge from vertex u to v. An edge means that there is a possible connection between u and v.
     * If bidirectional, an edge is added from v to u also
     * @param edge the edge to add
     * @param bidirectional true if it is a bidirectional edge, false if not
     */
    public void addEdge(Edge edge, boolean bidirectional) {
        if (!containsEdge(edge)) {
            Waypoint u = edge.getStart(), v = edge.getEnd();
            if (!vertices.containsValue(u))
                addVertex(u);

            if (!vertices.containsValue(v))
                addVertex(v);

            edges.get(u).add(edge); // v has been added as a neighbour of u, so there is now an edge between them

            if (bidirectional) {
                edges.get(v).add(edge.reverse());
            }
        }
    }

    /**
     * This method adds a bidirectional edge from vertex u to v and then v to u. An edge means that there is a possible connection between u and v
     * @param edge the edge to add
     */
    public void addEdge(Edge edge) {
        addEdge(edge, true);
    }

    /**
     * Creates a deep copy of this graph
     * @return the copied graph
     */
    public Graph copy() {
        Map<Long, Waypoint> vertices = new HashMap<>(this.vertices.size());
        Map<Waypoint, List<Edge>> edges = new HashMap<>(this.edges.size());

        for (Map.Entry<Long, Waypoint> e : this.vertices.entrySet()) {
            Long id = e.getKey();
            Waypoint vertex = e.getValue();

            vertices.put(id, new Waypoint(vertex.getId(), vertex.getName(), vertex.getLatitude(), vertex.getLongitude()));
        }

        for (Map.Entry<Waypoint, List<Edge>> e : this.edges.entrySet()) {
            Waypoint key = e.getKey();
            List<Edge> edgeList = e.getValue();

            Waypoint copied = vertices.get(key.getId());

            List<Edge> copiedEdges = new ArrayList<>();

            for (Edge edge : edgeList) {
                copiedEdges.add(new RouteLeg(edge));
            }

            edges.put(copied, copiedEdges);
        }

        return new Graph(vertices, edges); // use the copy constructor
    }

    /**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        int[] edgeSize = new int[]{0};
        edges.forEach((k, v) -> edgeSize[0] += v.size());
        return String.format("Graph of %d vertices and %d edges", vertices.size(), edgeSize[0]);
    }
}
