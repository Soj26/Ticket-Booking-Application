    package ca.sheridancollege.alagao.controllers;

    import ca.sheridancollege.alagao.beans.Ticket;
    import ca.sheridancollege.alagao.beans.User;
    import ca.sheridancollege.alagao.database.DatabaseAccess;
    import ca.sheridancollege.alagao.database.TicketDatabase;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.servlet.mvc.support.RedirectAttributes;

    import java.time.LocalDateTime;
    import java.util.List;

    @Controller
    @RequestMapping("/secured/admin")
    public class AdminController {

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private DatabaseAccess databaseAccess;

        @Autowired
        private TicketDatabase ticketDatabase;

        // Display the admin dashboard with lists of users and tickets
        @GetMapping("/adminIndex")
        public String adminDashboard(Model model) {
            List<User> users = databaseAccess.findAllUsers();
            List<Ticket> tickets = ticketDatabase.findAllTickets();
            model.addAttribute("users", users);
            model.addAttribute("tickets", tickets);
            model.addAttribute("newUser", new User());
            model.addAttribute("newTicket", new Ticket());
            return "secured/admin/adminIndex";

        }
        @GetMapping("/addTicket")
        public String showAddTicketForm(Model model) {
            model.addAttribute("ticket", new Ticket());
            return "secured/admin/addTicket";
        }
        // Create a new ticket
        @PostMapping("/addTicket")
        public String addTicket(@ModelAttribute("newTicket") Ticket ticket, Authentication authentication, RedirectAttributes redirectAttributes) {
            if (authentication != null) {
                String username = authentication.getName(); // Get the username (email)
                User user = databaseAccess.findUserAccount(username); // Find the user in the database
                if (user != null) {
                    ticket.setUserID(user.getUserID()); // Set the user ID from the database
                } else {
                    // Handle the case where the user is not found
                    redirectAttributes.addFlashAttribute("errorMessage", "User not found.");
                    return "redirect:/secured/admin/addTicket";
                }
            } else {
                // Handle the case where there is no authentication object
                redirectAttributes.addFlashAttribute("errorMessage", "No authenticated user.");
                return "redirect:/secured/admin/addTicket";
            }

            ticket.setTimestamp(LocalDateTime.now()); // Set the current time as the timestamp
            ticketDatabase.insertTicket(ticket);
            redirectAttributes.addFlashAttribute("successMessage", "Ticket created successfully!");
            return "redirect:/secured/admin/adminIndex";
        }


        @GetMapping("/addUser")
        public String showAddUserForm(Model model) {
            model.addAttribute("user", new User());
            return "secured/admin/addUser";
        }
        // Create a new user
        @PostMapping("/addUser")
        public String addUser(@ModelAttribute("newUser") User user, RedirectAttributes redirectAttributes) {
            String encodedPassword = passwordEncoder.encode(user.getEncryptedPassword());
            user.setEncryptedPassword(encodedPassword);
            // Additional user setup
            databaseAccess.createUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "User created successfully!");
            return "redirect:/secured/admin/adminIndex";
        }



        // Update an existing user
        @PostMapping("/editUser/{userId}")
        public String editUser(@PathVariable Long userId, @ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
            user.setUserID(userId);
            databaseAccess.updateUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "User updated successfully!");
            return "redirect:/secured/admin/adminIndex";
        }



        // Update an existing ticket
        @PostMapping("/editTicket/{ticketId}")
        public String editTicket(@PathVariable Long ticketId, @ModelAttribute("ticket") Ticket ticket, RedirectAttributes redirectAttributes) {
            ticket.setTicketID(ticketId);
            ticketDatabase.updateTicket(ticket);
            redirectAttributes.addFlashAttribute("successMessage", "Ticket updated successfully!");
            return "redirect:/secured/admin/adminIndex";
        }

        // Delete a ticket
        @GetMapping("/deleteTicket/{ticketID}")
        public String deleteTicket(@PathVariable Long ticketID, RedirectAttributes redirectAttributes) {
            ticketDatabase.deleteTicket(ticketID);
            redirectAttributes.addFlashAttribute("successMessage", "Ticket deleted successfully!");
            return "redirect:/secured/admin/adminIndex";
        }

        // Delete a user
        @GetMapping("/deleteUser/{userID}")
        public String deleteUser(@PathVariable Long userID, RedirectAttributes redirectAttributes) {
            databaseAccess.deleteUser(userID);
            redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully!");
            return "redirect:/secured/admin/adminIndex";
        }

        // Show form for editing an existing ticket
        @GetMapping("/editTicketForm/{ticketID}")
        public String showEditTicketForm(@PathVariable Long ticketID, Model model) {
            Ticket ticket = ticketDatabase.findTicketById(ticketID);
            model.addAttribute("ticket", ticket);
            return "redirect:/secured/admin/adminIndex";
        }

        // Show form for editing an existing user
        @GetMapping("/editUserForm/{userID}")
        public String showEditUserForm(@PathVariable Long userID, Model model) {
            User user = databaseAccess.findUserAccount(userID);
            model.addAttribute("user", user);
            return "redirect:/secured/admin/adminIndex";
        }


    }
