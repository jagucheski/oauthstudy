package br.com.jagucheski.oauthstudy.controller;

import br.com.jagucheski.oauthstudy.model.DadosAutenticacao2F;
import br.com.jagucheski.oauthstudy.model.UsuarioAutenticacao2F;
import br.com.jagucheski.oauthstudy.service.TimeBasedOneTimePasswordService;
import br.com.jagucheski.oauthstudy.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

    private final TimeBasedOneTimePasswordService totpService;
    private final UsuarioService usuarioService;

    public AutenticacaoController(TimeBasedOneTimePasswordService totpService, UsuarioService usuarioService) {
        this.totpService = totpService;
        this.usuarioService = usuarioService;
    }

    /**
     * Configura a autenticação de dois fatores (2FA) para o usuário informado,
     * gerando e retornando a URL OTPAuth para ser convertida em QR Code.
     *
     * <p>A URL retornada deve ser usada pelo frontend para exibir o QR Code,
     * que o usuário escaneará com um autenticador como Google Authenticator ou Authy.
     *
     * @param email e-mail do usuário para o qual o 2FA será configurado,
     *              informado como variável de path
     * @return {@link ResponseEntity} contendo a URL no formato OTPAuth
     *         (ex: {@code otpauth://totp/oauthstudy:user@email.com?secret=XXX&issuer=oauthstudy})
     */
    @PatchMapping("/configurar-a2f/{email}")
    public ResponseEntity<String> gerarQrCode(@PathVariable String email){
        var url = usuarioService.gerarQrCode(email);
        return ResponseEntity.ok(url);
    }

    /**
     * Verifica o código TOTP informado pelo usuário na etapa de segundo fator de autenticação (2FA).
     *
     * <p>Busca o usuário pelo e-mail, valida o código TOTP informado contra a secret
     * cadastrada e retorna o resultado da verificação.
     *
     * @param dadosA2F objeto contendo o e-mail do usuário e o código TOTP de 6 dígitos
     *                 informado pelo autenticador. Validado via {@code @Valid}
     * @return {@link ResponseEntity} contendo {@link UsuarioAutenticacao2F} com o e-mail,
     *         código informado e um booleano indicando se o código é válido ({@code true})
     *         ou inválido ({@code false})
     */
    @PostMapping("/verificar-a2f")
    public ResponseEntity<UsuarioAutenticacao2F> verificarSegundoFator(@Valid @RequestBody DadosAutenticacao2F dadosA2F){
        var usuario = usuarioService.findByEmailIgnoreCase(dadosA2F.email());
        var codigoValido = totpService.verificarCodigo(dadosA2F.codigo(), usuario);
        return ResponseEntity.ok(new UsuarioAutenticacao2F(dadosA2F.email(), dadosA2F.codigo(), codigoValido));
    }

}
