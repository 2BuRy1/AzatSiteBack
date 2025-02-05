package application.httpFilter;

import application.authenticationToken.CustomAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.logging.Logger;

@Component
public class AdminFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsManager userDetailsManager;

    @Autowired
    private Logger logger;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.info("meow");
        if(request.getRequestURI().equals("/admin")){
            String login = request.getParameter("login");
            String password =request.getParameter("password");
            try {
                var user = userDetailsManager.loadUserByUsername(login);

            logger.info(login);


            if(user!=null && SecurityContextHolder.getContext().getAuthentication()== null && user.getPassword().equals(password)){
                CustomAuthenticationToken customAuthenticationToken = new CustomAuthenticationToken(user.getAuthorities(), user.getUsername());
                SecurityContextHolder.getContext().setAuthentication(customAuthenticationToken);
                logger.info("AUTHENTICATED!!!");
            }
            } catch (UsernameNotFoundException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("idi nahuy");
                return;
            }

        }

        filterChain.doFilter(request, response);
    }


}
