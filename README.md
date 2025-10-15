# 🚗 Sistema de Gerenciamento de Consertos de Veículos

> Sistema REST API desenvolvido em Spring Boot para gerenciar consertos de veículos em oficinas mecânicas, com persistência em banco H2, validações robustas e exclusão lógica de registros.

## 👥 Autores

**Bruno Gabriel Alves Silva** • **Rhuan Andrey de Andrade Boni**

*Projeto acadêmico - Disciplina de Programação Web 3*

---

## 🛠️ Stack Tecnológico

```
Java 21                    Linguagem base
Spring Boot 3.5.6          Framework principal
Spring Data JPA            Camada de persistência
H2 Database                Banco de dados em memória
Lombok                     Redução de boilerplate
Flyway                     Versionamento de schema
Bean Validation            Validação de entrada
Maven                      Gerenciador de dependências
```

---

## 📁 Estrutura do Projeto

```
src/main/java/projeto/pw3/carproject/
│
├── 📦 model/
│   ├── Conserto.java          # Entidade principal
│   ├── Veiculo.java           # Embeddable
│   └── Mecanico.java          # Embeddable
│
├── 📋 dto/
│   ├── ConsertoDTO.java
│   ├── ConsertoResumoDTO.java
│   └── ConsertoAtualizacaoDTO.java
│
├── 💾 repository/
│   └── ConsertoRepository.java
│
├── ⚙️ service/
│   └── ConsertoService.java
│
└── 🌐 controller/
    └── ConsertoController.java

src/main/resources/
├── application.properties
└── db/migration/
    ├── V1__Create_consertos_table.sql
    ├── V2__Insert_sample_data.sql
    ├── V3__Add_veiculo_cor.sql
    └── V4__Add_ativo_field.sql
```

---

## ✨ Funcionalidades

### 📊 Modelo de Dados

- **Veículo** → marca, modelo, ano, cor (opcional)
- **Mecânico** → nome, anos de experiência
- **Conserto** → datas de entrada/saída, veículo, mecânico, status ativo

### ✅ Validações Implementadas

| Campo | Regra |
|-------|-------|
| Datas | Formato obrigatório `dd/mm/aaaa` |
| Mecânico Nome | Obrigatório |
| Veículo Marca | Obrigatório |
| Veículo Modelo | Obrigatório |
| Veículo Ano | Obrigatório, formato `aaaa` |
| Veículo Cor | Opcional |

### 🔌 API Endpoints

| Método | Endpoint | Descrição | Status |
|--------|----------|-----------|--------|
| `POST` | `/api/consertos` | Criar novo conserto | 201 |
| `GET` | `/api/consertos` | Listar todos (paginado) | 200 |
| `GET` | `/api/consertos/resumo` | Listar apenas ativos | 200 |
| `GET` | `/api/consertos/{id}` | Buscar por ID | 200/404 |
| `PATCH` | `/api/consertos/{id}` | Atualizar parcialmente | 200 |
| `DELETE` | `/api/consertos/{id}` | Exclusão lógica | 204 |

### 🗑️ Exclusão Lógica (Soft Delete)

- Campo `ativo` controla visibilidade dos registros
- `DELETE` marca como `ativo = false` sem remover do banco
- Listagem `/resumo` exibe apenas registros ativos
- Dados históricos preservados para auditoria

---

## 🚀 Executando o Projeto

### Pré-requisitos

- ☕ Java 21+
- 📦 Maven 3.6+
- 💻 IDE (IntelliJ IDEA, Eclipse ou VS Code)

### Instalação e Execução

**Via Maven:**
```bash
git clone <url-do-repositorio>
cd car-project
mvn spring-boot:run
```

**Via IDE:**
```
Execute: ConsertoCarrosApplication.java
```

### 🗄️ Acessar Console H2

```
URL:      http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:testdb
Username: sa
Password: password
```

---

## 🧪 Casos de Teste

### ✅ Teste 1: Criar Conserto Válido

```bash
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
```

**Esperado:** `201 CREATED` + dados do conserto com `ativo: true`

---

### ❌ Teste 2: Data em Formato Inválido

```bash
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
```

**Esperado:** `400 BAD REQUEST` + mensagem de erro

---

### ❌ Teste 3: Campo Obrigatório Faltando

```bash
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
```

**Esperado:** `400 BAD REQUEST` + erro indicando marca obrigatória

---

### 📋 Teste 4: Listar Resumo (Ativos)

```bash
curl http://localhost:8080/api/consertos/resumo
```

**Resposta Esperada:**
```json
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
```

---

### 🔍 Teste 5: Buscar por ID

```bash
curl http://localhost:8080/api/consertos/1
```

**Esperado:** `200 OK` + dados completos do conserto

---

### ❓ Teste 6: Buscar ID Inexistente

```bash
curl http://localhost:8080/api/consertos/999
```

**Esperado:** `404 NOT FOUND`

---

### 🔄 Teste 7: Atualização Parcial

```bash
curl -X PATCH http://localhost:8080/api/consertos/1 \
  -H "Content-Type: application/json" \
  -d '{
    "dataSaida": "25/10/2024",
    "mecanicoNome": "Roberto Alves",
    "mecanicoAnosExperiencia": 15
  }'
```

**Esperado:** `200 OK` + dados atualizados

---

### 🗑️ Teste 8: Exclusão Lógica

```bash
curl -X DELETE http://localhost:8080/api/consertos/1
```

**Esperado:** `204 NO CONTENT`

**Verificação:**
1. Consulte `/api/consertos/resumo` → ID 1 não aparece
2. No H2 Console: `SELECT * FROM consertos WHERE id = 1;`
3. Registro existe com `ativo = false`

---

### 📄 Teste 9: Paginação

```bash
curl "http://localhost:8080/api/consertos?page=0&size=3"
```

**Resposta Esperada:**
```json
{
  "content": ["..."],
  "totalElements": 10,
  "totalPages": 4,
  "size": 3,
  "number": 0
}
```

---

### 🎨 Teste 10: Campo Cor Opcional

```bash
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
```

**Esperado:** `201 CREATED` + conserto sem campo `cor`

---

## 🔍 Consultas SQL Úteis

### Listar todos os consertos
```sql
SELECT * FROM consertos;
```

### Apenas consertos ativos
```sql
SELECT * FROM consertos WHERE ativo = true;
```

### Verificar estrutura da tabela
```sql
SHOW COLUMNS FROM consertos;
```

### Histórico de migrations
```sql
SELECT * FROM flyway_schema_history;
```

---

## 📌 Notas Importantes

**🗑️ Exclusão Lógica**  
Registros deletados permanecem no banco com `ativo = false`, mas não aparecem na listagem resumida. Isso preserva histórico para auditoria.

**✅ Validações Automáticas**  
Bean Validation processa todas as validações antes da persistência, garantindo integridade dos dados.

**🔄 Migrations Controladas**  
Flyway gerencia automaticamente as versões do schema do banco de dados, facilitando deploy e rollback.

**📡 HTTP Status Adequados**  
Todos os endpoints seguem boas práticas REST com status codes apropriados (200, 201, 204, 400, 404).

---

## 📄 Licença

Projeto acadêmico desenvolvido exclusivamente para fins educacionais.

---

**Desenvolvido com ☕ e 💙 por Bruno Gabriel e Rhuan Andrey**