package br.com.jagucheski.oauthstudy.service;

import br.com.jagucheski.oauthstudy.config.LocalProperties;
import br.com.jagucheski.oauthstudy.model.UsuarioLoginOauth;
import com.auth0.jwt.JWT;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

import static java.util.Objects.isNull;

@Service
public class LoginGoogleService {

    private static final String GOOGLE_AUTH_URL = "https://accounts.google.com/o/oauth2/v2/auth";
    private static final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String GOOGLE_AUTH_SCOPE = "https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email";
    private static final String GOOGLE_RESPONSE_TYPE = "code";
    private static final String GOOGLE_GRANT_TYPE = "authorization_code";

    private final LocalProperties localProperties;
    private final RestClient restClient;

    public LoginGoogleService(LocalProperties localProperties, RestClient restClient) {
        this.localProperties = localProperties;
        this.restClient = restClient;
    }

    /**
     * Gera a URL de autorização do Google OAuth2 com os scopes de perfil e e-mail.
     *
     * <p>A URL gerada redireciona o usuário para a tela de login do Google,
     * solicitando acesso aos dados de perfil ({@code userinfo.profile})
     * e e-mail ({@code userinfo.email}).
     *
     * @return String contendo a URL completa de autorização do Google OAuth2
     */
    public String gerarUrl() {
        return UriComponentsBuilder
                .fromUriString(GOOGLE_AUTH_URL)
                .queryParam("client_id", localProperties.getOauthGoogleClientId())
                .queryParam("redirect_uri", localProperties.getOauthGoogleRedirectURI())
                .queryParam("scope", GOOGLE_AUTH_SCOPE)
                .queryParam("response_type", GOOGLE_RESPONSE_TYPE)
                .toUriString();
    }

    /**
     * Troca o código de autorização do Google por um token de acesso (id_token JWT).
     *
     * <p>Realiza uma requisição POST para o endpoint de token do Google,
     * enviando o código recebido no callback junto com as credenciais da aplicação.
     * Retorna o {@code id_token} no formato JWT contendo os dados do usuário.
     *
     * @param code código de autorização recebido no callback do Google
     * @return String contendo o {@code id_token} JWT retornado pelo Google
     * @throws RuntimeException se a resposta do Google for nula
     */
    private String obterToken(String code) {
        var resposta = restClient.post()
                .uri(GOOGLE_TOKEN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(getBodyTokenGoogle(code))
                .retrieve()
                .body(Map.class);
        if (resposta == null || resposta.get("id_token") == null) {
            throw new RuntimeException("Erro - resposta oauth2.googleapis.com/token is null");
        }
        return resposta.get("id_token").toString();
    }

    /**
     * Obtém os dados do usuário autenticado a partir do código de autorização do Google.
     *
     * <p>Troca o código pelo token JWT, decodifica o token e extrai as claims
     * com as informações do usuário (nome, e-mail e foto).
     *
     * @param code código de autorização recebido no callback do Google
     * @return {@link UsuarioLoginOauth} contendo primeiro nome, nome completo,
     * e-mail e URL da foto de perfil do usuário
     * @throws RuntimeException se o token retornado for nulo
     */
    public UsuarioLoginOauth obterDadosUsuarioOauth(String code) {
        var token = obterToken(code);
        if (isNull(token)) {
            throw new RuntimeException("Erro ao obter token");
        }
        var decodedJWT = JWT.decode(token);
        return new UsuarioLoginOauth(
                decodedJWT.getClaim("given_name").asString(),
                decodedJWT.getClaim("name").asString(),
                decodedJWT.getClaim("email").asString(),
                decodedJWT.getClaim("picture").asString());
    }

    private Map<String, String> getBodyTokenGoogle(String code) {
        return Map.of("code", code,
                "client_id", localProperties.getOauthGoogleClientId(),
                "client_secret", localProperties.getOauthGoogleClientSecret(),
                "redirect_uri", localProperties.getOauthGoogleRedirectURI(),
                "grant_type", GOOGLE_GRANT_TYPE);
    }
}
