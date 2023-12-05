package ca.sheridancollege.alagao.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvcMatcher = new MvcRequestMatcher.Builder(introspector);

        http
                .authorizeRequests(auth -> auth
                        .requestMatchers(mvcMatcher.pattern("/secured/admin/**")).hasAuthority("ROLE_ADMIN")
                        .requestMatchers(mvcMatcher.pattern("/secured/user/**")).hasAuthority("ROLE_USER")
                        .requestMatchers(mvcMatcher.pattern("/register")).permitAll()
                        .requestMatchers(mvcMatcher.pattern("/login")).permitAll()
                        .requestMatchers(mvcMatcher.pattern("/")).permitAll()
                        .requestMatchers(mvcMatcher.pattern("/js/**")).permitAll()
                        .requestMatchers(mvcMatcher.pattern("/css/**")).permitAll()
                        .requestMatchers(mvcMatcher.pattern("/imgs/**")).permitAll()
                        .requestMatchers(mvcMatcher.pattern("/**")).authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .logout(logout -> logout.permitAll());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
