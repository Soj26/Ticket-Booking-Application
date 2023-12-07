package ca.sheridancollege.alagao.database;

import ca.sheridancollege.alagao.beans.User;
import ca.sheridancollege.alagao.beans.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class DatabaseAccess {
    @Autowired
    TicketDatabase ticketDatabase;
    @Autowired
    @Lazy
    private BCryptPasswordEncoder passenc;


    @Autowired
    private NamedParameterJdbcTemplate jdbc;


    public User findUserAccount(String email) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        String query = "SELECT * FROM sec_user WHERE email = :email";
        params.addValue("email", email);
        return jdbc.queryForObject(query, params, new BeanPropertyRowMapper<>(User.class));
    }
    public List<String> getRolesById(Long userID) {
        MapSqlParameterSource mapsqlp = new MapSqlParameterSource();

        String query = "SELECT sec_role.roleName "
                + "FROM user_role, sec_role "
                + "WHERE user_role.roleId = sec_role.roleId "
                + "AND userID = :userID";

        mapsqlp.addValue("userID", userID);
        return jdbc.queryForList(query, mapsqlp, String.class);
    }

    public void addUser(String email, String password, String name) {
        String encryptedPassword = passenc.encode(password);

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("email", email);
        parameters.addValue("encryptedPassword", encryptedPassword);
        parameters.addValue("name", name);

        String query = "INSERT INTO sec_user (email, encryptedPassword, enabled, balance, name, purchaseCount) "
                + "VALUES (:email, :encryptedPassword, 1, 0.00, :name, 0)";

        jdbc.update(query, parameters);
    }

    public void addRole(Long userId, Long roleId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userID", userId);
        params.addValue("roleId", roleId);

        String query = "INSERT INTO user_role (userID, roleId) VALUES (:userID, :roleId)";

        try {
            jdbc.update(query, params);
        } catch (DuplicateKeyException e) {
            System.out.println("Role already assigned to user: " + e.getMessage());
        } catch (Exception e) {

            System.out.println("Error in adding role to user: " + e.getMessage());
        }
    }
    public Long getRoleId(String roleName) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("roleName", roleName);

        String query = "SELECT roleId FROM sec_role WHERE roleName = :roleName";

        try {
            return jdbc.queryForObject(query, params, Long.class);
        } catch (Exception e) {
            System.out.println("Error retrieving role ID: " + e.getMessage());
            return null;
        }
    }

    public void createUser(User user) {
        String query = "INSERT INTO sec_user (email, encryptedPassword, enabled, name, balance, purchaseCount) " +
                "VALUES (:email, :encryptedPassword, :enabled, :name, :balance, :purchaseCount)";
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("email", user.getEmail())
                .addValue("encryptedPassword", user.getEncryptedPassword())
                .addValue("enabled", user.getEnabled())
                .addValue("name", user.getName())
                .addValue("balance", user.getBalance())
                .addValue("purchaseCount", user.getPurchaseCount());
        jdbc.update(query, parameters);
    }

    // Method to update a user
    public void updateUser(User user) {
        String query = "UPDATE sec_user SET email = :email, encryptedPassword = :encryptedPassword, " +
                "enabled = :enabled, name = :name, balance = :balance, purchaseCount = :purchaseCount " +
                "WHERE userID = :userID";
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("userID", user.getUserID())
                .addValue("email", user.getEmail())
                .addValue("encryptedPassword", user.getEncryptedPassword())
                .addValue("enabled", user.getEnabled())
                .addValue("name", user.getName())
                .addValue("balance", user.getBalance())
                .addValue("purchaseCount", user.getPurchaseCount());
        jdbc.update(query, parameters);
    }
    public void deleteUser(Long userID) {
        String query = "DELETE FROM sec_user WHERE userID = :userID";
        MapSqlParameterSource parameters = new MapSqlParameterSource().addValue("userID", userID);
        jdbc.update(query, parameters);
    }

    // Method to associate a role with a user
    public void addRoleToUser(Long userID, Long roleID) {
        String query = "INSERT INTO user_role (userID, roleId) VALUES (:userID, :roleID)";
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("userID", userID)
                .addValue("roleID", roleID);
        jdbc.update(query, parameters);
    }

    // Method to remove a role from a user
    public void removeRoleFromUser(Long userID, Long roleID) {
        String query = "DELETE FROM user_role WHERE userID = :userID AND roleId = :roleID";
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("userID", userID)
                .addValue("roleID", roleID);
        jdbc.update(query, parameters);
    }

    // Method to find a user by their ID
    public User findUserAccount(Long userID) {
        String query = "SELECT * FROM sec_user WHERE userID = :userID";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userID", userID);
        List<User> users = jdbc.query(query, params, new BeanPropertyRowMapper<>(User.class));
        return users.isEmpty() ? null : users.get(0);
    }

    // Method to update the user's balance
    public void updateUserBalance(String email, BigDecimal newBalance) {
        String query = "UPDATE sec_user SET balance = :balance WHERE email = :email";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", email)
                .addValue("balance", newBalance);
        jdbc.update(query, params);
    }


    public boolean purchaseTicket(Long userID, Long ticketID) {

        User user = findUserAccount(userID);
        Ticket ticket = ticketDatabase.findTicketById(ticketID);

        if (ticket != null && ticket.isAvailable() && user.getBalance().compareTo(ticket.getPrice()) >= 0) {
            // Subtract ticket price from user balance
            BigDecimal newBalance = user.getBalance().subtract(ticket.getPrice());
            updateUserBalance(user.getEmail(), newBalance);


            ticket.setNumberOfSeats(ticket.getNumberOfSeats() - 1);
            if (ticket.getNumberOfSeats() == 0) {
                ticket.setAvailable(false);
            }
            ticketDatabase.updateTicket(ticket);


            return true;
        }

        return false;
    }

    // Method to find a user by their email
    public User findUserAccountByEmail(String email) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        String query = "SELECT * FROM sec_user WHERE email = :email";
        params.addValue("email", email);
        List<User> users = jdbc.query(query, params, new BeanPropertyRowMapper<>(User.class));
        if(users.isEmpty()) {
            return null;
        } else {
            return users.get(0);
        }
    }
    public List<User> findAllUsers() {
        String query = "SELECT * FROM sec_user";
        List<User> users = jdbc.query(query, new BeanPropertyRowMapper<>(User.class));
        return users;
    }
}
