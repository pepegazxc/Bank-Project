package bank_project.Service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AutoAuthService {

    private final AuthenticationManager authenticationManager;

    public AutoAuthService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public void autoAuth(String name, String password) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(name, password));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
