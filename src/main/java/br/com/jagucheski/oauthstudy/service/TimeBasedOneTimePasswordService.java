package br.com.jagucheski.oauthstudy.service;

import br.com.jagucheski.oauthstudy.model.Usuario;
import com.atlassian.onetime.core.TOTPGenerator;
import com.atlassian.onetime.model.TOTPSecret;

import com.atlassian.onetime.service.RandomSecretProvider;
import org.springframework.stereotype.Service;

@Service
public class TimeBasedOneTimePasswordService {

    /**
     * Gera uma chave secreta aleatória em formato Base32 para uso em autenticação TOTP.
     *
     * <p>A secret gerada deve ser armazenada vinculada ao usuário e utilizada tanto
     * para gerar o QR Code quanto para validar os códigos TOTP subsequentes.
     *
     * @return String contendo a secret aleatória codificada em Base32
     *         (ex: {@code 4R7Q6OCPZ7JJAFMX2NNX55IX4WG225S5})
     */
    public String gerarSecret(){
        return new RandomSecretProvider().generateSecret().getBase32Encoded();
    }

    /**
     * Gera a URL no formato OTPAuth utilizada para configurar autenticadores TOTP,
     * como Google Authenticator ou Authy.
     *
     * <p>Formato gerado:
     * {@code otpauth://totp/<issuer>:<email>?secret=<secret>&issuer=<issuer>}
     *
     * @param email  e-mail do usuário, utilizado como identificador na conta do autenticador
     * @param secret chave secreta em Base32 gerada para o usuário
     * @return String no formato OTPAuth pronta para ser convertida em QR Code
     *
     * @example
     * gerarQrCode("user@email.com", "4R7Q6OCPZ7JJAFMX2NNX55IX4WG225S5")
     * // retorna: otpauth://totp/oauthstudy:user@email.com?secret=4R7Q6OCPZ7JJAFMX2NNX55IX4WG225S5&issuer=oauthstudy
     */
    public String gerarQrCode(String email, String secret){
        var issuer = "oauthstudy";
        return String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s", issuer, email, secret, issuer
        );
    }

    /**
     * Verifica se o código TOTP informado pelo usuário é válido no momento atual.
     *
     * <p>O código é gerado com base na secret do usuário e no timestamp atual (janela de 30 segundos).
     * A comparação é feita entre o código digitado e o código gerado localmente pelo servidor.
     *
     * @param codigo  código de 6 dígitos informado pelo usuário via autenticador (ex: Google Authenticator)
     * @param usuario entidade {@link Usuario} contendo a secret Base32 cadastrada para o usuário
     * @return {@code true} se o código informado corresponde ao código atual gerado pela secret,
     *         {@code false} caso contrário ou se o código estiver expirado
     */
    public Boolean verificarCodigo(String codigo, Usuario usuario) {
        var secretDecodificada = TOTPSecret.Companion.fromBase32EncodedString(usuario.getSecret());
        var codigoAplicacao = new TOTPGenerator().generateCurrent(secretDecodificada).getValue();
        return codigoAplicacao.equals(codigo);
    }
}
