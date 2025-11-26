package projeto.pw3.carproject.conserto.controller;

import jakarta.validation.Valid;
import projeto.pw3.carproject.conserto.dto.LoginDTO;
import projeto.pw3.carproject.conserto.dto.RegisterDTO;
import projeto.pw3.carproject.conserto.dto.TokenDTO;
import projeto.pw3.carproject.conserto.model.Usuario;
import projeto.pw3.carproject.conserto.model.UsuarioRole;
import projeto.pw3.carproject.conserto.repository.UsuarioRepository;
import projeto.pw3.carproject.conserto.service.PW3TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final PW3TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        System.out.println("🔑 AuthController - Tentativa de login: " + loginDTO.getLogin());

        try {
            UsernamePasswordAuthenticationToken usernamePassword =
                    new UsernamePasswordAuthenticationToken(loginDTO.getLogin(), loginDTO.getSenha());

            System.out.println("🔐 Autenticando usuário...");
            Authentication auth = authenticationManager.authenticate(usernamePassword);
            System.out.println("✅ Autenticação bem-sucedida!");

            String token = tokenService.gerarToken((Usuario) auth.getPrincipal());
            System.out.println("🎫 Token gerado: " + token.substring(0, 20) + "...");

            return ResponseEntity.ok(new TokenDTO(token));
        } catch (Exception e) {
            System.out.println("❌ Erro na autenticação: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterDTO registerDTO) {
        if (usuarioRepository.findByLogin(registerDTO.getLogin()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        String senhaEncriptada = passwordEncoder.encode(registerDTO.getSenha());

        UsuarioRole role;
        try {
            role = UsuarioRole.valueOf(registerDTO.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Usuario novoUsuario = new Usuario(
                registerDTO.getLogin(),
                senhaEncriptada,
                role
        );

        usuarioRepository.save(novoUsuario);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}