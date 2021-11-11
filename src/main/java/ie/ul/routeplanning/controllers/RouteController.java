package ie.ul.routeplanning.controllers;

import ie.ul.routeplanning.repositories.RouteRepository;
import ie.ul.routeplanning.routes.Route;
import ie.ul.routeplanning.routes.Waypoint;
import ie.ul.routeplanning.routes.graph.Graph;
import ie.ul.routeplanning.routes.graph.creation.BuilderException;
import ie.ul.routeplanning.services.GraphService;
import ie.ul.routeplanning.services.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

/**
 * The controller class for handling routes
 */
@Controller
public class RouteController {
    /**
     * Our repository for saving and loading routes
     */
    @Autowired
    private RouteRepository routeRepository;

    /**
     * The graph service for loading our graph
     */
    @Autowired
    private GraphService graphService;

    /**
     * Our route service for generating routes
     */
    @Autowired
    private RouteService routeService;

    /**
     * The home page for the routes
     * @param model the model for the view
     * @param response the response to the request
     * @return the name of the routes page
     */
    @RequestMapping(value="/routes", method=RequestMethod.GET)
    public String routesHome(Model model, HttpServletResponse response) {
        if (model.asMap().size() != 0) {
            response.addHeader("Cache-Control", "Public");
        }

        return "routes";
    }

    /**
     * Loads and returns the graph
     * @return the loaded graph or null if an error occurs
     */
    private Graph loadGraph() {
        Graph graph = null;

        try {
            graph = graphService.loadGraph();
        } catch (BuilderException ex) {
            ex.printStackTrace();
        }

        return graph;
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

            Graph graph = loadGraph();

            if (graph != null) {
                List<Route> routes = routeService.generateRoutes(graph, start, end, ecoFriendly);

                routeRepository.saveAll(routes);

                Route bestRoute = (routes.size() == 0) ? null : routes.remove(0);

                redirectAttributes.addFlashAttribute("bestRoute", bestRoute);
                redirectAttributes.addFlashAttribute("routes", routes);
            } else {
                redirectAttributes.addFlashAttribute("error", "An error occurred generating routes, please try again");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", parameters.getError());
        }
        modelAndView.setViewName("redirect:/routes");

        // TODO display co2 and time information in routes.html and route.html

        return modelAndView;
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
