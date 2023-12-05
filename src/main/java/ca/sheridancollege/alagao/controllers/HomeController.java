package ca.sheridancollege.alagao.controllers;

import ca.sheridancollege.alagao.beans.User;
import ca.sheridancollege.alagao.database.DatabaseAccess;
import ca.sheridancollege.alagao.database.TicketDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @Autowired
    private DatabaseAccess database;
    @Autowired
    private TicketDatabase tDa;

    @GetMapping("/register")
    public String getRegistrationForm(Model model) {
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@RequestParam String email,
                                  @RequestParam String password,
                                  @RequestParam String confirmPassword,
                                  @RequestParam String name,
                                  @RequestParam(required = false) String role,
                                  Model model) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "register";
        }

        try {
            database.addUser(email, password, name);
            Long userId = database.findUserAccount(email).getUserID();

            // Set the role to "User" for regular users
            if (!model.getAttribute("role").equals("ADMIN")) {
                role = "USER";
            }

            // Assuming getRoleId method is implemented in DatabaseAccess
            Long roleId = database.getRoleId("ROLE_" + role.toUpperCase());
            database.addRole(userId, roleId);

            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register";
        }
    }
}

