package application.httpFilter;

import application.authenticationToken.CustomAuthenticationToken;
import application.services.JwtConfiguration;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.logging.Logger;

@Component
public class AdminFilter extends OncePerRequestFilter {

    @Autowired
    private JwtConfiguration jwtConfiguration;

    @Autowired
    private Logger logger;

    @Autowired
    private UserDetailsManager userDetailsManager;


    @Autowired
    private HttpSessionSecurityContextRepository httpSessionSecurityContextRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getParameter("token");
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        if (token != null) {
            String username = jwtConfiguration.extractUsername(token);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                var user = userDetailsManager.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                httpSessionSecurityContextRepository.saveContext(SecurityContextHolder.getContext(), request, response);


            }
        }
        filterChain.doFilter(request, response);
    }
}

