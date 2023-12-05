package ca.sheridancollege.alagao.security;

import ca.sheridancollege.alagao.beans.User;
import ca.sheridancollege.alagao.database.DatabaseAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private DatabaseAccess database;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = database.findUserAccount(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        List<String> roleNameList = database.getRolesById(user.getUserID());
        List<GrantedAuthority> grantList = new ArrayList<>();

        for (String role : roleNameList) {
            grantList.add(new SimpleGrantedAuthority(role));
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getEncryptedPassword(),
                grantList
        );
    }
}