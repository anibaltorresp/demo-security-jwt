package pe.edu.idat.demo_security_jwt.service.impl;

import org.springframework.stereotype.Service;
import pe.edu.idat.demo_security_jwt.model.Usuario;
import pe.edu.idat.demo_security_jwt.repository.UsuarioRepository;
import pe.edu.idat.demo_security_jwt.service.IUsuarioService;
@Service
public class UsuarioService implements IUsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario obtenerUsuarioxNomusuario(String nomusuario) {
        return usuarioRepository.findByNomusuario(nomusuario);
    }
}
