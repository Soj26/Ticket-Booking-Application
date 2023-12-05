package ca.sheridancollege.alagao.database;

import ca.sheridancollege.alagao.beans.User;
import ca.sheridancollege.alagao.beans.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        String query = "INSERT INTO sec_user (email, encryptedPassword, enabled, balance, name, purchaseCount) "
                + "VALUES (:email, :encryptedPassword, 1, 0.00, :name, 0)";

        parameters.addValue("email", email);
        parameters.addValue("encryptedPassword", passenc.encode(password));
        parameters.addValue("name", name); // Adding the name parameter

        jdbc.update(query, parameters);
    }


}
