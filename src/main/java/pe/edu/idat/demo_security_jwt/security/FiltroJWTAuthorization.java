package pe.edu.idat.demo_security_jwt.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class FiltroJWTAuthorization extends OncePerRequestFilter {

    private final String KEY = "@idat2025";
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

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
