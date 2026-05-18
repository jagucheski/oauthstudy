package br.com.jagucheski.oauthstudy.service;

import br.com.jagucheski.oauthstudy.model.Usuario;
import br.com.jagucheski.oauthstudy.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final TimeBasedOneTimePasswordService timeBasedOneTimePasswordService;

    public UsuarioService(UsuarioRepository usuarioRepository, TimeBasedOneTimePasswordService timeBasedOneTimePasswordService) {
        this.usuarioRepository = usuarioRepository;
        this.timeBasedOneTimePasswordService = timeBasedOneTimePasswordService;
    }

    public Usuario findByEmailIgnoreCase(@NotBlank String email) {
        return usuarioRepository.findByEmailIgnoreCase(email).orElseThrow(
                () -> new RuntimeException("Usuario não encontrado!")
        );
    }

    @Transactional
    public String gerarQrCode(String email) {
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email).orElseThrow(
                () -> new RuntimeException("Usuario não encontrado!")
        );
        var secret = timeBasedOneTimePasswordService.gerarSecret();
        usuario.setSecret(secret);
        usuarioRepository.save(usuario);
        return timeBasedOneTimePasswordService.gerarQrCode(usuario.getEmail(), secret);
    }
}
