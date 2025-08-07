package bank_project.security.token;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SessionToken {

    public String createToken() {
        return UUID.randomUUID().toString();
    }

}
