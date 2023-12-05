package ca.sheridancollege.alagao.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    private Long ticketID;
    private Long userID; // Reference to the User's ID
    private String flightDetails;
    private String seatNumber;
    private BigDecimal price;
    private int numberOfSeats;
    private boolean available;
}
