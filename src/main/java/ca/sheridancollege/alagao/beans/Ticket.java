package ca.sheridancollege.alagao.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    private Long ticketID;
    private String ticketTitle; // Title of the ticket
    private LocalDateTime timestamp; // Timestamp for the ticket creation or update
    private Long userID; // Reference to the User's ID
    private String flightDetails;
    private String seatNumber;
    private BigDecimal price;
    private int numberOfSeats;
    private boolean available;
}