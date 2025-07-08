package AiInvestimentes.microserviceUser.services;

import AiInvestimentes.microserviceUser.dtos.LoginDTO;
import AiInvestimentes.microserviceUser.dtos.RegisterDTO;
import AiInvestimentes.microserviceUser.enuns.UserRole;
import AiInvestimentes.microserviceUser.models.User;
import AiInvestimentes.microserviceUser.repositories.UserRepository;
import AiInvestimentes.microserviceUser.security.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    public String register(RegisterDTO registerDTO) {

        if (userRepository.findByEmail(registerDTO.email()).isPresent()) {
            throw new IllegalArgumentException("Email já registrado");
        }

        User user = new User();
        user.setName(registerDTO.name());
        user.setEmail(registerDTO.email());
        user.setPassword(passwordEncoder.encode(registerDTO.password()));
        user.setRole(UserRole.USER);

        userRepository.save(user);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(registerDTO.email(), registerDTO.password())
        );
        return jwtService.generateToken(authentication);
    }

    public String login(LoginDTO loginDTO) {

        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.password())
            );
            return jwtService.generateToken(authentication);

        } catch (AuthenticationException e) {
            throw new IllegalArgumentException("Senha incorreta");
        }
    }

    public String loginOauth(OidcUser principal) {
        String email = principal.getEmail();
        String name = principal.getFullName();


        Optional<User> userOptional = userRepository.findByEmail(email);

        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
        } else {
            String randomPassword = generateRandomPassword(6);

            user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setPassword(passwordEncoder.encode(randomPassword));
            user.setRole(UserRole.USER);

            userRepository.save(user);
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                email, null, getAuthoritiesFromRole(user.getRole())
        );

        return jwtService.generateToken(authentication);
    }

    private String generateRandomPassword(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10); // 0-9
            sb.append(digit);
        }

        return sb.toString();
    }

    private Collection<? extends GrantedAuthority> getAuthoritiesFromRole(UserRole role) {
        if (role == UserRole.ADMIN) {
            return List.of(
                    new SimpleGrantedAuthority("admin"),
                    new SimpleGrantedAuthority("user")
            );
        } else {
            return List.of(new SimpleGrantedAuthority("user"));
        }
    }
}