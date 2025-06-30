package AiInvestimentes.microserviceUser.models;

import AiInvestimentes.microserviceUser.enuns.UserRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb-user")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

}