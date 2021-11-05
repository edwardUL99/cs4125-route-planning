package ie.ul.routeplanning.users;

import javax.persistence.*;
import java.util.Set;

/**
 * This class represents the role of a user
 */
@Entity
public class Role {
    /**
     * The ID for this role
     */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    /**
     * The name of the role
     */
    private String name;
    /**
     * The users in this role
     */
    @ManyToMany
    private Set<User> users;

    /**
     * Retrieve the ID of the role
     * @return the role's ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the role's ID
     * @param id the ID of the role
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Retrieve the name of the role
     * @return the role's name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the role's name
     * @param name the new name for the role
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the set of users in this role
     * @return set of users in the role
     */
    public Set<User> getUsers() {
        return users;
    }

    /**
     * Set the list of users in this role
     * @param users set of users in this role
     */
    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
