# 🚗 Sistema de Gerenciamento de Consertos de Veículos com JWT

> API REST desenvolvida em Spring Boot para gerenciar consertos de veículos em oficinas mecânicas, com autenticação JWT, persistência em H2, validações robustas e exclusão lógica de registros.

[![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen?style=for-the-badge&logo=spring)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)](LICENSE)

## 👥 Autores

**Bruno Gabriel Alves Silva** • **Rhuan Andrey de Andrade Boni**

*Projeto Acadêmico - Disciplina de Programação Web 3*

---

## 📋 Índice

- [Características](#-características)
- [Stack Tecnológico](#️-stack-tecnológico)
- [Arquitetura](#-arquitetura)
- [Instalação](#-instalação)
- [Configuração](#️-configuração)
- [Endpoints da API](#-endpoints-da-api)
- [Autenticação JWT](#-autenticação-jwt)
- [Testes](#-testes)
- [H2 Console](#️-h2-console)
- [Segurança](#-segurança)
- [Licença](#-licença)

---

## ✨ Características

### 🔐 Autenticação & Segurança
- ✅ Autenticação **stateless via JWT**
- ✅ Senha criptografada com **BCrypt**
- ✅ Tokens com expiração configurável (2 horas)
- ✅ Roles de usuário (ADMIN, USER)
- ✅ Endpoints públicos e protegidos
- ✅ Session policy: **STATELESS**

### 📊 Gerenciamento de Dados
- ✅ **UUID** como identificador único
- ✅ **Soft Delete** (exclusão lógica)
- ✅ Paginação de resultados
- ✅ Validações automáticas com Bean Validation
- ✅ Migrations controladas com Flyway

### 🏗️ Arquitetura
- ✅ Padrão **REST API**
- ✅ Arquitetura em camadas (Controller → Service → Repository)
- ✅ DTOs para transferência de dados
- ✅ Embeddable entities (Veiculo, Mecanico)

---

## 🛠️ Stack Tecnológico

```
Java 21                    Linguagem base
Spring Boot 3.5.6          Framework principal
Spring Security 6.5.5      Autenticação e autorização
Spring Data JPA            Camada de persistência
H2 Database 2.3.232        Banco de dados em memória
JWT (java-jwt 4.5.0)       JSON Web Tokens
Lombok 1.18.40             Redução de boilerplate
Flyway 11.7.2              Versionamento de schema
Bean Validation            Validação de entrada
Maven                      Gerenciador de dependências
```

---

## 📁 Arquitetura

```
src/main/java/projeto/pw3/carproject/conserto/
│
├── 📦 model/
│   ├── Conserto.java          # Entidade principal (UUID)
│   ├── Usuario.java           # Entity + UserDetails
│   ├── UsuarioRole.java       # Enum (ADMIN, USER)
│   ├── Veiculo.java           # Embeddable
│   └── Mecanico.java          # Embeddable
│
├── 📋 dto/
│   ├── ConsertoDTO.java
│   ├── ConsertoResumoDTO.java
│   ├── ConsertoAtualizacaoDTO.java
│   ├── LoginDTO.java
│   ├── RegisterDTO.java
│   └── TokenDTO.java
│
├── 💾 repository/
│   ├── ConsertoRepository.java
│   └── UsuarioRepository.java
│
├── ⚙️ service/
│   ├── ConsertoService.java
│   ├── PW3TokenService.java       # Geração/validação JWT
│   └── AutenticacaoService.java   # UserDetailsService
│
├── 🔒 security/
│   ├── SecurityFilter.java        # OncePerRequestFilter
│   └── SecurityConfigurations.java # Config Spring Security
│
└── 🌐 controller/
    ├── ConsertoController.java
    └── AuthController.java

src/main/resources/
├── application.properties
└── db/migration/
    ├── V1__Create_consertos_table.sql
    ├── V2__Insert_sample_data.sql
    ├── V3__Add_veiculo_cor.sql
    ├── V4__Add_ativo_field.sql
    └── V5__Create_usuarios_table.sql
```

---

## 🚀 Instalação

### Pré-requisitos

- ☕ **Java 21+** ([Download](https://www.oracle.com/java/technologies/downloads/))
- 📦 **Maven 3.6+** ([Download](https://maven.apache.org/download.cgi))
- 💻 **IDE** (IntelliJ IDEA, Eclipse ou VS Code)

### Clone o Repositório

```bash
git clone https://github.com/seu-usuario/car-project.git
cd car-project
```

### Compile e Execute

```bash
# Via Maven
mvn clean install
mvn spring-boot:run

# Ou via IDE
# Execute: ConsertoCarrosApplication.java
```

A aplicação estará disponível em: **http://localhost:8080**

---

## ⚙️ Configuração

### application.properties

```properties
# Application
spring.application.name=car-project

# Database H2
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=password

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true

# Flyway
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration

# JWT Secret (use variável de ambiente em produção)
api.security.token.secret=${JWT_SECRET:sua-chave-secreta-super-segura}
```

### Variáveis de Ambiente (Produção)

```bash
export JWT_SECRET="sua-chave-secreta-super-segura-minimo-256-bits"
```

---

## 🌐 Endpoints da API

### 🔓 Endpoints Públicos (Autenticação)

#### POST `/api/auth/register`
Registrar novo usuário

**Request:**
```json
{
  "login": "usuario",
  "senha": "senha123",
  "role": "USER"
}
```

**Response:** `201 Created`

---

#### POST `/api/auth/login`
Fazer login e obter token JWT

**Request:**
```json
{
  "login": "usuario",
  "senha": "senha123"
}
```

**Response:** `200 OK`
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

---

### 🔒 Endpoints Protegidos (Consertos)

> **Requer:** `Authorization: Bearer <token>`

#### POST `/api/consertos`
Criar novo conserto

**Request:**
```json
{
  "dataEntrada": "25/11/2024",
  "dataSaida": "30/11/2024",
  "veiculoMarca": "Toyota",
  "veiculoModelo": "Corolla",
  "veiculoAno": "2023",
  "veiculoCor": "Prata",
  "mecanicoNome": "João Silva",
  "mecanicoAnosExperiencia": 10
}
```

**Response:** `201 Created`

---

#### GET `/api/consertos`
Listar todos os consertos (paginado)

**Query Params:**
- `page` (default: 0)
- `size` (default: 20)

**Response:** `200 OK`
```json
{
  "content": [...],
  "totalElements": 10,
  "totalPages": 1,
  "size": 20,
  "number": 0
}
```

---

#### GET `/api/consertos/resumo`
Listar apenas consertos ativos

**Response:** `200 OK`
```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "dataEntrada": "25/11/2024",
    "dataSaida": "30/11/2024",
    "mecanicoNome": "João Silva",
    "veiculoMarca": "Toyota",
    "veiculoModelo": "Corolla"
  }
]
```

---

#### GET `/api/consertos/{id}`
Buscar conserto por UUID

**Response:** `200 OK` ou `404 Not Found`

---

#### PATCH `/api/consertos/{id}`
Atualizar conserto parcialmente

**Request:**
```json
{
  "dataSaida": "02/12/2024",
  "mecanicoNome": "Carlos Alberto",
  "mecanicoAnosExperiencia": 15
}
```

**Response:** `200 OK` ou `404 Not Found`

---

#### DELETE `/api/consertos/{id}`
Excluir conserto logicamente (soft delete)

**Response:** `204 No Content` ou `404 Not Found`

---

## 🔐 Autenticação JWT

### Como Usar

1. **Registre um usuário:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"login":"teste","senha":"senha123","role":"USER"}'
```

2. **Faça login:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"login":"teste","senha":"senha123"}'
```

3. **Use o token nos endpoints protegidos:**
```bash
curl http://localhost:8080/api/consertos/resumo \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

### Características do Token

- ⏱️ **Expiração:** 2 horas
- 🔒 **Algoritmo:** HMAC256
- 📋 **Claims:** issuer, subject (login), expiration
- 🚫 **Stateless:** Não armazenado no servidor

---

## 🧪 Testes

### Teste 1: Criar Conserto Válido

```bash
TOKEN="seu_token_aqui"

curl -X POST http://localhost:8080/api/consertos \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "dataEntrada": "25/11/2024",
    "dataSaida": "30/11/2024",
    "veiculoMarca": "Ferrari",
    "veiculoModelo": "F40",
    "veiculoAno": "2024",
    "veiculoCor": "Vermelho",
    "mecanicoNome": "Bruno Silva",
    "mecanicoAnosExperiencia": 15
  }'
```

**Esperado:** `201 Created` + JSON com UUID

---

### Teste 2: Validação - Data Inválida

```bash
curl -X POST http://localhost:8080/api/consertos \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "dataEntrada": "2024-11-25",
    "dataSaida": "30/11/2024",
    "veiculoMarca": "Fiat",
    "veiculoModelo": "Uno",
    "veiculoAno": "2020",
    "mecanicoNome": "José",
    "mecanicoAnosExperiencia": 5
  }'
```

**Esperado:** `400 Bad Request` + mensagem de erro

---

### Teste 3: Soft Delete

```bash
# 1. Deletar conserto
curl -X DELETE http://localhost:8080/api/consertos/{UUID} \
  -H "Authorization: Bearer $TOKEN"

# 2. Verificar que não aparece no resumo
curl http://localhost:8080/api/consertos/resumo \
  -H "Authorization: Bearer $TOKEN"

# 3. Verificar que ainda existe no banco (ativo=false)
# Acesse H2 Console: SELECT * FROM consertos WHERE id = '{UUID}'
```

---

### Teste 4: Acesso Sem Token

```bash
curl http://localhost:8080/api/consertos/resumo
```

**Esperado:** `403 Forbidden`

---

## 🗄️ H2 Console

### Acessar

```
URL:      http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:testdb
Username: sa
Password: password
```

### Consultas Úteis

```sql
-- Listar todos os consertos
SELECT * FROM consertos;

-- Apenas ativos
SELECT * FROM consertos WHERE ativo = true;

-- Consertos deletados (soft delete)
SELECT * FROM consertos WHERE ativo = false;

-- Todos os usuários
SELECT id, login, "role" FROM usuarios;

-- Histórico de migrations
SELECT * FROM flyway_schema_history;
```

---

## 🔒 Segurança

### ✅ Implementações de Segurança

| Recurso | Implementação |
|---------|---------------|
| **Autenticação** | JWT stateless |
| **Senha** | BCrypt com salt automático |
| **Sessão** | STATELESS (sem cookies) |
| **CSRF** | Desabilitado (API stateless) |
| **Token Expiration** | 2 horas configurável |
| **Endpoints Públicos** | `/api/auth/**`, `/h2-console/**` |
| **Soft Delete** | Preserva dados para auditoria |
| **UUID** | Identificadores não-sequenciais |

### ⚠️ Importante

- **Produção:** SEMPRE use HTTPS
- **JWT_SECRET:** Configure via variável de ambiente
- **Secret forte:** Mínimo 256 bits
- **H2 Console:** Desabilite em produção
- **Logs:** Remova logs de debug em produção

---

## 📊 Validações Implementadas

| Campo | Regra |
|-------|-------|
| **Data Entrada/Saída** | Formato obrigatório `dd/mm/aaaa` |
| **Mecânico Nome** | Obrigatório, não vazio |
| **Veículo Marca** | Obrigatória, não vazia |
| **Veículo Modelo** | Obrigatório, não vazio |
| **Veículo Ano** | Obrigatório, formato `aaaa` (4 dígitos) |
| **Veículo Cor** | Opcional |
| **Login** | Obrigatório, único |
| **Senha** | Obrigatória (min 6 caracteres recomendado) |
| **Role** | Obrigatória (ADMIN ou USER) |

---

## 🔄 Fluxo de Dados

```
┌─────────┐         ┌────────────┐         ┌──────────┐         ┌──────────┐
│ Cliente │────────>│ Controller │────────>│ Service  │────────>│   Repo   │
└─────────┘         └────────────┘         └──────────┘         └──────────┘
     │                     │                      │                    │
     │  1. POST /login     │                      │                    │
     ├────────────────────>│                      │                    │
     │                     │  2. authenticate()   │                    │
     │                     ├─────────────────────>│                    │
     │                     │                      │ 3. findByLogin()   │
     │                     │                      ├───────────────────>│
     │                     │                      │<───────────────────┤
     │                     │  4. gerarToken()     │                    │
     │                     │<─────────────────────┤                    │
     │  5. {token: "..."}  │                      │                    │
     │<────────────────────┤                      │                    │
     │                     │                      │                    │
     │  6. GET /consertos  │                      │                    │
     │  + Bearer token     │                      │                    │
     ├────────────────────>│ 7. SecurityFilter    │                    │
     │                     │    valida token      │                    │
     │                     │                      │ 8. findAll()       │
     │                     │                      ├───────────────────>│
     │  9. [consertos...]  │                      │                    │
     │<────────────────────┴──────────────────────┴────────────────────┘
```

---

## 📦 Dependências Principais

```xml
<!-- Spring Boot -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- Spring Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- Spring Data JPA -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- JWT -->
<dependency>
    <groupId>com.auth0</groupId>
    <artifactId>java-jwt</artifactId>
    <version>4.5.0</version>
</dependency>

<!-- H2 Database -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- Flyway -->
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>

<!-- Validation -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>

<!-- Lombok -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
```

---

## 🐛 Troubleshooting

### Erro: "Access Denied" (403)
- ✅ Verifique se o token está no formato: `Authorization: Bearer <token>`
- ✅ Confirme que o token não expirou (2 horas de validade)
- ✅ Faça login novamente para obter novo token

### Erro: "Bad Credentials"
- ✅ Senha incorreta
- ✅ Usuário não existe
- ✅ Verifique o hash BCrypt no banco

### Token não valida
- ✅ Secret configurada incorretamente
- ✅ Token alterado manualmente
- ✅ Formato inválido

### Flyway Migration Failed
- ✅ Delete o diretório `target/`
- ✅ Execute `mvn clean install`
- ✅ Verifique sintaxe SQL nas migrations





---

## 📄 Licença

Este projeto é um trabalho acadêmico desenvolvido para fins educacionais.

Copyright © 2024 Bruno Gabriel Alves Silva & Rhuan Andrey de Andrade Boni

---

## 🤝 Contribuindo

Este é um projeto acadêmico, mas sugestões são bem-vindas!

1. Fork o projeto
2. Crie uma branch (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request



---

<div align="center">

**Desenvolvido com ☕ e 💙**


</div>