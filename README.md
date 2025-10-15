# Sistema de Gerenciamento de Consertos de Veículos

Sistema desenvolvido em Spring Boot para gerenciar consertos de veículos em uma oficina mecânica, com persistência de dados em banco H2, validações de entrada e exclusão lógica de registros.

## Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3.5.6**
- **Spring Data JPA** - Persistência de dados
- **H2 Database** - Banco de dados em memória
- **Lombok** - Redução de código boilerplate
- **Flyway** - Controle de versão do banco de dados
- **Bean Validation** - Validação de dados
- **Maven** - Gerenciamento de dependências

## Estrutura do Projeto

\`\`\`
src/main/java/projeto/pw3/carproject/
├── model/
│   ├── Conserto.java          # Entidade principal
│   ├── Veiculo.java           # Classe embeddable
│   └── Mecanico.java          # Classe embeddable
├── dto/
│   ├── ConsertoDTO.java       # DTO para criação
│   ├── ConsertoResumoDTO.java # DTO para listagem resumida
│   └── ConsertoAtualizacaoDTO.java # DTO para atualização parcial
├── repository/
│   └── ConsertoRepository.java # Interface JPA com queries customizadas
├── service/
│   └── ConsertoService.java   # Lógica de negócio
└── controller/
└── ConsertoController.java # Endpoints REST

src/main/resources/
└── db/migration/
├── V1__Create_consertos_table.sql
├── V2__Insert_sample_data.sql
├── V3__Add_veiculo_cor.sql
└── V4__Add_ativo_field.sql
\`\`\`

## Funcionalidades Implementadas

### 1. Modelo de Dados
- **Veículo** (Embeddable): marca, modelo, ano, cor
- **Mecânico** (Embeddable): nome, anos de experiência
- **Conserto** (Entidade): datas de entrada/saída, veículo, mecânico, campo ativo

### 2. Validações
- Datas no formato `dd/mm/aaaa`
- Campos obrigatórios: nome do mecânico, marca, modelo e ano do veículo
- Ano do veículo no formato `aaaa`

### 3. Endpoints REST

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/consertos` | Criar novo conserto |
| GET | `/api/consertos` | Listar todos (com paginação) |
| GET | `/api/consertos/resumo` | Listar resumo (apenas ativos) |
| GET | `/api/consertos/{id}` | Buscar por ID |
| PATCH | `/api/consertos/{id}` | Atualizar parcialmente |
| DELETE | `/api/consertos/{id}` | Exclusão lógica |

### 4. Exclusão Lógica
- Campo `ativo` controla visibilidade dos registros
- DELETE marca `ativo = false` sem remover do banco
- Listagem resumida exibe apenas registros ativos

## Como Executar

### 1. Pré-requisitos
- Java 21 ou superior
- Maven 3.6+
- IDE (IntelliJ IDEA, Eclipse, VS Code)

### 2. Clonar o repositório
\`\`\`bash
git clone <url-do-repositorio>
cd car-project
\`\`\`

### 3. Executar a aplicação
\`\`\`bash
mvn spring-boot:run
\`\`\`

Ou pela IDE: Execute a classe `ConsertoCarrosApplication.java`

### 4. Acessar o Console H2
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

## Casos de Teste para Avaliação

### Teste 1: Criar Conserto com Dados Válidos

**Requisição:**
\`\`\`bash
curl -X POST http://localhost:8080/api/consertos \
-H "Content-Type: application/json" \
-d '{
"dataEntrada": "15/10/2024",
"dataSaida": "20/10/2024",
"veiculoMarca": "Volkswagen",
"veiculoModelo": "Golf",
"veiculoAno": "2023",
"veiculoCor": "Prata",
"mecanicoNome": "Carlos Alberto",
"mecanicoAnosExperiencia": 10
}'
\`\`\`

**Resultado Esperado:** Status 201 CREATED, retorna o conserto criado com ID e `ativo: true`

---

### Teste 2: Validação de Data Inválida

**Requisição:**
\`\`\`bash
curl -X POST http://localhost:8080/api/consertos \
-H "Content-Type: application/json" \
-d '{
"dataEntrada": "2024-10-15",
"dataSaida": "20/10/2024",
"veiculoMarca": "Fiat",
"veiculoModelo": "Uno",
"veiculoAno": "2021",
"mecanicoNome": "João Silva",
"mecanicoAnosExperiencia": 5
}'
\`\`\`

**Resultado Esperado:** Status 400 BAD REQUEST com mensagem de erro indicando formato inválido de data

---

### Teste 3: Validação de Campo Obrigatório

**Requisição:**
\`\`\`bash
curl -X POST http://localhost:8080/api/consertos \
-H "Content-Type: application/json" \
-d '{
"dataEntrada": "15/10/2024",
"dataSaida": "20/10/2024",
"veiculoModelo": "Civic",
"veiculoAno": "2022",
"mecanicoNome": "Maria Santos",
"mecanicoAnosExperiencia": 8
}'
\`\`\`

**Resultado Esperado:** Status 400 BAD REQUEST indicando que a marca do veículo é obrigatória

---

### Teste 4: Listar Resumo de Consertos Ativos

**Requisição:**
\`\`\`bash
curl http://localhost:8080/api/consertos/resumo
\`\`\`

**Resultado Esperado:** Status 200 OK, lista JSON com ID, datas, nome do mecânico, marca e modelo (apenas consertos ativos)

**Exemplo de Resposta:**
\`\`\`json
[
{
"id": 1,
"dataEntrada": "15/01/2024",
"dataSaida": "20/01/2024",
"mecanicoNome": "João Silva",
"veiculoMarca": "Toyota",
"veiculoModelo": "Corolla"
}
]
\`\`\`

---

### Teste 5: Buscar Conserto por ID

**Requisição:**
\`\`\`bash
curl http://localhost:8080/api/consertos/1
\`\`\`

**Resultado Esperado:** Status 200 OK, retorna todos os dados do conserto incluindo veículo e mecânico completos

---

### Teste 6: Buscar Conserto Inexistente

**Requisição:**
\`\`\`bash
curl http://localhost:8080/api/consertos/999
\`\`\`

**Resultado Esperado:** Status 404 NOT FOUND

---

### Teste 7: Atualizar Parcialmente um Conserto

**Requisição:**
\`\`\`bash
curl -X PATCH http://localhost:8080/api/consertos/1 \
-H "Content-Type: application/json" \
-d '{
"dataSaida": "25/10/2024",
"mecanicoNome": "Roberto Alves",
"mecanicoAnosExperiencia": 15
}'
\`\`\`

**Resultado Esperado:** Status 200 OK, retorna o conserto com os campos atualizados

---

### Teste 8: Exclusão Lógica de Conserto

**Requisição:**
\`\`\`bash
curl -X DELETE http://localhost:8080/api/consertos/1
\`\`\`

**Resultado Esperado:** Status 204 NO CONTENT (sem corpo na resposta)

**Verificação:**
1. Execute novamente o Teste 4 (listar resumo) - o conserto ID 1 não deve aparecer
2. No console H2, execute: `SELECT * FROM consertos WHERE id = 1;`
3. O registro deve existir com `ativo = false`

---

### Teste 9: Listar com Paginação

**Requisição:**
\`\`\`bash
curl "http://localhost:8080/api/consertos?page=0&size=3"
\`\`\`

**Resultado Esperado:** Status 200 OK, objeto JSON com paginação contendo:
- `content`: array de consertos
- `totalElements`: total de registros
- `totalPages`: total de páginas
- `size`: tamanho da página
- `number`: número da página atual

---

### Teste 10: Campo Cor Opcional

**Requisição:**
\`\`\`bash
curl -X POST http://localhost:8080/api/consertos \
-H "Content-Type: application/json" \
-d '{
"dataEntrada": "16/10/2024",
"dataSaida": "21/10/2024",
"veiculoMarca": "Honda",
"veiculoModelo": "Civic",
"veiculoAno": "2024",
"mecanicoNome": "Ana Paula",
"mecanicoAnosExperiencia": 6
}'
\`\`\`

**Resultado Esperado:** Status 201 CREATED, conserto criado sem o campo cor (campo opcional)

---

## Verificação no Banco de Dados

### Consultar todos os consertos
\`\`\`sql
SELECT * FROM consertos;
\`\`\`

### Consultar apenas consertos ativos
\`\`\`sql
SELECT * FROM consertos WHERE ativo = true;
\`\`\`

### Verificar estrutura da tabela
\`\`\`sql
SHOW COLUMNS FROM consertos;
\`\`\`

### Verificar histórico de migrations
\`\`\`sql
SELECT * FROM flyway_schema_history;
\`\`\`

## Observações Importantes

1. **Exclusão Lógica**: Os registros deletados permanecem no banco com `ativo = false`, mas não aparecem na listagem resumida
2. **Validações**: Todas as validações são feitas automaticamente pelo Bean Validation antes de persistir no banco
3. **Migrations**: O Flyway gerencia automaticamente as versões do banco de dados
4. **ResponseEntity**: Todos os endpoints retornam status HTTP adequados (200, 201, 204, 404, 400)

## Autores

- **Bruno Gabriel Alves Silva**
- **Rhuan Andrey de Andrade Boni**

Desenvolvido como projeto acadêmico para a disciplina de Programação Web 3.
