INSERT INTO usuarios (id, nome, email, password, secret, autenticacao_2f)
SELECT 1, 'Maicon Aessio Jagucheski', 'maiconjagucheski@gmail.com', '123456', null, false
    WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE email = 'maiconjagucheski@gmail.com');

-- INSERT INTO usuarios (id, nome, email, password, secret, autenticacao_2f)
-- VALUES (1, 'Maicon Aessio Jagucheski', 'maiconjagucheski@gmail.com', '123456', null, false)