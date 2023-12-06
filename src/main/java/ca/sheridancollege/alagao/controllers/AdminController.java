package ca.sheridancollege.alagao.controllers;

import ca.sheridancollege.alagao.beans.Ticket;
import ca.sheridancollege.alagao.database.TicketDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/secured/admin")
public class AdminController {

    @Autowired
    private TicketDatabase ticketAccess;
    // Admin Dashboard
    @GetMapping("/adminIndex")
    public String adminDashboard(Model model) {
        return "/secured/admin/adminIndex";
    }

    // Mapping to show form to add a new ticket
    @GetMapping("/addTicket")
    public String addTicketForm(Model model) {
        model.addAttribute("ticket", new Ticket());
        return ":/secured/admin/adminIndex";
    }

    @PostMapping("/addTicket")
    public String addTicket(@ModelAttribute Ticket ticket) {
        ticketAccess.insertTicket(ticket);
        return "redirect:/secured/admin/adminIndex";
    }

    @GetMapping("/editTicket/{ticketId}")
    public String editTicketForm(@PathVariable Long ticketId, Model model) {
        Ticket ticket = ticketAccess.findTicketById(ticketId);
        model.addAttribute("ticket", ticket);
        return "secured/admin/adminIndex";
    }

    @PostMapping("/editTicket")
    public String editTicket(@ModelAttribute Ticket ticket) {
        ticketAccess.updateTicket(ticket);
        return "redirect:/secured/admin/adminIndex";
    }

    // Mapping to delete a ticket
    @GetMapping("/deleteTicket/{ticketId}")
    public String deleteTicket(@PathVariable Long ticketId) {
        ticketAccess.deleteTicket(ticketId);
        return "redirect:/secured/admin/adminIndex";
    }
}
