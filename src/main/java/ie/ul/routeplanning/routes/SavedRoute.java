package ie.ul.routeplanning.routes;

import ie.ul.routeplanning.users.User;

import javax.persistence.*;

/**
 * A SavedRoute represents a route that can be saved to a user's account
 */
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public class SavedRoute extends Route {
    /**
     * The ID of this saved route
     */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    /**
     * The user that saved the route
     */
    @OneToOne(cascade=CascadeType.MERGE)
    private User user;
    /**
     * The route saved by this route instance
     */
    @OneToOne(cascade=CascadeType.MERGE)
    private Route savedRoute;

    /**
     * Create the SavedRoute instance
     * @param id the ID of the the saved route
     * @param user the user that owns this route
     * @param route the route that is being saved
     */
    public SavedRoute(Long id, User user, Route route) {
        this.id = id;
        this.user = user;

        setSavedRoute(route);

        this.saved = true;
    }

    /**
     * Creates a default instantiation of the SavedRoute
     */
    public SavedRoute() {
        this(null, null, null);
    }

    /**
     * Retrieve the id of the saved route
     * @return the saved route ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the id for this saved route
     * @param id the id of the route to save
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Retrieve the user that owns the saved route
     * @return the owner user account
     */
    public User getUser() {
        return user;
    }

    /**
     * Set the user that owns the saved route
     * @param user the owner user account
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Retrieve the saved route wrapped by this instance
     * @return the saved route wrapped by the instance
     */
    public Route getSavedRoute() {
        return savedRoute;
    }

    /**
     * Change the saved route instance. It must be a route that is saved in the database, so if route.getId() returns null,
     * an error is thrown
     * @param route the route to save.
     */
    public void setSavedRoute(Route route) {
        if (route != null && route.getId() == null)
            throw new IllegalStateException("The provided route has no ID, so it is assumed not to be saved. This cannot happen");

        this.savedRoute = route;
    }
}
