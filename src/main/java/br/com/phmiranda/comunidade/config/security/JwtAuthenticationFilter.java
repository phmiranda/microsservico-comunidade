package br.com.phmiranda.comunidade.config.security;

import br.com.phmiranda.comunidade.domain.entity.Usuario;
import br.com.phmiranda.comunidade.repository.UsuarioRepository;
import br.com.phmiranda.comunidade.service.AuthTokenService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthTokenService authTokenService;
    private final UsuarioRepository usuarioRepository;

    public JwtAuthenticationFilter(AuthTokenService authTokenService, UsuarioRepository usuarioRepository) {
        this.authTokenService = authTokenService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        String token = recuperarToken(request);

        if (token != null && authTokenService.isTokenValido(token)) {
            autenticarUsuario(token, request);
        }

        filterChain.doFilter(request, response);
    }

    private void autenticarUsuario(String token, HttpServletRequest request) {
        Long usuarioId = authTokenService.getUsuarioId(token);
        Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);

        if (usuario.isPresent() && usuario.get().isEnabled()) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                usuario.get(),
                null,
                usuario.get().getAuthorities()
            );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    private String recuperarToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return null;
        }

        return authorization.substring(7);
    }
}
