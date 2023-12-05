package ca.sheridancollege.alagao.database;

import ca.sheridancollege.alagao.beans.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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



}
