package br.com.jagucheski.oauthstudy.model;

public record UsuarioAutenticacao2F(String email,
                                    String codigo,
                                    Boolean valido) {
}