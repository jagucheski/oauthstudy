package br.com.jagucheski.oauthstudy.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class LocalProperties {

    @Value("${oauth2.google.client-id}")
    private String oauthGoogleClientId;

    @Value("${oauth2.google.client-secret}")
    private String oauthGoogleClientSecret;

    @Value("${oauth2.google.redirect-uri}")
    private String oauthGoogleRedirectURI;

}