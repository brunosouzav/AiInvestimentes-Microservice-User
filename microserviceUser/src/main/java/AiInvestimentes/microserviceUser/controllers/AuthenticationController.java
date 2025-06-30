package AiInvestimentes.microserviceUser.controllers;

import AiInvestimentes.microserviceUser.dtos.LoginDTO;
import AiInvestimentes.microserviceUser.dtos.RegisterDTO;
import AiInvestimentes.microserviceUser.services.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDTO registerDTO) {
        String token = authenticationService.register(registerDTO);
        return ResponseEntity.status(201).body(token);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
        String token = authenticationService.login(loginDTO);
        return ResponseEntity.ok(token);
    }
}