package ie.ul.routeplanning.controllers;

import ie.ul.routeplanning.services.SecurityService;
import ie.ul.routeplanning.services.UserService;
import ie.ul.routeplanning.users.User;
import ie.ul.routeplanning.users.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * The controller for user authentication and registration etc.
 */
@Controller
public class UserController {
    /**
     * The user service for the controller
     */
    @Autowired
    private UserService userService;
    /**
     * The security service for the controller
     */
    @Autowired
    private SecurityService securityService;
    /**
     * The user validator for the controller
     */
    @Autowired
    private UserValidator userValidator;

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

        userService.save(user);
        securityService.autoLogin(user.getUsername(), user.getPassword());

        return "redirect:/welcome";
    }

    /**
     * Handles the get login functionality. POST login is handled by Spring
     *
     * @param model  the model for the view
     * @param error  null if no error, message if there is
     * @param logout null if not to logout, other value if you want to logour
     * @return the page to go to
     */
    @RequestMapping("/login")
    public String login(Model model, String error, String logout) {
        if (securityService.isAuthenticated()) {
            return "redirect:/welcome";
        }

        if (error != null) {
            model.addAttribute("error", "Your username and password is invalid"); // todo maybe change these to boolean
        }

        if (logout != null) {
            model.addAttribute("message", "You have been logged out");
        }

        return "login";
    }

    @GetMapping({"/", "/welcome"})
    public String welcome(Model model) {
        return "welcome";
    }
}
