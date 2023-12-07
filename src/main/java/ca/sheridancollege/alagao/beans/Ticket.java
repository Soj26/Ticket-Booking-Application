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
    private String ticketTitle;
    private LocalDateTime timestamp;
    private Long userID;
    private String flightDetails;
    private String seatNumber;
    private BigDecimal price;
    private int numberOfSeats;
    private boolean available;
}