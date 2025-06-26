package AiInvestimentes.microserviceUser.services;

import AiInvestimentes.microserviceUser.models.User;
import AiInvestimentes.microserviceUser.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> listAllUsers() {
        return userRepository.findAll();
    }
}