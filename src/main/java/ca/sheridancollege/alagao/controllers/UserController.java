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
public class UserController {

    @Autowired
    private DatabaseAccess database;

    // Mapping for admin dashboard
    @GetMapping("/secured/admin/adminIndex")
    public String adminDashboard(Model model) {

        return "secured/admin/adminIndex";
    }

    // Mapping for user dashboard
    @GetMapping("/secured/user/userIndex")
    public String userDashboard(Model model) {
        //User user = getCurrentAuthenticatedUser();
        // model.addAttribute("user", user);
        return "secured/user/userIndex";
    }

}

