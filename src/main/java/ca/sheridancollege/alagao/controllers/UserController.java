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
@RequestMapping("secured/user/")
public class UserController {

    @Autowired
    private DatabaseAccess database;

    @GetMapping("/userIndex")
    public String userDashboard(Model model) {
        //User user = getCurrentAuthenticatedUser();
        // model.addAttribute("user", user);
        return "secured/user/userIndex";
    }

}

