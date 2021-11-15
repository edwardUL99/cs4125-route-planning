package ie.ul.routeplanning.routes;

import javax.persistence.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * A Route connects the initial point and the final point.
 */
@Entity
public class Route {
    /**
     * The ID of this Route
     */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    /**
     * A Route will have a list of one or more RouteLeg objects.
     */
    @OneToMany(cascade=CascadeType.ALL)
    private final List<RouteLeg> routeLegs;
    /**
     * Determines if this route is saved or not
     */
    protected boolean saved;

    /**
     * Constructor for Route class that initializes a list of RouteLegs
     */
    public Route() {
        routeLegs = new ArrayList<>();
    }

    /**
     * Parameterized constructor for Route class that takes in a list of RouteLegs.
     * @param routeLegs a list of RouteLegs passed as parameter which will form the Route.
     */
    public Route(List<RouteLeg> routeLegs) {
        this.routeLegs = new ArrayList<>();
        routeLegs.forEach(leg -> this.routeLegs.add(copyLeg(leg)));
    }

    /**
     * Retrieve the ID for this route
     * @return this route ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the id of this route
     * @param id this route's ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Adds a route leg to the list of route legs.
     * @param routeLeg route leg connecting two points
     */
    public void addRouteLeg(RouteLeg routeLeg) {
        routeLegs.add(copyLeg(routeLeg));
    }

    /**
     * Copy the route leg
     * @param leg the leg of the route to copy
     * @return the copied route leg
     */
    private RouteLeg copyLeg(RouteLeg leg) {
        return new RouteLeg(leg.getStart(), leg.getEnd(), leg.getTransportMethod(), leg.getDistance());
    }

    /**
     * Returns the list of route legs in this route.
     * @return the list of RouteLeg objects
     */
    public List<RouteLeg> getRouteLegs() {
        return routeLegs;
    }

    /**
     * Gets the start waypoint of the route
     * @return the start waypoint
     */
    public Waypoint getStart() {
        return routeLegs.get(0).getStart();
    }

    /**
     * Gets the end waypoint of the route
     * @return the end waypoint
     */
    public Waypoint getEnd() {
        return routeLegs.get(routeLegs.size() - 1).getEnd();
    }

    /**
     * Gets the number of intermediary stops
     * @return number of waypoints between start and end
     */
    public int getNumberStops() {
        int routeLegsSize = routeLegs.size();
        return (routeLegsSize == 1) ? 0:(routeLegsSize - 1);
    }

    /**
     * Calculates the total distance between the initial and final point of the route.
     * @return the sum of distances of all route legs
     */
    public double calculateDistance() {
        double totalDistance = 0;
        for(RouteLeg routeLeg : routeLegs) {
            totalDistance += routeLeg.calculateDistance();
        }

        return totalDistance;
    }

    /**
     * Calculates the total time taken to reach from the initial to the final point.
     * @return the total time taken to cover all the route legs
     */
    public Duration calculateTime() {
        Duration duration = Duration.ofMillis(0);

        for (RouteLeg routeLeg : routeLegs)
            duration = duration.plus(routeLeg.calculateTime());

        return duration;
    }

    /**
     * Calculates the average co2 emissions for this route
     * @return the calculated co2 emissions
     */
    public double calculateCO2Emissions() {
        double emissions = 0.0;

        for (RouteLeg routeLeg : routeLegs)
            emissions += routeLeg.calculateCO2Emissions();

        return emissions;
    }

    /**
     * A method to determine if this route has been saved on a user's account or not
     * @return true if a saved route, false if not
     */
    public boolean isSaved() {
        return saved;
    }
}