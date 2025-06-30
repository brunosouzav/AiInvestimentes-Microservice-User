package AiInvestimentes.microserviceUser.controllers;

import AiInvestimentes.microserviceUser.models.User;
import AiInvestimentes.microserviceUser.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping
    public ResponseEntity<List<User>> listAllUsers() {
        List<User> users = userService.listAllUsers();
        return ResponseEntity.ok(users);
    }
}