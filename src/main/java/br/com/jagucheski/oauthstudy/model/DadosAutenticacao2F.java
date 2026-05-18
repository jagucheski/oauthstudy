package br.com.jagucheski.oauthstudy.model;

import jakarta.validation.constraints.NotBlank;

public record DadosAutenticacao2F(@NotBlank String email,
                                  @NotBlank String codigo) {
}