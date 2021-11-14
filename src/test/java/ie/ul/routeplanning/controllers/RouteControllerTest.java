package ie.ul.routeplanning.controllers;

import ie.ul.routeplanning.routes.Route;
import ie.ul.routeplanning.routes.RouteLeg;
import ie.ul.routeplanning.routes.Waypoint;
import ie.ul.routeplanning.routes.graph.Graph;
import ie.ul.routeplanning.services.GraphService;
import ie.ul.routeplanning.services.RouteService;
import ie.ul.routeplanning.services.SecurityService;
import ie.ul.routeplanning.services.UserService;
import ie.ul.routeplanning.transport.TransportFactory;
import ie.ul.routeplanning.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * This class is used to unit test the route controller
 */
@SpringBootTest
public class RouteControllerTest {
    /**
     * Web app context for testing
     */
    @Autowired
    private WebApplicationContext webApplicationContext;
    /**
     * Used for mocking MVC
     */
    private MockMvc mockMvc;
    /**
     * The mocked route service
     */
    @MockBean
    private RouteService routeServiceMock;
    /**
     * The mocked graph service
     */
    @MockBean
    private GraphService graphServiceMock;
    /**
     * The mocked security service
     */
    @MockBean
    private SecurityService securityServiceMock;
    /**
     * The mocked user service
     */
    @MockBean
    private UserService userServiceMock;
    /**
     * Array of waypoints used for testing purposes
     */
    private Waypoint[] TEST_WAYPOINTS;
    /**
     * A graph we use for testing purposes
     */
    private Graph TEST_GRAPH;
    /**
     * A route result used for testing purposes
     */
    private List<Route> TEST_ROUTES;

    /**
     * Initialises the test objects
     */
    @BeforeEach
    private void init() {
        TEST_GRAPH = new Graph();

        Waypoint[] waypoints = {
                new Waypoint(1L, "Tralee", 1000.00, 2000.00),
                new Waypoint(2L, "Cork", 1500.00, 2000.00),
                new Waypoint(3L, "Dublin", 1510.00, 2100.00)
        };

        RouteLeg[] edges = {
                new RouteLeg(waypoints[0], waypoints[1],
                        TransportFactory.getTransportMethod(TransportFactory.TransportMethods.BUS), 120.00),
                new RouteLeg(waypoints[0], waypoints[2],
                        TransportFactory.getTransportMethod(TransportFactory.TransportMethods.TRAIN), 350.00),
                new RouteLeg(waypoints[1], waypoints[2],
                        TransportFactory.getTransportMethod(TransportFactory.TransportMethods.PLANE), 250.00)
        };

        for (RouteLeg edge : edges)
            TEST_GRAPH.addEdge(edge);

        List<Route> routes = new ArrayList<>();

        Route route = new Route();
        route.addRouteLeg(edges[0]);
        routes.add(route);

        route = new Route();
        route.addRouteLeg(edges[1]);
        route.addRouteLeg((RouteLeg)edges[2].reverse());
        routes.add(route);

        TEST_ROUTES = routes;
        TEST_WAYPOINTS = waypoints;
    }

    /**
     * Initialises the test mocks
     */
    @BeforeEach
    private void initMocks() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        MockitoAnnotations.openMocks(this);
    }

    /**
     * This test tests that when the route service is asked for a route and there are two routes available, bestRoute attributes should be set and routes should be of size 1
     */
    @Test
    void shouldReturnBestRouteAndOneOther() throws Exception {
        Waypoint startWaypoint = TEST_WAYPOINTS[0];
        Waypoint endWaypoint = TEST_WAYPOINTS[1];
        String startWaypointName = startWaypoint.getName();
        String endWaypointName = endWaypoint.getName();

        RouteService.RouteParameters parameters = RouteService.RouteParameters.createValid(startWaypoint, endWaypoint);

        when(routeServiceMock.generateRoutes(TEST_GRAPH, startWaypoint, endWaypoint, false, false))
                .thenReturn(TEST_ROUTES);
        when(routeServiceMock.parseParameters(startWaypointName, endWaypointName))
                .thenReturn(parameters);
        when(graphServiceMock.loadGraph())
                .thenReturn(TEST_GRAPH);

        Route bestRoute = TEST_ROUTES.get(0);
        Route nextRoute = TEST_ROUTES.get(1);

        mockMvc.perform(post("/routes")
                .param("startWaypoint", startWaypointName)
                .param("endWaypoint", endWaypointName)
                .param("ecoFriendly", "false")
                .param("time", "false"))
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/routes"))
                .andExpect(flash().attribute("startWaypoint", is(startWaypointName)))
                .andExpect(flash().attribute("endWaypoint", is(endWaypointName)))
                .andExpect(flash().attribute("ecoFriendly", false))
                .andExpect(flash().attribute("time", false))
                .andExpect(flash().attribute("routes", hasSize(1)))
                .andExpect(flash().attribute("routes", hasItem(nextRoute)))
                .andExpect(flash().attribute("bestRoute", is(bestRoute)));

        verify(routeServiceMock).generateRoutes(TEST_GRAPH, startWaypoint, endWaypoint, false, false);
        verify(graphServiceMock).loadGraph();
    }

    /**
     * This test tests that when the route service is asked for a route, and only 1 "best route" is available, it should give back the provided route result
     */
    @Test
    void shouldReturnRouteOnlyOneRoute() throws Exception {
        Waypoint startWaypoint = TEST_WAYPOINTS[0];
        Waypoint endWaypoint = TEST_WAYPOINTS[1];
        String startWaypointName = startWaypoint.getName();
        String endWaypointName = endWaypoint.getName();

        TEST_ROUTES.remove(1);

        RouteService.RouteParameters parameters = RouteService.RouteParameters.createValid(startWaypoint, endWaypoint);

        when(routeServiceMock.generateRoutes(TEST_GRAPH, startWaypoint, endWaypoint, false, false))
                .thenReturn(TEST_ROUTES);
        when(routeServiceMock.parseParameters(startWaypointName, endWaypointName))
                .thenReturn(parameters);
        when(graphServiceMock.loadGraph())
                .thenReturn(TEST_GRAPH);

        Route bestRoute = TEST_ROUTES.get(0);

        mockMvc.perform(post("/routes")
                            .param("startWaypoint", startWaypointName)
                            .param("endWaypoint", endWaypointName)
                            .param("ecoFriendly", "false")
                            .param("time", "false"))
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/routes"))
                .andExpect(flash().attribute("startWaypoint", is(startWaypointName)))
                .andExpect(flash().attribute("endWaypoint", is(endWaypointName)))
                .andExpect(flash().attribute("ecoFriendly", false))
                .andExpect(flash().attribute("time", false))
                .andExpect(flash().attribute("routes", hasSize(0)))
                .andExpect(flash().attribute("bestRoute", is(bestRoute)));

        verify(routeServiceMock).generateRoutes(TEST_GRAPH, startWaypoint, endWaypoint, false, false);
        verify(graphServiceMock).loadGraph();
    }

    /**
     * This test tests that a route should be displayed successfully.
     */
    @Test
    void shouldDisplaySavedTest() throws Exception {
        Route route = TEST_ROUTES.get(0);

        when(routeServiceMock.getRoute(1L))
                .thenReturn(route);

        mockMvc.perform(get("/routes/" + 1))
                .andExpect(status().isOk())
                .andExpect(view().name("route"))
                .andExpect(model().attribute("route", is(route)));

        verify(routeServiceMock).getRoute(1L);
    }

    /**
     * This tests the successful saving of a route
     */
    @Test
    void shouldSaveRoute() throws Exception {
        long routeId = 1;
        Route route = TEST_ROUTES.get(0);
        String username = "testUser";
        User user = new User();
        user.setUsername(username);

        when(routeServiceMock.getRoute(routeId))
                .thenReturn(route);
        when(securityServiceMock.getUsername())
                .thenReturn(username);
        when(userServiceMock.findByUsername(username))
                .thenReturn(user);

        mockMvc.perform(post("/routes/save_route").param("saveRouteID", "" + routeId))
                .andExpect(status().is(302))
                .andExpect(flash().attribute("success", "Route has been saved successfully"))
                .andExpect(redirectedUrl("/routes/" + routeId));

        verify(routeServiceMock).getRoute(routeId);
        verify(securityServiceMock).getUsername();
        verify(userServiceMock).findByUsername(username);
    }

    /**
     * This tests what should happen if the route doesn't exist
     */
    @Test
    void shouldNotSaveIfRouteIsNull() throws Exception {
        long routeId = 1;

        when(routeServiceMock.getRoute(routeId))
                .thenReturn(null);

        mockMvc.perform(post("/routes/save_route").param("saveRouteID", "" + routeId))
                .andExpect(status().is(302))
                .andExpect(flash().attribute("error", "The route does not exist"))
                .andExpect(redirectedUrl("/routes"));

        verify(routeServiceMock).getRoute(routeId);
    }

    /**
     * This tests that an error occurs if the username is null as user is not logged in or anonymous
     */
    @Test
    void shouldNotSaveWhenUsernameIsNull() throws Exception {
        long routeId = 1;
        Route route = TEST_ROUTES.get(0);

        when(routeServiceMock.getRoute(routeId))
                .thenReturn(route);
        when(securityServiceMock.getUsername())
                .thenReturn(null);

        mockMvc.perform(post("/routes/save_route").param("saveRouteID", "" + routeId))
                .andExpect(status().is(302))
                .andExpect(flash().attribute("error", "You must be a registered and logged in user to save a route"))
                .andExpect(redirectedUrl("/routes"));

        verify(routeServiceMock).getRoute(routeId);
        verify(securityServiceMock).getUsername();
    }
}
