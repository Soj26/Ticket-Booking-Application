package ca.sheridancollege.alagao.security;

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
    private DatabaseAccess da;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ca.sheridancollege.alagao.beans.User user = da.findUserAccount(email);
        if (user == null) {
            System.out.println("User not found: " + email);
            throw new UsernameNotFoundException("User with email " + email + " was not found in the database");
        }

        List<String> roleNameList = da.getRolesById(user.getUserID());
        List<GrantedAuthority> grantList = new ArrayList<>();

        if (roleNameList != null) {
            for (String role : roleNameList) {
                GrantedAuthority authority = new SimpleGrantedAuthority(role);
                grantList.add(authority);
            }
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getEncryptedPassword(),
                grantList
        );
    }
}
