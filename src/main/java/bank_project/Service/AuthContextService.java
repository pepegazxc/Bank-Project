package bank_project.Service;

import bank_project.Entity.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthContextService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public AuthContextService(AuthenticationManager authenticationManager, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }


    public void autoAuth(String userName, String password, HttpServletRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userName, password));
        SecurityContextHolder.getContext().setAuthentication(auth);

        HttpSession session = request.getSession(true);
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext()
        );
        log.info("User {} authenticated", userName);
    }

    public void updateUserAuthentication(UserEntity savedUser) {
        UserDetails updateUser = userDetailsService.loadUserByUsername(savedUser.getUsername());

        Authentication auth = new UsernamePasswordAuthenticationToken(
                updateUser,
                savedUser.getPassword(),
                updateUser.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(auth);
        log.info("User {} authentication has updated", savedUser.getUsername());
    }
}
