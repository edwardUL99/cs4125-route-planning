package ie.ul.routeplanning.controllers;

import ie.ul.routeplanning.constants.Constant;
import ie.ul.routeplanning.repositories.RouteRepository;
import ie.ul.routeplanning.repositories.WaypointRepository;
import ie.ul.routeplanning.routes.Route;
import ie.ul.routeplanning.routes.Waypoint;
import ie.ul.routeplanning.routes.data.SourceFactory;
import ie.ul.routeplanning.routes.graph.Graph;
import ie.ul.routeplanning.routes.graph.creation.BuilderException;
import ie.ul.routeplanning.routes.graph.creation.BuilderFactory;
import ie.ul.routeplanning.routes.graph.function.PathFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The controller class for handling routes
 */
@Controller
public class RouteController {
    /**
     * Our repository for saving and loading waypoints
     */
    @Autowired
    private WaypointRepository waypointRepository;

    /**
     * Our repository for saving and loading routes
     */
    @Autowired
    private RouteRepository routeRepository;

    /**
     * The graph instance for the route controller
     */
    private Graph graph;

    /**
     * Lazily initialises the graph
     * @throws BuilderException if an exception occurs building the graph
     */
    private Graph getGraph() throws BuilderException {
        if (graph == null) {
            List<Waypoint> waypoints = new ArrayList<>();
            waypointRepository.findAll().forEach(waypoints::add);
            graph = BuilderFactory.fromFile("edges.json", SourceFactory.fromList(waypoints)).buildGraph();
        }

        return graph;
    }

    /**
     * The home page for the routes
     * @param model the model for the view
     * @return the name of the routes page
     */
    @RequestMapping(value="/routes", method=RequestMethod.GET)
    public String routesHome(Model model) {
        return "routes";
    }

    /**
     * Find a waypoint with the provided name
     * @param name the name of the waypoint
     * @return an optional containing the waypoint, empty if not found
     */
    private Optional<Waypoint> findWaypoint(String name) {
        return waypointRepository.findByName(name).stream().findFirst();
    }

    /**
     * The handler for when route generation is requested
     * @param model the model for the view
     * @param startWaypoint the name of the start waypoint
     * @param endWaypoint the name of the end waypoint
     * @param ecoFriendly true if the routes are to be filtered by eco friendliness
     * @return the name of the view
     */
    @RequestMapping(value="/routes", method=RequestMethod.POST)
    public String generateRoutes(Model model, @RequestParam String startWaypoint, @RequestParam String endWaypoint,
                                 @RequestParam(required=false) boolean ecoFriendly) throws BuilderException {
        startWaypoint = Constant.capitalise(startWaypoint);
        endWaypoint = Constant.capitalise(endWaypoint);

        Optional<Waypoint> start = findWaypoint(startWaypoint); //waypointFinder.matchWaypoint(startWaypoint);
        Optional<Waypoint> end = findWaypoint(endWaypoint); //waypointFinder.matchWaypoint(endWaypoint);

        model.addAttribute("startWaypoint", startWaypoint);
        model.addAttribute("endWaypoint", endWaypoint);
        model.addAttribute("ecoFriendly", ecoFriendly);

        if (start.equals(end)) {
            model.addAttribute("error", "You cannot create a route with the same start and end waypoints");
        } else if (!start.isPresent()) {
            model.addAttribute("error", String.format("No start waypoint found with name %s", startWaypoint));
        } else if (!end.isPresent()) {
            model.addAttribute("error", String.format("No end waypoint found with name %s", endWaypoint));
        } else {
            PathFinder pathFinder = new PathFinder(getGraph());
            pathFinder.findRoutes(start.get(), end.get(), -1);

            List<Route> routes = pathFinder.getRouteList();
            routeRepository.saveAll(routes);

            model.addAttribute("routes", routes);
        }

        return "routes";
    }

    /**
     * The controller method for accessing a route
     * @param model the model for this request
     * @param routeID the ID of the route passed in on the request
     * @return the name of the view to load
     */
    @RequestMapping("/route/{routeID}")
    public String getRoute(Model model, @PathVariable Long routeID) {
        Optional<Route> route = routeRepository.findById(routeID);
        model.addAttribute("route", route.orElse(null));

        return "route";
    }
}