package ie.ul.routeplanning.routes.algorithms;

import ie.ul.routeplanning.routes.Route;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class represents a result that can contain 1 or more items, each of which is a route
 */
public class RouteResult implements Result<Route> {
    /**
     * The list of routes behind the route result
     */
    private final List<Route> routes;
    /**
     * The position in the list
     */
    private int position;

    /**
     * Construct the RouteResult from the provided routes
     * @param routes the list of routes to initialise the result with
     */
    public RouteResult(Collection<Route> routes) {
        this.routes = new ArrayList<>(routes);
    }

    /**
     * Constructs a default
     */
    public RouteResult() {
        this(new ArrayList<>());
    }

    /**
     * Collect all the remaining elements in this result into a single list.
     * I.e. if 3 items have already been taken using next(), the elements returned should only be the remaining elements,
     * excluding those 3. If no elements are available, this returns an empty list
     * This operation should end in the result having no more elements.
     * @return the list of all elements in the result.
     */
    @Override
    public List<Route> collect() {
        List<Route> routes = new ArrayList<>();

        for (int i = this.position; i < this.routes.size(); i++, this.position++) {
            routes.add(this.routes.get(i));
        }

        return routes;
    }

    /**
     * Get the next element in the result
     *
     * @return the next element of the result, null if there are no more elements
     */
    @Override
    public Route next() {
        return (hasNext()) ? this.routes.get(this.position++):null;
    }

    /**
     * Determines if there are items left in this result
     *
     * @return true if there are items left, false if not
     */
    @Override
    public boolean hasNext() {
        return position < routes.size();
    }

    /**
     * Add an item to this result
     *
     * @param item the item to add to the result
     */
    @Override
    public void addItem(Route item) {
        routes.add(item);
    }
}
