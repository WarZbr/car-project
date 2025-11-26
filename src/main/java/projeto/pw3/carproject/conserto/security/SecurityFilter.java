package projeto.pw3.carproject.conserto.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import projeto.pw3.carproject.conserto.repository.UsuarioRepository;
import projeto.pw3.carproject.conserto.service.PW3TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final PW3TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Pular autenticação para endpoints públicos
        String path = request.getRequestURI();
        System.out.println("🔍 SecurityFilter - Path: " + path + " | Method: " + request.getMethod());

        if (path.startsWith("/api/auth/") || path.startsWith("/h2-console/")) {
            System.out.println("✅ Endpoint público detectado, pulando autenticação");
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("🔒 Endpoint protegido, verificando token...");
        String token = recuperarToken(request);

        if (token != null) {
            String login = tokenService.validarToken(token);
            if (!login.isEmpty()) {
                UserDetails usuario = usuarioRepository.findByLogin(login);

                if (usuario != null) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    System.out.println("✅ Usuário autenticado: " + login);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        return authHeader.replace("Bearer ", "");
    }
}