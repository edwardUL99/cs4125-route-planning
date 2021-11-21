package ie.ul.routeplanning.controllers;

import ie.ul.routeplanning.services.SecurityService;
import ie.ul.routeplanning.services.UserService;
import ie.ul.routeplanning.users.User;
import ie.ul.routeplanning.users.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The controller for user authentication and registration etc.
 */
@Controller
public class UserController {
    /**
     * The user service for the controller
     */
    private final UserService userService;
    /**
     * The security service for the controller
     */
    private final SecurityService securityService;
    /**
     * The user validator for the controller
     */
    private final UserValidator userValidator;

    /**
     * Constructs a UserController with the autowired parameters
     * @param userService the service for working with users
     * @param securityService the service for querying authentication for logged in users
     * @param userValidator the validator for validating users
     */
    @Autowired
    public UserController(UserService userService, SecurityService securityService, UserValidator userValidator) {
        this.userService = userService;
        this.securityService = securityService;
        this.userValidator = userValidator;
    }

    /**
     * The mapping for registration
     *
     * @param model the model for registration
     * @return the path to navigate to
     */
    @RequestMapping("/registration")
    public String registration(Model model) {
        if (securityService.isAuthenticated()) {
            return "redirect:/welcome";
        }

        model.addAttribute("userForm", new User());

        return "registration";
    }

    @RequestMapping(value="/registration", method=RequestMethod.POST)
    public String registration(@ModelAttribute("userForm") User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        String rawPassword = user.getPassword();
        userService.save(user);
        securityService.autoLogin(user.getUsername(), rawPassword);

        return "redirect:/welcome";
    }

    /**
     * Handles the get login functionality. POST login is handled by Spring
     *
     * @param model  the model for the view
     * @param error  null if no error, message if there is
     * @param logout null if not to logout, other value if you want to logout
     * @return the page to go to
     */
    @RequestMapping("/login")
    public String login(Model model, String error, String logout) {
        if (securityService.isAuthenticated()) {
            return "redirect:/welcome";
        }

        if (error != null) {
            model.addAttribute("error", "Your username and password is invalid");
        }

        if (logout != null) {
            model.addAttribute("message", "You have been logged out");
        }

        return "login";
    }

    /**
     * Handles when a session is invalid or timed out
     * @param redirectAttributes the redirect attributes to forward to login's model
     * @return the redirect url
     */
    @GetMapping("/session-timeout")
    public String timeout(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("timeout",
                "The session has timed out due to inactivity. Please login again");
        return "redirect:/login";
    }

    @GetMapping({"/", "/welcome"})
    public String welcome(Model model) {
        return "welcome";
    }
}
