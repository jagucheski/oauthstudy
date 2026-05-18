INSERT INTO usuarios (id, nome, email, password, secret, autenticacao_2f)
SELECT 1, 'Usuario teste', 'usuario@gmail.com', '123456', null, false
    WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE email = 'usuario@gmail.com');

-- INSERT INTO usuarios (id, nome, email, password, secret, autenticacao_2f)
-- VALUES (1, 'Usuario teste', 'usuario@gmail.com'', '123456', null, false)