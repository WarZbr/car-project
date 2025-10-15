CREATE TABLE consertos (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           data_entrada VARCHAR(255) NOT NULL,
                           data_saida VARCHAR(255),
                           veiculo_marca VARCHAR(255) NOT NULL,
                           veiculo_modelo VARCHAR(255) NOT NULL,
                           veiculo_ano VARCHAR(4) NOT NULL,
                           mecanico_nome VARCHAR(255) NOT NULL,
                           mecanico_anos_experiencia INTEGER NOT NULL
);
