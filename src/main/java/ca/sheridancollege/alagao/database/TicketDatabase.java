package ca.sheridancollege.alagao.database;

import ca.sheridancollege.alagao.beans.Ticket;
import ca.sheridancollege.alagao.beans.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TicketDatabase {
    @Autowired
    private NamedParameterJdbcTemplate jdbc;

    public List<Ticket> findAvailableTickets() {
        String query = "SELECT * FROM tickets WHERE available = TRUE";
        return jdbc.query(query, new BeanPropertyRowMapper<>(Ticket.class));
    }
    // Method to insert a new ticket
    public void insertTicket(Ticket ticket) {
        String sql = "INSERT INTO tickets (userID, flightDetails, seatNumber, price, numberOfSeats, available) " +
                "VALUES (:userID, :flightDetails, :seatNumber, :price, :numberOfSeats, :available)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userID", ticket.getUserID())
                .addValue("flightDetails", ticket.getFlightDetails())
                .addValue("seatNumber", ticket.getSeatNumber())
                .addValue("price", ticket.getPrice())
                .addValue("numberOfSeats", ticket.getNumberOfSeats())
                .addValue("available", ticket.isAvailable());

        jdbc.update(sql, params);
    }

    // Method to update an existing ticket
    public void updateTicket(Ticket ticket) {
        String sql = "UPDATE tickets SET " +
                "userID = :userID, " +
                "flightDetails = :flightDetails, " +
                "seatNumber = :seatNumber, " +
                "price = :price, " +
                "numberOfSeats = :numberOfSeats, " +
                "available = :available " +
                "WHERE ticketID = :ticketID";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("ticketID", ticket.getTicketID())
                .addValue("userID", ticket.getUserID())
                .addValue("flightDetails", ticket.getFlightDetails())
                .addValue("seatNumber", ticket.getSeatNumber())
                .addValue("price", ticket.getPrice())
                .addValue("numberOfSeats", ticket.getNumberOfSeats())
                .addValue("available", ticket.isAvailable());

        jdbc.update(sql, params);
    }

    // Method to delete a ticket
    public void deleteTicket(Long ticketID) {
        String sql = "DELETE FROM tickets WHERE ticketID = :ticketID";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("ticketID", ticketID);

        jdbc.update(sql, params);
    }

    // Method to find a ticket by its ID
    public Ticket findTicketById(Long ticketID) {
        String sql = "SELECT * FROM tickets WHERE ticketID = :ticketID";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("ticketID", ticketID);
        List<Ticket> tickets = jdbc.query(sql, params, new BeanPropertyRowMapper<>(Ticket.class));
        return tickets.isEmpty() ? null : tickets.get(0);
    }

    // Method to get all tickets
    public List<Ticket> findAllTickets() {
        String sql = "SELECT * FROM tickets";
        return jdbc.query(sql, new BeanPropertyRowMapper<>(Ticket.class));
    }
}
