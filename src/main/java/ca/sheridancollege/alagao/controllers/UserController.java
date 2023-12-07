package ca.sheridancollege.alagao.controllers;

import ca.sheridancollege.alagao.beans.Ticket;
import ca.sheridancollege.alagao.beans.User;
import ca.sheridancollege.alagao.database.DatabaseAccess;
import ca.sheridancollege.alagao.database.TicketDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/secured/user")
public class UserController {

    @Autowired
    private DatabaseAccess database;

    @Autowired
    private TicketDatabase ticketDatabase;

    @GetMapping("/userIndex")
    public String userDashboard(Model model, Principal principal) {
        String email = principal.getName();
        User user = database.findUserAccount(email);
        List<Ticket> availableTickets = ticketDatabase.findAvailableTickets();

        model.addAttribute("user", user);
        model.addAttribute("availableTickets", availableTickets);
        return "secured/user/userIndex";
    }

    @PostMapping("/buyTicket/{ticketId}")
    public String buyTicket(@PathVariable Long ticketId,
                            @RequestParam("quantity") int quantity,
                            Principal principal,
                            RedirectAttributes redirectAttributes) {
        User user = database.findUserAccountByEmail(principal.getName());
        Ticket ticket = ticketDatabase.findTicketById(ticketId);

        if (ticket != null && ticket.isAvailable()) {
            BigDecimal totalCost = ticket.getPrice().multiply(new BigDecimal(quantity));
            if (user.getBalance().compareTo(totalCost) >= 0) {
                BigDecimal newBalance = user.getBalance().subtract(totalCost);
                database.updateUserBalance(user.getEmail(), newBalance);

                int newNumberOfSeats = ticket.getNumberOfSeats() - quantity;
                ticket.setNumberOfSeats(newNumberOfSeats);
                ticket.setAvailable(newNumberOfSeats > 0);
                ticketDatabase.updateTicket(ticket);

                redirectAttributes.addFlashAttribute("successMessage", "Transaction successful!");
                redirectAttributes.addFlashAttribute("totalCost", totalCost);
                redirectAttributes.addFlashAttribute("newBalance", newBalance);
                return "redirect:/secured/user/transactionSummary";
            }
        }

        redirectAttributes.addFlashAttribute("error", "Cannot purchase ticket. Not enough seats or insufficient balance.");
        return "redirect:/secured/user/userIndex";
    }

    @GetMapping("/transactionSummary")
    public String transactionSummary(Model model) {
        return "secured/user/transactionSummary";
    }
}
