package ie.ul.routeplanning.routes.graph.function;

import ie.ul.routeplanning.routes.Route;
import ie.ul.routeplanning.routes.RouteLeg;
import ie.ul.routeplanning.routes.Waypoint;
import ie.ul.routeplanning.routes.graph.Graph;

import java.util.ArrayList;
import java.util.List;

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
     * This constructor initializes the graph object.
     */
    public PathFinder(Graph graph) {
        this.graph = graph;
    }

    /**
     * This method will call the recursive method which will find all routes from start to end.
     * @param start is the initial point.
     * @param end is the final point.
     * @param numberOfStops number of waypoints between the start and end point.
     *                      '-1' can mean any number of stops
     */
    public void findRoutes(Waypoint start, Waypoint end, int numberOfStops) {
        List<RouteLeg> traversedRouteLegs = new ArrayList<>();
        List<Waypoint> traversedWaypoints = new ArrayList<>();
        List<Route> listOfRoutes = new ArrayList<>();
        setRouteList(recursive(graph, start, end, numberOfStops, traversedWaypoints, traversedRouteLegs, listOfRoutes));
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
     * @param traversedRouteLegs the list of visited RouteLegs.
     * @param listOfRoutes a list of routes, that stores a collection of RouteLegs
     * @return routeList once all RouteLegs have been traversed
     */
    private List<Route> recursive(Graph graph, Waypoint start, Waypoint end, int numberOfStops, List<Waypoint> traversedWaypoints,
                                           List<RouteLeg> traversedRouteLegs, List<Route> listOfRoutes) {

        traversedWaypoints.add(start);
        Waypoint next;

        if(numberOfStops != -1 && numberOfStops < traversedRouteLegs.size()) {
            return listOfRoutes;
        }

        for (RouteLeg routeLeg : graph.getConnections(start)) {
            next = routeLeg.getNeighbor(start);

            if (traversedWaypoints.contains(next)) {
                continue;
            }

            traversedRouteLegs.add(routeLeg);
            if (next.equals(end)) {
                listOfRoutes.add(new Route(traversedRouteLegs));
                traversedRouteLegs.remove(traversedRouteLegs.size() - 1);
                continue;
            }

            listOfRoutes = recursive(graph, next, end, numberOfStops, traversedWaypoints, traversedRouteLegs, listOfRoutes);
            traversedRouteLegs.remove(traversedRouteLegs.size() - 1);
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