-- Migration para adicionar campo 'ativo' para exclusão lógica
ALTER TABLE consertos ADD COLUMN ativo BOOLEAN DEFAULT TRUE;

-- Atualizar registros existentes para ativo = true
UPDATE consertos SET ativo = TRUE WHERE ativo IS NULL;
