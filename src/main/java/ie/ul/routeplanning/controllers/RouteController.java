package ie.ul.routeplanning.controllers;

import ie.ul.routeplanning.constants.Constant;
import ie.ul.routeplanning.repositories.WaypointRepository;
import ie.ul.routeplanning.routes.Route;
import ie.ul.routeplanning.routes.Waypoint;
import ie.ul.routeplanning.routes.graph.Graph;
import ie.ul.routeplanning.routes.graph.creation.BuilderException;
import ie.ul.routeplanning.services.*;
import ie.ul.routeplanning.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The controller class for handling routes
 */
@Controller
public class RouteController {
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
     * Our waypoint service for retrieving waypoints
     */
    @Autowired
    private WaypointService waypointService;

    /**
     * Our user service for finding users
     */
    @Autowired
    private UserService userService;

    /**
     * Our security service for determining users
     */
    @Autowired
    private SecurityService securityService;

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
     * Validates the start and end waypoints, setting the appropriate references
     * @param startWaypoint the reference containing the start waypoint name. This method capitalises the waypoint name, so changes the reference
     * @param endWaypoint the reference containing the end waypoint name. Does same processing as start
     * @param error the reference containing error message. If this is null, no error occurred
     * @param start the reference which will after executing this method, contain the start waypoint
     * @param end the reference that will after executing this method, contain the end waypoint
     */
    private void validateParameters(AtomicReference<String> startWaypoint, AtomicReference<String> endWaypoint, AtomicReference<String> error,
                                    AtomicReference<Waypoint> start, AtomicReference<Waypoint> end) {
        String startWaypointName = startWaypoint.get();
        String endWaypointName = endWaypoint.get();

        startWaypointName = Constant.capitalise(startWaypointName);
        endWaypointName = Constant.capitalise(endWaypointName);

        Waypoint startWaypointFound = waypointService.findWaypoint(startWaypointName);
        Waypoint endWaypointFound = waypointService.findWaypoint(endWaypointName);

        if (startWaypointName.equals(endWaypointName)) {
            error.set("You cannot create a route with the same start and end waypoints");
        } else if (startWaypointFound == null) {
            error.set(String.format("No start waypoint found with name %s", startWaypoint));
        } else if (endWaypointFound == null) {
            error.set(String.format("No end waypoint found with name %s", endWaypoint));
        } else {
            startWaypoint.set(startWaypointName);
            endWaypoint.set(endWaypointName);
            start.set(startWaypointFound);
            end.set(endWaypointFound);
        }
    }

    /**
     * The handler for when route generation is requested
     * @param startWaypoint the name of the start waypoint
     * @param endWaypoint the name of the end waypoint
     * @param ecoFriendly true if the routes are to be filtered by eco friendliness
     * @param time        true if time should be factored into the route duration
     * @return the name of the view
     */
    @RequestMapping(value="/routes", method=RequestMethod.POST)
    public ModelAndView generateRoutes(RedirectAttributes redirectAttributes, @RequestParam String startWaypoint, @RequestParam String endWaypoint,
                                       @RequestParam(required=false) boolean ecoFriendly, @RequestParam(required=false) boolean time) {
        AtomicReference<String> startWayRef = new AtomicReference<>(startWaypoint);
        AtomicReference<String> endWayRef = new AtomicReference<>(endWaypoint);
        AtomicReference<String> errorRef = new AtomicReference<>();
        AtomicReference<Waypoint> startRef = new AtomicReference<>();
        AtomicReference<Waypoint> endRef = new AtomicReference<>();

        validateParameters(startWayRef, endWayRef, errorRef, startRef, endRef);

        redirectAttributes.addFlashAttribute("startWaypoint", startWayRef.get());
        redirectAttributes.addFlashAttribute("endWaypoint", endWayRef.get());
        redirectAttributes.addFlashAttribute("ecoFriendly", ecoFriendly);
        redirectAttributes.addFlashAttribute("time", time);

        ModelAndView modelAndView = new ModelAndView();

        String error = errorRef.get();

        if (error == null) {
            Waypoint start = startRef.get();
            Waypoint end = endRef.get();

            Graph graph = loadGraph();

            if (graph != null) {
                List<Route> routes = routeService.generateRoutes(graph, start, end, ecoFriendly, time);

                Route bestRoute = (routes.size() == 0) ? null : routes.remove(0);

                redirectAttributes.addFlashAttribute("bestRoute", bestRoute);
                redirectAttributes.addFlashAttribute("routes", routes);
            } else {
                redirectAttributes.addFlashAttribute("error", "An error occurred generating routes, please try again");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", error);
        }
        modelAndView.setViewName("redirect:/routes");

        return modelAndView;
    }

    /**
     * The controller method for accessing a route
     * @param model the model for this request
     * @param routeID the ID of the route passed in on the request
     * @return the name of the view to load
     */
    @RequestMapping("/routes/{routeID}")
    public String getRoute(Model model, @PathVariable Long routeID) {
        Route route = routeService.getRoute(routeID);
        model.addAttribute("route", route);

        return "route";
    }

    /**
     * Saves the route to the user's account
     * @param redirectAttributes the attributes to add redirected values to
     * @param saveRouteID the ID of the route to save
     * @return the redirected view
     */
    @RequestMapping(value="/routes/save_route", method=RequestMethod.POST)
    public ModelAndView saveRoute(RedirectAttributes redirectAttributes, @RequestParam Long saveRouteID) {
        Route route = routeService.getRoute(saveRouteID);

        ModelAndView modelAndView = new ModelAndView();

        if (route == null) {
            redirectAttributes.addFlashAttribute("error", "The route does not exist");
            modelAndView.setViewName("redirect:/routes");
        } else {
            String username = securityService.getUsername();

            if (username != null) {
                User user = userService.findByUsername(username);
                routeService.saveRoute(user, route);

                redirectAttributes.addFlashAttribute("success", "Route has been saved successfully");

                modelAndView.setViewName("redirect:/routes/" + saveRouteID);
            } else {
                redirectAttributes.addFlashAttribute("error", "You must be a registered and logged in user to save a route");
                modelAndView.setViewName("redirect:/routes");
            }
        }

        return modelAndView;
    }

    /**
     * Retrieves the saved routes for the user
     * @param model the model to add attributes to
     * @return the name of the view
     */
    @RequestMapping(value="/routes/saved", method=RequestMethod.GET)
    public String savedRoutes(Model model) {
        String username = securityService.getUsername();

        if (username != null) {
            User user = userService.findByUsername(username);
            List<Route> savedRoutes = routeService.getSavedRoutes(user);

            model.addAttribute("user", user);
            model.addAttribute("routes", savedRoutes);

            return "saved_routes";
        } else {
            return "redirect:/";
        }
    }

    /**
     * Perform an action on the provided saved route
     * @param model the model for the view
     * @param action the action to perform on the saved route
     * @param routeID the ID of the route to perform the action on
     * @return the name of the view
     */
    @RequestMapping(value="/routes/saved", method=RequestMethod.POST)
    public String savedRouteAction(Model model, @RequestParam String action, @RequestParam Long routeID) {
        action = action.toUpperCase();

        if (action.equals("DELETE")) {
            routeService.deleteRoute(routeID);
        }

        return "redirect:/routes/saved";
    }
}
