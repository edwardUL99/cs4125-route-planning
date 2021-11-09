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
import ie.ul.routeplanning.services.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
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
     * Our route service for generating routes
     */
    @Autowired
    private RouteService routeService;

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
     * @param startWaypoint the name of the start waypoint
     * @param endWaypoint the name of the end waypoint
     * @param ecoFriendly true if the routes are to be filtered by eco friendliness
     * @return the name of the view
     */
    @RequestMapping(value="/routes", method=RequestMethod.POST)
    public ModelAndView generateRoutes(RedirectAttributes redirectAttributes, @RequestParam String startWaypoint, @RequestParam String endWaypoint,
                                       @RequestParam(required=false) boolean ecoFriendly) throws BuilderException {
        RouteService.RouteParameters parameters = routeService.parseParameters(startWaypoint, endWaypoint);

        redirectAttributes.addFlashAttribute("startWaypoint", startWaypoint);
        redirectAttributes.addFlashAttribute("endWaypoint", endWaypoint);
        redirectAttributes.addFlashAttribute("ecoFriendly", ecoFriendly);

        ModelAndView modelAndView = new ModelAndView();

        if (parameters.isValid()) {
            Waypoint start = parameters.getStart();
            Waypoint end = parameters.getEnd();

            List<Route> routes = routeService.generateRoutes(getGraph(), start, end, ecoFriendly);

            routeRepository.saveAll(routes);

            Route bestRoute = (routes.size() == 0) ? null:routes.remove(0);

            redirectAttributes.addFlashAttribute("bestRoute", bestRoute);
            redirectAttributes.addFlashAttribute("routes", routes);

            modelAndView.setViewName("redirect:/routes/generated");
        } else {
            redirectAttributes.addFlashAttribute("error", parameters.getError());
            modelAndView.setViewName("redirect:/routes");
        }

        return modelAndView;
    }

    /**
     * Views the generated routes
     * @param model the model for holding the attributes
     * @return the name of the view
     */
    @RequestMapping("/routes/generated")
    public String viewGeneratedRoutes(Model model, HttpServletResponse response) {
        if (model.asMap().size() == 0) {
            return "redirect:/routes";
        } else {
            response.addHeader("Cache-Control", "Public");
            return "routes";
        }
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
