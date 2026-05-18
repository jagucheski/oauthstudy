package br.com.jagucheski.oauthstudy.controller;

import br.com.jagucheski.oauthstudy.model.UsuarioLoginOauth;
import br.com.jagucheski.oauthstudy.service.LoginGoogleService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;

@RestController
@RequestMapping("/login/google")
public class GoogleController {

    private final LoginGoogleService loginGoogleService;

    public GoogleController(LoginGoogleService loginGoogleService) {
        this.loginGoogleService = loginGoogleService;
    }

    /**
     * Redireciona o usuário para a tela de login do Google OAuth2.
     *
     * <p>Gera a URL de autorização do Google com os scopes configurados
     * e retorna um HTTP 302 (Found) com o header {@code Location} apontando para ela.
     *
     * @return {@link ResponseEntity} vazio com status 302 e header Location
     * contendo a URL de autenticação do Google
     */
    @GetMapping
    public ResponseEntity<Void> redirecionarGoogleOauth2() {
        var url = loginGoogleService.gerarUrl();
        var headers = new HttpHeaders();
        headers.setLocation(URI.create(url));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    /**
     * Callback chamado pelo Google após o usuário autorizar o acesso.
     *
     * <p>Recebe o código de autorização ({@code code}) retornado pelo Google,
     * troca pelo token de acesso e busca os dados do usuário autenticado.
     *
     * @param code código de autorização gerado pelo Google, recebido como
     *             query parameter na URL de redirecionamento
     * @return {@link ResponseEntity} contendo {@link UsuarioLoginOauth} com os
     * dados do usuário (nome, email, picture, etc.) com status 200
     */
    @GetMapping("/autorizado")
    public ResponseEntity<?> autenticarUsuarioOAuth(@RequestParam String code) {
        try {
            var usuarioLoginOauth = loginGoogleService.obterDadosUsuarioOauth(code);
            return ResponseEntity.ok(usuarioLoginOauth);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Falha na autenticação: " + e.getMessage());
        }
    }
}