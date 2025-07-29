package alessiopanconi.u5w3d1.security;

import alessiopanconi.u5w3d1.entities.Dipendente;
import alessiopanconi.u5w3d1.exceptions.UnauthorizedException;
import alessiopanconi.u5w3d1.services.DipendentiService;
import alessiopanconi.u5w3d1.tools.JWTTools;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    private JWTTools jwtTools;

    @Autowired
    private DipendentiService dipendentiService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new UnauthorizedException("Inserire il token nell'Authorization Header nel formato corretto!");

        String accessToken = authHeader.replace("Bearer ", "");
        jwtTools.verifyToken(accessToken);

        String dipendenteId = jwtTools.extractIdFromToken(accessToken);
        Dipendente dipendenteAttivo = this.dipendentiService.findDipendenteById(Long.parseLong(accessToken));
        Authentication authentication = new UsernamePasswordAuthenticationToken(dipendenteAttivo, null, dipendenteAttivo.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return new AntPathMatcher().match("/autenticazione/**", request.getServletPath());
    }
}
