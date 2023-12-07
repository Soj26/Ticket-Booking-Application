package ca.sheridancollege.alagao.controllers;

import ca.sheridancollege.alagao.beans.Ticket;
import ca.sheridancollege.alagao.database.DatabaseAccess;
import ca.sheridancollege.alagao.database.TicketDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TicketController {

    @Autowired
    private TicketDatabase tDa;

    @GetMapping("/")
    public String showIndex(Model model) {
        List<Ticket> availableTickets = tDa.findAvailableTickets();
        model.addAttribute("availableTickets", availableTickets);
        return "index";
    }

}
