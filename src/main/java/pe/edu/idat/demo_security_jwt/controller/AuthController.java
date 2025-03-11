package pe.edu.idat.demo_security_jwt.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.idat.demo_security_jwt.dto.UsuarioSeguridadDto;
import pe.edu.idat.demo_security_jwt.model.Usuario;
import pe.edu.idat.demo_security_jwt.service.IUsuarioService;
import pe.edu.idat.demo_security_jwt.service.impl.DetalleUsuarioService;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {
    private final IUsuarioService usuarioService;
    private final DetalleUsuarioService detalleUsuarioService;
    private final AuthenticationManager authenticationManager;

    public AuthController(IUsuarioService usuarioService, DetalleUsuarioService detalleUsuarioService, AuthenticationManager authenticationManager) {
        this.usuarioService = usuarioService;
        this.detalleUsuarioService = detalleUsuarioService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/login")
    public ResponseEntity<UsuarioSeguridadDto> autenticarUsuario(
            @RequestParam String usuario,
            @RequestParam String password
    ) throws Exception {
        try{
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(usuario,
                                    password));
            if(authentication.isAuthenticated()){
                Usuario objUsuario = usuarioService
                        .obtenerUsuarioxNomusuario(usuario);
                String token = generarToken(objUsuario);
                UsuarioSeguridadDto usuarioSeguridadDto
                        = new UsuarioSeguridadDto();
                usuarioSeguridadDto.setIdusuario(objUsuario.getIdusuario());
                usuarioSeguridadDto.setNomusuario(objUsuario.getNomusuario());
                usuarioSeguridadDto.setToken(token);
                return new ResponseEntity<>(usuarioSeguridadDto,
                        HttpStatus.OK);
            }else{
                UsuarioSeguridadDto usuarioSeguridadDto =
                        new UsuarioSeguridadDto();
                usuarioSeguridadDto
                        .setMensajeError("Error en la autenticación");
                return new ResponseEntity<>(usuarioSeguridadDto,
                        HttpStatus.UNAUTHORIZED);

            }
        }catch (Exception ex){
            throw  new Exception(ex.getMessage());
        }
    }

    private String generarToken(Usuario objUsuario){
        String clave = "@idat2025";
        List<GrantedAuthority> grantedAuthorities =
                detalleUsuarioService.getAuthorities(
                        objUsuario.getRoles());

        return Jwts.builder()
                .setId(objUsuario.getIdusuario().toString())
                .setSubject(objUsuario.getNomusuario())
                .claim("Authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList())
                )
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ 300000))
                .signWith(SignatureAlgorithm.HS512, clave.getBytes())
                .compact();


    }


}