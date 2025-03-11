package pe.edu.idat.demo_security_jwt.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FiltroJWTAuthorization extends OncePerRequestFilter {

    private final String KEY = "@idat2025";
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    }

    private void cargarAutorizaciones(Claims claims){
        List<String> autorizaciones = (List<String>) claims.get("Authorities");
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                claims.getSubject(),
                null,
                autorizaciones.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList()));
        SecurityContextHolder.getContext().setAuthentication(authToken);

    }
    private Claims validarToken(HttpServletRequest request){
        String token = request.getHeader("Authorization").replace("Bearer ","");
        return Jwts.parser().setSigningKey(KEY.getBytes()).parseClaimsJws(token).getBody();
    }

    private boolean validarUsoDeToken(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        if (token == null || token.startsWith("Bearer ")){
            return false;
        }
        return true;
    }

}
