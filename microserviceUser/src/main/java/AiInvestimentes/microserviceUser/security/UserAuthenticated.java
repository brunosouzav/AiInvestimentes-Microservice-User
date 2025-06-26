package AiInvestimentes.microserviceUser.security;


import AiInvestimentes.microserviceUser.models.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class UserAuthenticated implements UserDetails {

    private final User user;

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = user.getRole().getRole();
        List<GrantedAuthority> authorities = new ArrayList<>();

        switch (role) {
            case "admin":
                authorities.add(new SimpleGrantedAuthority("admin"));
                authorities.add(new SimpleGrantedAuthority("premium"));
                authorities.add(new SimpleGrantedAuthority("user"));
                break;
            case "user":
                authorities.add(new SimpleGrantedAuthority("user"));
                break;
            default:
                throw new IllegalArgumentException("Role inválida: " + role);
        }

        return authorities;
    }


}