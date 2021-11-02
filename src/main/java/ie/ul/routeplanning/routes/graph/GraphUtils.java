package ie.ul.routeplanning.routes.graph;

import ie.ul.routeplanning.routes.Waypoint;

/**
 * This class provides utilities for the graph package
 */
public class GraphUtils {
    /**
     * The factor to multiply the final waypoint distance by to convert it to kilometres
     */
    public static final double KM_CONVERSION = 1.609344;

    /**
     * The factor to multiply calculated waypoint distance by to get the final distance value
     */
    public static final double DISTANCE_FACTOR = 60 * 1.1515;

    /**
     * Calculates the kilometre distance between the coordinates of the 2 provided waypoints
     * @param w1 the first waypoint
     * @param w2 the second waypoint
     * @return the distance between the coordinate waypoints in kilometres
     */
    public static double kilometreDistance(Waypoint w1, Waypoint w2) {
        double latitude = w1.getLatitude(), longitude = w2.getLongitude(),
                latitude1 = w2.getLatitude(), longitude1 = w2.getLongitude();

        if ((latitude == latitude1) && (longitude == longitude1)) {
            return 0;
        } else {
            double theta = longitude - longitude1;
            double distance = Math.sin(Math.toRadians(latitude)) * Math.sin(Math.toRadians(latitude1))
                    * Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(latitude1))
                    * Math.cos(Math.toRadians(theta));
            distance = Math.acos(distance);

            return Math.toDegrees(distance) * DISTANCE_FACTOR * KM_CONVERSION;
        }
    }
}
