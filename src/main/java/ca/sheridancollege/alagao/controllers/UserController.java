    package ca.sheridancollege.alagao.controllers;

    import ca.sheridancollege.alagao.beans.Ticket;
    import ca.sheridancollege.alagao.beans.User;
    import ca.sheridancollege.alagao.database.DatabaseAccess;
    import ca.sheridancollege.alagao.database.TicketDatabase;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.core.Authentication;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.*;

    import java.math.BigDecimal;
    import java.security.Principal;
    import java.util.List;
    @Controller
    @RequestMapping("secured/user/")
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
        public String buyTicket(@PathVariable Long ticketId, Principal principal, Model model) {
            User user = database.findUserAccountByEmail(principal.getName());
            Ticket ticket = ticketDatabase.findTicketById(ticketId);

            if (ticket != null && ticket.isAvailable() && user.getBalance().compareTo(ticket.getPrice()) >= 0) {

                BigDecimal newBalance = user.getBalance().subtract(ticket.getPrice());
                database.updateUserBalance(user.getEmail(), newBalance);


                int newNumberOfSeats = ticket.getNumberOfSeats() - 1;
                ticket.setNumberOfSeats(newNumberOfSeats);
                if (newNumberOfSeats == 0) {
                    ticket.setAvailable(false);
                }
                ticketDatabase.updateTicket(ticket);

                model.addAttribute("success", "Ticket purchased successfully!");
                return "secured/user/userIndex";
            } else {
                model.addAttribute("error", "Cannot purchase ticket.");
                return "secured/user/userIndex";
            }
        }
    }

