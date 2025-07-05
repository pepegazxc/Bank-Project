package bank_project.Service;

import bank_project.Entity.UserEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthContextService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public AuthContextService(AuthenticationManager authenticationManager, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }


    public void autoAuth(String userName, String password) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userName, password));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    public void updateUserAuthentication(UserEntity savedUser) {
        UserDetails updateUser = userDetailsService.loadUserByUsername(savedUser.getUsername());

        Authentication auth = new UsernamePasswordAuthenticationToken(
                updateUser,
                savedUser.getPassword(),
                updateUser.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
