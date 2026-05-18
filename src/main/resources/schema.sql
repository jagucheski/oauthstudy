CREATE TABLE usuarios
(
    id              INT          NOT NULL PRIMARY KEY,
    nome            VARCHAR(255) NOT NULL,
    email           VARCHAR(255) NOT NULL,
    password        VARCHAR(255) NOT NULL,
    secret          VARCHAR(255),
    autenticacao_2f BOOLEAN      NOT NULL
);
