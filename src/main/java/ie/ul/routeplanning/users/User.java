package ie.ul.routeplanning.users;

import javax.persistence.*;
import java.util.Set;

/**
 * This class represents a user in the system
 */
@Entity
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames={"username", "email"})
})
public class User {
    /**
     * The ID of this user
     */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    /**
     * The username for the user
     */
    @Column(name="username")
    private String username;
    /**
     * The user's password
     */
    private String password;
    /**
     * The user's password confirmation
     */
    @Transient
    private String passwordConfirm;
    /**
     * The user's full name
     */
    private String name;
    /**
     * The user's email address
     */
    @Column(name="email")
    private String email;
    /**
     * The set of roles this user is in
     */
    @ManyToMany(fetch=FetchType.EAGER)
    private Set<Role> roles;

    /**
     * Retrieve the user's ID
     * @return the id of the user
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the user's ID
     * @param id the id of the user
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Retrieve the password for the user
     * @return user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the password for this user
     * @param password the new password for the user
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Retrieve the username for the user
     * @return user's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Change the user's username
     * @param username the new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Retrieve the user's full name
     * @return user's full name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of this user
     * @param name the user's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieve the user's e-mail address
     * @return user's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the email of this user
     * @param email the user's email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get the password confirmation for the user
     * @return the password confirmation
     */
    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    /**
     * Set the password confirmation for the user
     * @param passwordConfirm password confirmation
     */
    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    /**
     * Retrieve the roles this user is in
     * @return the roles the user is a member of
     */
    public Set<Role> getRoles() {
        return roles;
    }

    /**
     * Set the roles this user is in
     * @param roles the new set of roles
     */
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
