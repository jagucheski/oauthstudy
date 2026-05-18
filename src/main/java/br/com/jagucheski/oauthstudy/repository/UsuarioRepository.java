package br.com.jagucheski.oauthstudy.repository;

import br.com.jagucheski.oauthstudy.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmailIgnoreCase(String email);
}
