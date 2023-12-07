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

    // Admin Dashboard
    @GetMapping("/adminIndex")
    public String adminDashboard(Model model) {
        List<User> users = databaseAccess.findAllUsers();
        List<Ticket> tickets = ticketDatabase.findAllTickets();
        model.addAttribute("users", users);
        model.addAttribute("tickets", tickets);
        return "secured/admin/adminIndex";
    }

    // Add Ticket
    @GetMapping("/addTicket")
    public String showAddTicketForm(Model model) {
        model.addAttribute("ticket", new Ticket());
        return "secured/admin/addTicket";
    }

    @PostMapping("/addTicket")
    public String addTicket(@ModelAttribute("newTicket") Ticket ticket, Authentication authentication, RedirectAttributes redirectAttributes) {
        if (authentication != null) {
            String username = authentication.getName();
            User user = databaseAccess.findUserAccount(username);
            ticket.setUserID(user != null ? user.getUserID() : null);
        }
        ticket.setTimestamp(LocalDateTime.now());
        ticketDatabase.insertTicket(ticket);
        redirectAttributes.addFlashAttribute("successMessage", "Ticket created successfully!");
        return "redirect:/secured/admin/adminIndex";
    }

    // Add User
    @GetMapping("/addUser")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new User());
        return "secured/admin/addUser";
    }

    @PostMapping("/addUser")
    public String addUser(@ModelAttribute("newUser") User user, RedirectAttributes redirectAttributes) {
        user.setEncryptedPassword(passwordEncoder.encode(user.getEncryptedPassword()));
        databaseAccess.createUser(user);
        redirectAttributes.addFlashAttribute("successMessage", "User created successfully!");
        return "redirect:/secured/admin/adminIndex";
    }

    // Edit Ticket
    @GetMapping("/editTicketForm/{ticketID}")
    public String showEditTicketForm(@PathVariable Long ticketID, Model model) {
        Ticket ticket = ticketDatabase.findTicketById(ticketID);
        if (ticket != null) {
            model.addAttribute("ticket", ticket);
            return "secured/admin/editTicket";
        }
        return "redirect:/secured/admin/adminIndex";
    }

    @PostMapping("/editTicket")
    public String editTicket(@ModelAttribute("ticket") Ticket ticket, Authentication authentication, RedirectAttributes redirectAttributes) {
        // Check if the ticket has a userID set, if not, set it based on the authenticated user
        if (ticket.getUserID() == null && authentication != null) {
            String username = authentication.getName(); // Get the username (email)
            User user = databaseAccess.findUserAccount(username); // Find the user in the database
            if (user != null) {
                ticket.setUserID(user.getUserID()); // Set the user ID from the database
            } else {
                // Handle the case where the user is not found
                redirectAttributes.addFlashAttribute("errorMessage", "User not found.");
                return "redirect:/secured/admin/editTicket";
            }
        } else if (ticket.getUserID() == null) {
            // Handle the case where there is no authenticated user
            redirectAttributes.addFlashAttribute("errorMessage", "No authenticated user.");
            return "redirect:/secured/admin/editTicket";
        }

        // Check and set the timestamp if it's null
        if (ticket.getTimestamp() == null) {
            ticket.setTimestamp(LocalDateTime.now());
        }

        // Update the ticket in the database
        ticketDatabase.updateTicket(ticket);
        redirectAttributes.addFlashAttribute("successMessage", "Ticket updated successfully!");
        return "redirect:/secured/admin/adminIndex";
    }


    // Edit User
    @GetMapping("/editUserForm/{userID}")
    public String showEditUserForm(@PathVariable Long userID, Model model) {
        User user = databaseAccess.findUserAccount(userID);
        if (user != null) {
            model.addAttribute("user", user);
            return "secured/admin/editUser"; // This part is already correct
        }
        return "redirect:/secured/admin/adminIndex";
    }

    @PostMapping("/editUser/{userId}")
    public String editUser(@PathVariable Long userId, @ModelAttribute("user") User formUser, RedirectAttributes redirectAttributes) {
        // Fetch the existing user from the database
        User existingUser = databaseAccess.findUserAccount(userId);

        if (existingUser != null) {
            // Update the existing user with the new details
            existingUser.setName(formUser.getName());
            existingUser.setEmail(formUser.getEmail());

            // Only update the password if a new one is provided
            if (formUser.getEncryptedPassword() != null && !formUser.getEncryptedPassword().isEmpty()) {
                existingUser.setEncryptedPassword(passwordEncoder.encode(formUser.getEncryptedPassword()));
            }

            // Update other fields as necessary
            // existingUser.setOtherFields(formUser.getOtherFields());

            // Save the updated user back to the database
            databaseAccess.updateUser(existingUser);
            redirectAttributes.addFlashAttribute("successMessage", "User updated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found.");
        }

        return "redirect:/secured/admin/adminIndex";
    }


    // Delete Ticket
    @GetMapping("/deleteTicket/{ticketID}")
    public String deleteTicket(@PathVariable Long ticketID, RedirectAttributes redirectAttributes) {
        ticketDatabase.deleteTicket(ticketID);
        redirectAttributes.addFlashAttribute("successMessage", "Ticket deleted successfully!");
        return "redirect:/secured/admin/adminIndex";
    }

    // Delete User
    @GetMapping("/deleteUser/{userID}")
    public String deleteUser(@PathVariable Long userID, RedirectAttributes redirectAttributes) {
        databaseAccess.deleteUser(userID);
        redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully!");
        return "redirect:/secured/admin/adminIndex";
    }
}
