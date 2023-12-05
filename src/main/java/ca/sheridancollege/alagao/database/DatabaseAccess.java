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

import java.util.List;

@Repository
public class DatabaseAccess {
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
            // Handle the case where the role assignment already exists
            // This is important to avoid trying to insert a duplicate role assignment
            System.out.println("Role already assigned to user: " + e.getMessage());
            // Depending on your application's requirements, you might want to throw an exception or just log the error
        } catch (Exception e) {
            // Handle other exceptions such as connectivity issues with the database
            System.out.println("Error in adding role to user: " + e.getMessage());
            // Again, depending on your requirements, either handle the exception or rethrow it
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
            return null; // Or handle the exception as appropriate for your application
        }
    }

}
