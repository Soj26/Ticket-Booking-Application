package ca.sheridancollege.alagao.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.io.IOException;
import java.util.Set;

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
                .csrf(csrf -> csrf.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")))
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(customSuccessHandler())
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")) // to allow GET requests for logout
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/error/permission-denied")
                );


        return http.build();
    }
    @Bean
    public AuthenticationSuccessHandler customSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
                Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
                if (roles.contains("ROLE_ADMIN")) {
                    response.sendRedirect("/secured/admin/adminIndex");
                } else if (roles.contains("ROLE_USER")) {
                    response.sendRedirect("/secured/user/userIndex");
                } else {
                    response.sendRedirect("/");
                }
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
