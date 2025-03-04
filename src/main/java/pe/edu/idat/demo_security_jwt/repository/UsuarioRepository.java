package pe.edu.idat.demo_security_jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.idat.demo_security_jwt.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    //select * from usuario where nomusuario = :nomusuario;
    Usuario findByNomusuario(String nomusuario);
}
