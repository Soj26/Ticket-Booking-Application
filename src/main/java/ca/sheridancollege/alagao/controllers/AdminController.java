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

    // Mapping to show form to add a new ticket
    @GetMapping("/addTicket")
    public String addTicketForm(Model model) {
        model.addAttribute("ticket", new Ticket());
        return "secured/admin/addTicket";
    }

    @PostMapping("/addTicket")
    public String addTicket(@ModelAttribute Ticket ticket) {
        ticketAccess.insertTicket(ticket);
        return "redirect:/secured/admin/ticketList";
    }

    @GetMapping("/editTicket/{ticketId}")
    public String editTicketForm(@PathVariable Long ticketId, Model model) {
        Ticket ticket = ticketAccess.findTicketById(ticketId);
        model.addAttribute("ticket", ticket);
        return "secured/admin/editTicket";
    }

    @PostMapping("/editTicket")
    public String editTicket(@ModelAttribute Ticket ticket) {
        ticketAccess.updateTicket(ticket);
        return "redirect:/secured/admin/ticketList";
    }

    // Mapping to delete a ticket
    @GetMapping("/deleteTicket/{ticketId}")
    public String deleteTicket(@PathVariable Long ticketId) {
        ticketAccess.deleteTicket(ticketId);
        return "redirect:/secured/admin/ticketList";
    }
}
