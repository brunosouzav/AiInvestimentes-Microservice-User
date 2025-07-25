package AiInvestimentes.microserviceUser.auth;

import AiInvestimentes.microserviceUser.dtos.LoginDTO;
import AiInvestimentes.microserviceUser.dtos.RegisterDTO;
import AiInvestimentes.microserviceUser.enuns.UserRole;
import AiInvestimentes.microserviceUser.models.User;
import AiInvestimentes.microserviceUser.repositories.UserRepository;
import AiInvestimentes.microserviceUser.security.JwtService;
import AiInvestimentes.microserviceUser.services.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.mockito.Mockito.mock;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    // Simulo a ijeção de dependencia do repositorio
    @Mock
    UserRepository userRepository;

    @Mock
    JwtService jwtService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    AuthenticationManager authenticationManager;

    // Ijeto o mock anterior na classe importada
    @InjectMocks
    AuthenticationService authenticationService;

    User user;

    // Preparar Ambiente de teste
    @BeforeEach
    public void setUp() {
        user = new User( 1L, "Bruno Teste", "brunoteste@gmail.com", "123456", UserRole.USER );
    }

    @Test
    void verificarProcessoDeLogin() {
        LoginDTO loginDTO = new LoginDTO("brunoteste@gmail.com", "123456");

        // Simula a busca da interface e retorna o optional
        when(userRepository.findByEmail(loginDTO.email()))
                .thenReturn(Optional.of(new User()));

        //Cria um obj de autenticação que o spring simularia quando o usuario for autenticado
        Authentication authenticationMock = mock(Authentication.class);

        //Quando a autenticação for chamada com qualquer email devolva o mock de cima
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authenticationMock);

        //Simula o jwt
        when(jwtService.generateToken(authenticationMock)).thenReturn("fake-jwt-token");

        // Chamo o metodo para gerar o token
        String token = authenticationService.login(loginDTO);

        assertEquals("fake-jwt-token", token);
    }

    @Test
    void deveRegistrarUsuarioComSucessoERetornarToken() {
        RegisterDTO registerDTO = new RegisterDTO("Bruno", "bruno@email.com", "123456");

        // 1. E-mail ainda não existe
        when(userRepository.findByEmail(registerDTO.email()))
                .thenReturn(Optional.empty());

        // 2. Simula o encode da senha
        when(passwordEncoder.encode(registerDTO.password()))
                .thenReturn("senha-criptografada");

        // 3. Simula salvar o usuário (pode ser void ou retornar o user)
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0)); // retorna o mesmo usuário

        // 4. Simula autenticação
        Authentication authenticationMock = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authenticationMock);

        // 5. Simula geração do token
        when(jwtService.generateToken(authenticationMock)).thenReturn("fake-jwt-token");

        // Act
        String token = authenticationService.register(registerDTO);

        // Assert
        assertEquals("fake-jwt-token", token);
    }
}