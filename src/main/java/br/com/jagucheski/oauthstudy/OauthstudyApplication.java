package br.com.jagucheski.oauthstudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "br.com.jagucheski.oauthstudy.config",
        "br.com.jagucheski.oauthstudy.controller",
        "br.com.jagucheski.oauthstudy.model",
        "br.com.jagucheski.oauthstudy.service",
        "br.com.jagucheski.oauthstudy.repository",
})
public class OauthstudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(OauthstudyApplication.class, args);
    }

}
