package ca.sheridancollege.alagao.controllers;

import ca.sheridancollege.alagao.beans.Ticket;
import ca.sheridancollege.alagao.beans.User;
import ca.sheridancollege.alagao.database.DatabaseAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private DatabaseAccess database;

    @GetMapping("/register")
    public String getRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register"; // name of the registration template
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        database.addUser(user.getEmail(), user.getEncryptedPassword(), user.getName());
        return "redirect:/user/login"; // redirect to login page after registration
    }

    // Add other endpoints as needed, such as updating user info, listing users, etc.
}
