INSERT INTO consertos (id, data_entrada, data_saida, veiculo_marca, veiculo_modelo, veiculo_ano, mecanico_nome, mecanico_anos_experiencia)
VALUES
    (RANDOM_UUID(), '15/01/2024', '20/01/2024', 'Toyota', 'Corolla', '2020', 'João Silva', 10),
    (RANDOM_UUID(), '16/01/2024', NULL, 'Honda', 'Civic', '2019', 'Maria Santos', 8),
    (RANDOM_UUID(), '17/01/2024', '22/01/2024', 'Ford', 'Focus', '2021', 'Pedro Oliveira', 15);