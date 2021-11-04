package ie.ul.routeplanning.routes.graph.function;

import ie.ul.routeplanning.routes.Route;
import ie.ul.routeplanning.routes.RouteLeg;
import ie.ul.routeplanning.routes.Waypoint;
import ie.ul.routeplanning.routes.graph.DistanceWeightFunction;
import ie.ul.routeplanning.routes.graph.Edge;
import ie.ul.routeplanning.routes.graph.Graph;
import ie.ul.routeplanning.routes.graph.WeightFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class will find all routes from the given point and the end point\
 */
public class PathFinder {

    /**
     * An instance of graph created.
     */
    private final Graph graph;

    /**
     * A list of routes from one point to another.
     */
    private List<Route> routeList = new ArrayList<>();

    /**
     * The weight function for calculating the weight between edges
     */
    private final WeightFunction weightFunction;

    /**
     * This constructor initializes the path finder with the provided graph and weight function.
     */
    public PathFinder(Graph graph, WeightFunction weightFunction) {
        this.graph = graph;
        this.weightFunction = weightFunction;
    }

    /**
     * Creates a path finder with default weight function
     * @param graph the graph to initialise the path finder with
     */
    public PathFinder(Graph graph) {
        this(graph, new DistanceWeightFunction());
    }

    /**
     * This method will call the recursive method which will find all routes from start to end.
     * @param start is the initial point.
     * @param end is the final point.
     * @param numberOfStops number of waypoints between the start and end point.
     *                      '-1' can mean any number of stops
     */
    public void findRoutes(Waypoint start, Waypoint end, int numberOfStops) {
        List<Edge> traversedEdges = new ArrayList<>();
        List<Waypoint> traversedWaypoints = new ArrayList<>();
        List<Route> listOfRoutes = new ArrayList<>();
        setRouteList(recursive(graph, start, end, numberOfStops, traversedWaypoints, traversedEdges, listOfRoutes));
    }

    /**
     * Finds the neighbor of the waypoint passed as argument
     * @param waypoint the main waypoint.
     * @return the waypoint connected to the main waypoint.
     */
    private Waypoint getNeighbour(Waypoint waypoint, Edge edge) {
        Waypoint start = edge.getStart();
        Waypoint end = edge.getEnd();
        // TODO this method doesn't make sense
        if (start.equals(waypoint)) {
            return end;
        } else {
            return start;
        }
    }

    /**
     * This recursive function will call itself and keep passing the arguments.
     * This function will traverse through all waypoints and finds all possible routes from start to end.
     * This function will come to an end eventually when there are no more waypoints left to visit.
     * @param graph the graph that contains all waypoints and edges
     * @param start the initial point.
     * @param end the destination.
     * @param numberOfStops number of waypoints between the start and end point;
     *                      '-1' can mean any number of stops.
     * @param traversedWaypoints the list of visited waypoints.
     * @param traversedEdges the list of visited edges.
     * @param listOfRoutes a list of routes, that stores a collection of RouteLegs
     * @return routeList once all RouteLegs have been traversed
     */
    private List<Route> recursive(Graph graph, Waypoint start, Waypoint end, int numberOfStops, List<Waypoint> traversedWaypoints,
                                           List<Edge> traversedEdges, List<Route> listOfRoutes) {
        traversedWaypoints.add(start);
        Waypoint next;

        if (numberOfStops != -1 && numberOfStops < traversedEdges.size()) {
            return listOfRoutes;
        }

        for (Edge edge : graph.getConnections(start)) {
            next = getNeighbour(start, edge);

            if (traversedWaypoints.contains(next)) {
                continue;
            }

            traversedEdges.add(edge);
            if (next.equals(end)) {
                List<RouteLeg> legs = traversedEdges.stream().map(RouteLeg::new).collect(Collectors.toList());
                listOfRoutes.add(new Route(legs));
                traversedEdges.remove(traversedEdges.size() - 1);
                continue;
            }

            listOfRoutes = recursive(graph, next, end, numberOfStops, traversedWaypoints, traversedEdges, listOfRoutes);
            traversedEdges.remove(traversedEdges.size() - 1);
            traversedWaypoints.remove(traversedWaypoints.size() - 1);
        }

        return listOfRoutes;
    }

    /**
     * @return a list of Routes.
     */
    public List<Route> getRouteList() {
        return routeList;
    }

    /**
     * @param listOfRouteLegs a list of Routes.
     */
    public void setRouteList(List<Route> listOfRouteLegs) {
        this.routeList = listOfRouteLegs;
    }
}