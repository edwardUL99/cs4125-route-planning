package ie.ul.routeplanning.controllers;

import ie.ul.routeplanning.constants.Constant;
import ie.ul.routeplanning.repositories.RouteRepository;
import ie.ul.routeplanning.repositories.WaypointRepository;
import ie.ul.routeplanning.routes.Route;
import ie.ul.routeplanning.routes.Waypoint;
import ie.ul.routeplanning.routes.algorithms.*;
import ie.ul.routeplanning.routes.data.SourceFactory;
import ie.ul.routeplanning.routes.graph.Graph;
import ie.ul.routeplanning.routes.graph.creation.BuilderException;
import ie.ul.routeplanning.routes.graph.creation.BuilderFactory;
import ie.ul.routeplanning.routes.graph.weights.WeightFunction;
import ie.ul.routeplanning.routes.graph.weights.WeightFunctionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

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
     * Lazily initialises the graph
     * @throws BuilderException if an exception occurs building the graph
     */
    private Graph getGraph() throws BuilderException {
        List<Waypoint> waypoints = new ArrayList<>();
        waypointRepository.findAll().forEach(waypoints::add);
        return BuilderFactory.fromFile("edges.json", SourceFactory.fromList(waypoints)).buildGraph();
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

        Optional<Waypoint> startOpt = findWaypoint(startWaypoint); //waypointFinder.matchWaypoint(startWaypoint);
        Optional<Waypoint> endOpt = findWaypoint(endWaypoint); //waypointFinder.matchWaypoint(endWaypoint);

        model.addAttribute("startWaypoint", startWaypoint);
        model.addAttribute("endWaypoint", endWaypoint);
        model.addAttribute("ecoFriendly", ecoFriendly);

        if (startWaypoint.equals(endWaypoint)) {
            model.addAttribute("error", "You cannot create a route with the same start and end waypoints");
        } else if (!startOpt.isPresent()) {
            model.addAttribute("error", String.format("No start waypoint found with name %s", startWaypoint));
        } else if (!endOpt.isPresent()) {
            model.addAttribute("error", String.format("No end waypoint found with name %s", endWaypoint));
        } else {
            Waypoint start = startOpt.get();
            Waypoint end = endOpt.get();

            // TODO need to find the next K shortest routes after dijkstra's suggestion
            WeightFunction weightFunction = new WeightFunctionBuilder()
                    .withEmissions(ecoFriendly)
                    .build();
            Algorithm<Route> dijkstra = AlgorithmFactory.dijkstraAlgorithm(start, end, weightFunction);
            Result<Route> result = dijkstra.perform(getGraph());
            List<Route> routes = result.collect();

            routeRepository.saveAll(routes);

            model.addAttribute("routes", routes);
        }

        return "routes";
    }

    /**
     * A get request to redirect from post request for route generation
     * @return the name of the view
     */
    @RequestMapping(value="/routes/generated", method=RequestMethod.GET)
    public String viewGeneratedRoutes(Model model, @ModelAttribute("startWaypoint") String startWaypoint,
                                      @ModelAttribute("endWaypoint") String endWaypoint,
                                      @ModelAttribute("routes") List<Route> routes,
                                      @ModelAttribute("ecoFriendly") boolean ecoFriendly) {
        model.addAttribute("startWaypoint", startWaypoint);
        model.addAttribute("endWaypoint", endWaypoint);
        model.addAttribute("routes", routes);
        model.addAttribute("ecoFriendly", ecoFriendly);
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
