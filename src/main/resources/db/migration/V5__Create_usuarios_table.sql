-- Migration para criar tabela de usuários com UUID
CREATE TABLE usuarios (
     id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
     login VARCHAR(255) NOT NULL UNIQUE,
     senha VARCHAR(255) NOT NULL,
     "role" VARCHAR(50) NOT NULL
);

-- Inserir usuários de teste (senha: "senha123")
-- Hash BCrypt gerado para "senha123"
INSERT INTO usuarios (id, login, senha, "role") VALUES
 (RANDOM_UUID(), 'admin', '$2a$10$xqk.T3BPLVEy9g5jU/8UVe8F1YWdKxZB5YvPJ5bqR3Vz8jNhVQW5S', 'ADMIN'),
 (RANDOM_UUID(), 'user', '$2a$10$xqk.T3BPLVEy9g5jU/8UVe8F1YWdKxZB5YvPJ5bqR3Vz8jNhVQW5S', 'USER');