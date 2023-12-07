package ca.sheridancollege.alagao.controllers;

import ca.sheridancollege.alagao.beans.User;
import ca.sheridancollege.alagao.beans.Ticket;
import ca.sheridancollege.alagao.database.DatabaseAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private DatabaseAccess database;

    private static final String USER_REST_URL = "http://localhost:8080/api/v1/users";
    private static final String TICKET_REST_URL = "http://localhost:8080/api/v1/tickets";
    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/")
    public String index(Model model) {
        ResponseEntity<User[]> usersResponse = restTemplate.getForEntity(USER_REST_URL, User[].class);
        ResponseEntity<Ticket[]> ticketsResponse = restTemplate.getForEntity(TICKET_REST_URL, Ticket[].class);

        model.addAttribute("userList", usersResponse.getBody());
        model.addAttribute("ticketList", ticketsResponse.getBody());

        return "index";
    }

    // ... Your existing methods like register, login, etc.

    @GetMapping("/getUser/{id}")
    public String getUser(@PathVariable Long id, Model model) {
        ResponseEntity<User> userResponse = restTemplate.getForEntity(USER_REST_URL + "/" + id, User.class);
        model.addAttribute("user", userResponse.getBody());
        return "userDetail"; // Make sure you have a Thymeleaf template for this
    }

    @GetMapping("/getTicket/{id}")
    public String getTicket(@PathVariable Long id, Model model) {
        ResponseEntity<Ticket> ticketResponse = restTemplate.getForEntity(TICKET_REST_URL + "/" + id, Ticket.class);
        model.addAttribute("ticket", ticketResponse.getBody());
        return "ticketDetail"; // Make sure you have a Thymeleaf template for this
    }

    // ... Additional methods as needed
}
