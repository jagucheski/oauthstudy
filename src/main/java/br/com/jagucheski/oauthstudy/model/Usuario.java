package br.com.jagucheski.oauthstudy.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String password;
    private String secret;

    @Column(name = "autenticacao_2f")
    private Boolean autenticacao2f;

}
